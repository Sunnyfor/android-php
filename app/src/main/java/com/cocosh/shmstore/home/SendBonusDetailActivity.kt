package com.cocosh.shmstore.home

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.BonusConfig
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.newCertification.ui.PayActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_bonus_send_detail.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File


/**
 * 发送红包填写内容页
 * Created by zhangye on 2018/4/23.
 */
class SendBonusDetailActivity : BaseActivity(), BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {

    private var topPhotoUrl: String? = null
    private var bottomPhotoUrl: String? = null
    private var file: File? = null
    private var boolean = true
    private var isfill = false

    override fun onTopClick() {
        cameraPhotoUtils.startCamera()
    }

    override fun onBottomClick() {
        cameraPhotoUtils.startPoto()
    }

    override fun onResult(file: File) {
        this.file = file
        tokenRequest()
    }

    private val cameraPhotoUtils = CameraPhotoUtils(this)

    override fun setLayout(): Int = R.layout.activity_bonus_send_detail

    override fun initView() {

        SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this)

        titleManager.defaultTitle("发红包")
        btnNext.setOnClickListener(this)
        ivBonusPhoto.setOnClickListener(this)
        ivAdPhoto.setOnClickListener(this)
        cameraPhotoUtils.onResultListener = this
        isfill = intent.getBooleanExtra("isfill", false)

        if (isfill) {
            val bonusConfig = SmApplication.getApp().getData<BonusConfig>(DataCode.BONUS_CONFIG, false)
            bonusConfig?.businessADInfo?.let {
                edtDesc.setText(it.desc)
                edtDesc.setSelection(edtDesc.text.length)
                topPhotoUrl = it.bannerImg?.url
                Glide.with(this).load(topPhotoUrl).into(ivBonusPhoto)
                bottomPhotoUrl = it.contentImg?.url
                Glide.with(this).load(bottomPhotoUrl).into(ivAdPhoto)
            }

        }

        tvMoney.text = ("需支付 ${intent.getStringExtra("money")}元")
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnNext.id -> {
                if (topPhotoUrl == null) {
                    ToastUtil.show("红包图片不能为空")
                    return
                }

                if (bottomPhotoUrl == null) {
                    ToastUtil.show("广告图片不能为空")
                    return
                }

                if (edtDesc.text.isEmpty()) {
                    ToastUtil.show("描述不能为空")
                    return
                }
                saveAd()
            }
            ivBonusPhoto.id -> {
                boolean = true
                cameraPhotoUtils.setAspectXY(resources.getDimension(R.dimen.w640).toInt(), resources.getDimension(R.dimen.w360).toInt())
                val dialog = BottomPhotoDialog(this)
                dialog.setOnItemClickListener(this)
                dialog.show()
            }

            ivAdPhoto.id -> {
                boolean = false
                cameraPhotoUtils.setAspectXY(0, 0)
                val dialog = BottomPhotoDialog(this)
                dialog.setOnItemClickListener(this)
                dialog.show()
            }
        }
    }

    override fun reTryGetData() {

    }


    private fun saveAd() {
        val parmas = HashMap<String, String>()

        parmas["bannerImg.orderId"] = "0"

        if (!topPhotoUrl!!.contains(Constant.QINIU_KEY_HEAD)) {
            topPhotoUrl = Constant.QINIU_KEY_HEAD + topPhotoUrl
        }
        parmas["bannerImg.url"] = topPhotoUrl ?: ""
        parmas["contentImg.orderId"] = "0"

        if (!bottomPhotoUrl!!.contains(Constant.QINIU_KEY_HEAD)) {
            bottomPhotoUrl = Constant.QINIU_KEY_HEAD + bottomPhotoUrl
        }
        parmas["contentImg.url"] = bottomPhotoUrl ?: ""
        parmas["idRedPacketOrder"] = intent.getStringExtra("idRedPacketOrder")
        parmas["desc"] = edtDesc.text.toString()
        ApiManager.post(this, parmas, Constant.BONUS_AD_SAVE, object : ApiManager.OnResult<BaseModel<Int>>() {
            override fun onSuccess(data: BaseModel<Int>) {
                if (data.success) {
                    val it = Intent(this@SendBonusDetailActivity, PayActivity::class.java)
                    it.putExtra("amount", intent.getStringExtra("money"))
                    it.putExtra("runningNumber", SmApplication.getApp().getData<BonusConfig>(DataCode.BONUS_CONFIG, false)?.orderNumber)
//                    it.putExtra("payOperatStatus", AuthenStatus.SEND_RED_PACKET.type)
                    startActivity(it)
                    setResult(IntentCode.FINISH)
                    finish()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<Int>) {

            }

        })
    }


    /**
     * 上传图片
     */
    private fun update(token: String) {
        showLoading()
        val url = UserManager.getUserId() + System.currentTimeMillis() + "bonusPhoto.jpg"
        if (boolean) {
            topPhotoUrl = url
        } else {
            bottomPhotoUrl = url
        }

        UploadManager().put(file, url, token, { key, info, _ ->
            runOnUiThread {
                hideLoading()
                if (info.isOK) {
                    if (boolean) {
                        Glide.with(this).load(file).into(ivBonusPhoto)
                    } else {
                        Glide.with(this).load(file).into(ivAdPhoto)
                    }
                } else {
                    if (info.statusCode == ResponseInfo.InvalidToken) {
                        SmApplication.getApp().removeData(DataCode.QINIU_TOKEN)
                    }
                    if (boolean) {
                        topPhotoUrl = null
                    } else {
                        bottomPhotoUrl = null
                    }
                    hideLoading()
                    ToastUtil.show("提交失败，请稍后重试！")
                }
            }
        }, null)
    }


    //获取七牛token
    private fun tokenRequest() {

        showLoading()

        val map = java.util.HashMap<String, String>()
        map["dataType"] = "1"
        ApiManager.get(0, this, map, Constant.FACE_TOKEN, object : ApiManager.OnResult<String>() {

            override fun onCatch(data: String) {}

            override fun onFailed(e: Throwable) {
                hideLoading()
                LogUtil.d("获取token失败" + e)
            }

            override fun onSuccess(data: String) {
                hideLoading()
                LogUtil.d("获取七牛Token结果：" + data)
                try {
                    val jsonObject = JSONObject(data)
                    val mToken = jsonObject.optString("token")
                    if (TextUtils.isEmpty(mToken)) {
                        ToastUtil.show("七牛Token为空")
                    } else {

                        update(mToken)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraPhotoUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraPhotoUtils.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        SmApplication.getApp().deleteActivity(DataCode.BONUS_SEND_ACTIVITYS, this)
        super.onDestroy()
    }
}