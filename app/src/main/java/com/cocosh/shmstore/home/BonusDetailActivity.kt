package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import android.view.animation.Animation
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.home.model.BonusOpen
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.term.ServiceTermActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BonusErrorDialog
import com.cocosh.shmstore.widget.dialog.ShareDialog
import kotlinx.android.synthetic.main.activity_bonus_detail.*

/**
 * 红包详情页
 * Created by zhangye on 2018/4/24.
 */
class BonusDetailActivity : BaseActivity() {

    var id: String? = null
    var type: String? = null
    var bonusOpen: BonusOpen? = null
    var token:String? = null
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
                intent.putExtra("comment_id", id)
                intent.putExtra(DataCode.BONUS_MONEY, bonusOpen)
                startActivity(intent)
                SmApplication.getApp().setData(DataCode.BONUS, BonusAction.OPEN)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })


        id = intent.getStringExtra("comment_id")

        type = intent.getStringExtra("typeInfo")

        type?.let {
            if (it != "common" && it != "comm_person") {
                btnShare.visibility = View.GONE
            }
        }

        titleManager.rightText("红包", "红包规则", View.OnClickListener {
            val intent = Intent(this, ServiceTermActivity::class.java).apply {
                putExtra("title", "红包规则")
                putExtra("OPEN_TYPE", when (this@BonusDetailActivity.type) {
                    "common" -> OpenType.BonusPublic.name
                    "special" -> OpenType.BonusAccurate.name
                    "fans" -> OpenType.BonusFans.name
                    else -> OpenType.BonusPublic.name
                })
            }
            startActivity(intent)
        })


        token = intent.getStringExtra("token")


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
                ToastUtil.show("敬请期待！")
//                id?.let {
//                    val shareDialog = ShareDialog(this)
//                    shareDialog.isFinish = true
//                    shareDialog.showGiveBouns(
//                            it, token?:"",redpacketId)
//                }
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
        val params = hashMapOf<String, String>()
        id?.let {
            params["no"] = it
        }
        params["token"] = token?:""

        ApiManager2.post(this, params, Constant.RP_DO_FAV, object : ApiManager2.OnResult<BaseBean<String>>() {

            override fun onSuccess(data: BaseBean<String>) {
                ToastUtil.showIcon(null, "收藏成功")
                SmApplication.getApp().setData(DataCode.BONUS, BonusAction.COLLECT)
                finish()
            }

            override fun onCatch(data: BaseBean<String>) {

            }

            override fun onFailed(code: String, message: String) {
            }
        })
    }


    //开红包
    private fun open() {

        val url = if (redpacketId != null) {
            Constant.RP_FAV_TO_OPEN
        } else {
            Constant.RP_DO_OPEN
        }

        val params = hashMapOf<String, String>()
        id?.let {
            params["no"] = it
        }
        params["token"] = token?:""

        ApiManager2.post(this, params, url, object : ApiManager2.OnResult<BaseBean<BonusOpen>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<BonusOpen>) {
                bonusOpen = data.message
                btnOpen.setBackgroundResource(R.drawable.bg_btn_money)
                btnOpen.startAnimation(animation)
            }

            override fun onCatch(data: BaseBean<BonusOpen>) {

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

    fun showErrorDialog(message: String?) {
        BonusErrorDialog(this).showDialog(companyLogo, companyName, message)
    }

}