package com.cocosh.shmstore.home

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.home.model.BonusConfig
import com.cocosh.shmstore.home.model.BonusParam
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
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
    private var type = "1"
    private var paramsList = ArrayList<BonusParam>()

    override fun onTopClick() {
        cameraPhotoUtils.startCamera()
    }

    override fun onBottomClick() {
        cameraPhotoUtils.startPoto()
    }

    override fun onResult(file: File) {
        this.file = file
        ApiManager2.postImage(this, file.path, Constant.COMMON_UPLOADS, object : ApiManager2.OnResult<BaseBean<ArrayList<String>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<String>>) {
                data.message?.let {
                    if (boolean) {
                        topPhotoUrl = it[0]
                        Glide.with(this@SendBonusDetailActivity).load(file).into(ivBonusPhoto)
                    } else {
                        bottomPhotoUrl = it[0]
                        Glide.with(this@SendBonusDetailActivity).load(file).into(ivAdPhoto)
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ArrayList<String>>) {

            }

        })

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
        type = intent.getStringExtra("type")

//        loadData()

//        if (type) {
//            val bonusConfig = SmApplication.getApp().getData<BonusConfig>(DataCode.BONUS_CONFIG, false)
//            bonusConfig?.businessADInfo?.let {
//                edtDesc.setText(it.desc)
//                edtDesc.setSelection(edtDesc.text.length)
//                topPhotoUrl = it.bannerImg?.url
//                Glide.with(this).load(topPhotoUrl).into(ivBonusPhoto)
//                bottomPhotoUrl = it.contentImg?.url
//                Glide.with(this).load(bottomPhotoUrl).into(ivAdPhoto)
//            }
//
//        }

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

        paramsList.clear()

        SmApplication.getApp().getData<ArrayList<BonusParam>>(DataCode.SEND_BONUS, false)?.let {
            paramsList.addAll(it)
        }

        paramsList?.let {
            it.add(BonusParam("ado", "ado_slider_master", topPhotoUrl ?: ""))
            it.add(BonusParam("ado", "ado_desc", edtDesc.text.toString()))
            it.add(BonusParam("ado", "ado_images", bottomPhotoUrl ?: ""))
        }

        val parmas = HashMap<String, String>()
        parmas["type_id"] = type
        parmas["data"] = ApiManager2.gson.toJson(paramsList)

        ApiManager2.post(this, parmas, Constant.RP_CREATE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {

                val it = Intent(this@SendBonusDetailActivity, PayActivity::class.java)
                it.putExtra("amount", intent.getStringExtra("money"))
                it.putExtra("no", data.message)
                startActivity(it)
                setResult(IntentCode.FINISH)
                finish()
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }


    fun loadData() {
        val param = hashMapOf<String, String>()
        param["ad_id"] = "1"
        param["type_id"] = type

        ApiManager2.get(1, this, param, Constant.RP_AD_ATTRS, object : ApiManager2.OnResult<BaseBean<ArrayList<BonusParam>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<BonusParam>>) {
                data.message?.let {


                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<ArrayList<BonusParam>>) {

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
        SmApplication.getApp().removeData(DataCode.SEND_BONUS)
        SmApplication.getApp().deleteActivity(DataCode.BONUS_SEND_ACTIVITYS, this)
        super.onDestroy()
    }

}