package com.cocosh.shmstore.baiduScan

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.view.View
import com.baidu.idl.util.NetUtil
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduFace.utils.FileUtil
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newCertification.contrat.CertificationContrat
import com.cocosh.shmstore.newCertification.presenter.CertificationPresenter
import com.cocosh.shmstore.newCertification.ui.CheckIdentityInfoActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_scan_id.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


/**
 *
 * Created by zhangye on 2018/3/23.
 */
class ScanIdCardActivity : BaseActivity(), CertificationContrat.IView {
    override fun reTryGetData() {

    }

    private var mPresenter: CertificationContrat.IPresenter = CertificationPresenter(this, this)
    private var backPath = ""
    private var frontPath = ""
    private var hasGotToken = false
    private var frontUpOk = false
    private var backUpOk = false
    private var frontScanOk = false
    private var backScanOk = false
    private var type = ""

    private val uploadManager = UploadManager()

    private val handler = Handler(Handler.Callback {

        if (frontScanOk && backScanOk) {
            btnSure.setBackgroundResource(R.color.red)
        } else {
            btnSure.setBackgroundResource(R.color.grayBtn)
        }

        //身份证上传OK，跳转信息确认页面
        if (frontUpOk && backUpOk) {
            frontUpOk = false
            backUpOk = false
            hideLoading()
            val mIntent = Intent(this, CheckIdentityInfoActivity::class.java)
            mIntent.putExtra("isAgent", intent.getBooleanExtra("isAgent", false))
            mIntent.putExtra("type", type)
            startActivity(mIntent)
        }
        return@Callback false
    })


    override fun setLayout(): Int = R.layout.activity_scan_id

    override fun initView() {

        type = intent.getStringExtra("type")

        tvDesc.text = ("${type}认证通过后将不能修改")
        titleManager.defaultTitle("${type}身份验证")
        ivIdFront.setOnClickListener(this)
        ivIdBack.setOnClickListener(this)
        ivIdBack.setType(true)
        btnSure.setOnClickListener(this) //提交按钮
    }

    override fun onListener(view: View) {
        when (view.id) {
            ivIdFront.id -> frontID()
            ivIdBack.id -> backID()
            btnSure.id -> {
                if (frontScanOk && backScanOk) {
                    mPresenter.tokenRequest()
                }
            }
        }
    }


    //启动背面扫描
    private fun backID() {
        val intent = Intent(this, CameraActivity::class.java)
        backPath = FileUtil.getSaveFile(application, "back").absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, backPath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK)
        startActivityForResult(intent, IntentCode.IS_CAMERA)
    }

    //启动正面扫描
    private fun frontID() {
        val intent = Intent(this, CameraActivity::class.java)
        frontPath = FileUtil.getSaveFile(application, "front").absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, frontPath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
        startActivityForResult(intent, IntentCode.IS_CAMERA)
    }

    //相机结果回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT == contentType) {
                        initAccessToken(IDCardParams.ID_CARD_SIDE_FRONT, frontPath)

                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK == contentType) {
                        initAccessToken(IDCardParams.ID_CARD_SIDE_BACK, backPath)
                    }
                }
            }
        }
    }


    //身份证扫描结果回调
    override fun idCardResult(idCardSide: String, result: IDCardResult?) {
        LogUtil.i("身份证识别结果：" + result)
        if (idCardSide == IDCardParams.ID_CARD_SIDE_FRONT) {

            if (result != null) {
                ivIdFront.loadImage(frontPath)
                frontScanOk = true
                ToastUtil.showIcon(null, "正面识别成功")
                if ("代办人" == type) {
                    SmApplication.getApp().setData(DataCode.AGENT_FRONT_DATA, result)
                } else {
                    SmApplication.getApp().setData(idCardSide, result)
                }
            } else {
                frontScanOk = false
                showScanErrorDialog()
                ivIdFront.defaultImage()
            }
            handler.sendEmptyMessage(1)
        }

        if (idCardSide == IDCardParams.ID_CARD_SIDE_BACK) {

            if (result != null) {
                ivIdBack.loadImage(backPath)
                backScanOk = true

                ToastUtil.showIcon(null, "反面识别成功")

                if ("代办人" == type) {
                    SmApplication.getApp().setData(DataCode.AGENT_BACK_DATA, result)
                } else {
                    SmApplication.getApp().setData(idCardSide, result)
                }
            } else {
                backScanOk = false
                showScanErrorDialog()
                ivIdBack.defaultImage()
            }
            handler.sendEmptyMessage(1)
        }
    }

    //上传图片到七牛
    override fun tokenResult(token: String) {
        //七牛token存储到内存
        SmApplication.getApp().setData(DataCode.QINIU_TOKEN, token)

        showLoading()
        if (!NetUtil.isConnected(this)) {
            hideLoading()
            ToastUtil.show(getString(R.string.networkError))
            return
        }

        val keyFront = UserManager.getUserId() + System.currentTimeMillis() + "front.jpg"//身份证正面
        val keyBack = UserManager.getUserId() + System.currentTimeMillis() + "back.jpg"//身份证背面图片

        //上传正面身份证
        showLoading()
        uploadManager.put(frontPath, keyFront, token, { key, info, _ ->
            runOnUiThread {
                if (info.isOK) {
                    //存储图片路径（用于接口提交数据）
                    if ("代办人" == type) {
                        SmApplication.getApp().setData(DataCode.AGENT_FRONT_URL, key)
                    } else {
                        SmApplication.getApp().setData(DataCode.FRONT_URL, key)
                    }
                    frontUpOk = true
                } else {
                    if (info.statusCode == ResponseInfo.InvalidToken) {
                        SmApplication.getApp().removeData(DataCode.QINIU_TOKEN)
                    }
                    frontUpOk = false
                    hideLoading()
                    ToastUtil.show("提交失败，请稍后重试！")
                }
            }
            handler.sendEmptyMessage(1)
        }, null)

        //上传背面身份证
        uploadManager.put(backPath, keyBack, token, { key, info, _ ->
            runOnUiThread {
                if (info.isOK) {
                    //存储图片路径（用于接口提交数据）
                    backUpOk = true
                    if ("代办人" == type) {
                        SmApplication.getApp().setData(DataCode.AGENT_BACK_URL, key)
                    } else {
                        SmApplication.getApp().setData(DataCode.BACK_URL, key)
                    }
                } else {
                    if (info.statusCode == ResponseInfo.InvalidToken) {
                        SmApplication.getApp().removeData(DataCode.QINIU_TOKEN)
                    }
                    hideLoading()
                    backUpOk = false
                    ToastUtil.show("提交失败，请稍后重试！")
                }
            }
            handler.sendEmptyMessage(1)
        }, null)

    }


    private fun showScanErrorDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("识别失败，请重新拍照")
        dialog.singleButton()
        dialog.show()
    }


    //初始化Token并识别身份证信息
    private fun initAccessToken(idCardSide: String, filePath: String) {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.show(getString(R.string.networkError))
            return
        }

        if (hasGotToken) {
            mPresenter.recIDCard(idCardSide, filePath)
        } else {
            OCR.getInstance().initAccessToken(object : OnResultListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {
                    hasGotToken = true
                    mPresenter.recIDCard(idCardSide, filePath)
                }

                override fun onError(error: OCRError) {
                    error.printStackTrace()
                    hasGotToken = false
                    launch(UI) {
                        ToastUtil.show("获取token失败,请重试")
                    }
                }
            }, applicationContext)
        }
    }
}