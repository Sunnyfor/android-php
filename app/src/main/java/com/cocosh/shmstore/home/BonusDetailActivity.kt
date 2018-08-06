package com.cocosh.shmstore.home

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.home.model.BonusOpen
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.HelpActivity
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.model.ValueByKey
import com.cocosh.shmstore.term.ServiceTermActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.web.WebViewActivity
import com.cocosh.shmstore.widget.dialog.BonusErrorDialog
import com.cocosh.shmstore.widget.dialog.ShareDialog
import com.umeng.socialize.ShareAction
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import kotlinx.android.synthetic.main.activity_bonus_detail.*

/**
 * 红包详情页
 * Created by zhangye on 2018/4/24.
 */
class BonusDetailActivity : BaseActivity() {

    var id: String? = null
    var type: String? = null
    var bonusOpen: BonusOpen? = null
    var animation = BonusYAnimation()
    var companyLogo: String? = null
    var companyName: String? = null

    private var redpacketId: String? = null //此ID表示是收藏红包进来的

    override fun setLayout(): Int = R.layout.activity_bonus_detail

    override fun initView() {
        animation.repeatCount = 1

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                val intent = Intent(this@BonusDetailActivity, BonusOpenActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra(DataCode.BONUS_MONEY, bonusOpen)
                startActivity(intent)
                SmApplication.getApp().setData(DataCode.BONUS, BonusAction.OPEN)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })


        titleManager.rightText("红包", "红包规则", View.OnClickListener {
            val intent = Intent(this, ServiceTermActivity::class.java).apply {
                putExtra("title", "红包规则")
                putExtra("OPEN_TYPE", OpenType.Bonus.name)
            }
            startActivity(intent)
        })

        id = intent.getStringExtra("id")

        type = intent.getStringExtra("typeInfo")
        type?.let {
            if (it != "1") {
                btnShare.visibility = View.GONE
            }
        }

        btnOpen.setOnClickListener(this)
        btnShare.setOnClickListener(this)
        btnCollect.setOnClickListener(this)

        redpacketId = SmApplication.getApp().getData<String>(DataCode.BONUS_ID, false)
        redpacketId?.let {
            btnCollect.visibility = View.GONE
        }

        intent.getStringExtra("companyLogo")?.let {
            GlideUtils.loadHead(this, it, ivHead)
            companyLogo = it
        }

        intent.getStringExtra("companyName")?.let {
            tvSplash.text = ("恭喜您! 领到${it}一个红包")
            tvName.text = it
            companyName = it
        }

        intent.getStringExtra("title")?.let {
            tvDesc.text = it
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnOpen.id -> { //开启红包
                open()
            }
            btnShare.id -> { //分享红包
                id?.let {
                    val shareDialog = ShareDialog(this, getParams())
                    shareDialog.isFinish = true
                    shareDialog.showGiveBouns(
                            it, redpacketId)
                }
            }
            btnCollect.id -> { //收藏红包
                collect()
            }
        }
    }

    override fun reTryGetData() {

    }

    //收藏成功
    private fun collect() {
        val params = getParams()
        id?.let {
            params["redPacketOrderId"] = it
        }

        ApiManager.post(this, params, Constant.BONUS_COLLECT, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success) {
                    ToastUtil.showIcon(null, "收藏成功")
                    SmApplication.getApp().setData(DataCode.BONUS, BonusAction.COLLECT)
                    finish()
                } else {
                    showErrorDialog(data.code, data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<String>) {

            }
        })
    }


    //开红包

    private fun open() {
        val params: HashMap<String, String>
        val url: String
        if (redpacketId != null) {
            params = hashMapOf()
            url = Constant.BONUS_OPEN_COLLECT
            params["redPacketId"] = redpacketId ?: ""
        } else {
            params = getParams()
            url = Constant.BONUS_OPEN
        }

        id?.let {
            params["redPacketOrderId"] = it
        }

        ApiManager.post(this, params, url, object : ApiManager.OnResult<BaseModel<BonusOpen>>() {
            override fun onSuccess(data: BaseModel<BonusOpen>) {
                if (data.success) {
                    bonusOpen = data.entity
                    btnOpen.setBackgroundResource(R.drawable.bg_btn_money)
                    btnOpen.startAnimation(animation)
                } else {
                    showErrorDialog(data.code, data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<BonusOpen>) {

            }

        })

    }

    override fun onResume() {
        super.onResume()
        SmApplication.getApp().getData<BonusAction>(DataCode.BONUS, false)?.let {
            if (it == BonusAction.GIVE) {
                finish()
            }
        }
    }

    fun showErrorDialog(code: Int, message: String?) {
        if (code == 19002) {
            BonusErrorDialog(this).showNone(companyLogo, companyName)
        } else {
            ToastUtil.show(message)
        }
    }


    //开红包、赠送红包、收藏红包put定位数据参数
    private fun getParams(): HashMap<String, String> {
        val params = HashMap<String, String>()
        SmApplication.getApp().getData<Location>(DataCode.LOCATION, false)?.let {
            params["areaCode"] = it.adcode
            params["lat"] = it.latitude
            params["lng"] = it.longitude
        }
        return params
    }
}