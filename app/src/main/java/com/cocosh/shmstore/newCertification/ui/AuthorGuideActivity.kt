package com.cocosh.shmstore.newCertification.ui

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
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
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_author_guide.*

/**
 *
 * Created by cjl on 2018/2/2.
 */
class AuthorGuideActivity : BaseActivity(), CertificationContrat.IView {
    override fun reTryGetData() {

    }

    var mPresenter: CertificationContrat.IPresenter = CertificationPresenter(this, this)
    //识别
    var front_ok = false
    var back_ok = false
    //qiniu
    var back = false
    var front = false

    private val handles = Handler(Handler.Callback { msg ->
        if (msg.what == 1) {
            back = true
        } else if (msg.what == 2) {
            front = true
        }
        if (back && front) {
            hideLoading()
            back = false
            front = false
            CheckIdentityInfoActivity.start(this@AuthorGuideActivity)
        }
        false
    })

    override fun idCardResult(idCardSide: String, result: IDCardResult?) {
        println(idCardSide + "------" + result)
        if (result == null) {
            if (idCardSide == "front") {
                front_ok = false
            }
            if (idCardSide == "back") {
                back_ok = false
            }

            if (!NetworkUtils.isNetworkAvaliable(this)) {
                runOnUiThread {
                    hideLoading()
                    ToastUtil.show("网络连接失败")
                }
                return
            }
//            ToastUtil.show("识别失败")
            runOnUiThread {
            hideLoading()

             val dialog = SmediaDialog(this)
             dialog.singleButton()
             dialog.setTitle("识别失败，请重新拍照")
             dialog.setPositiveText("确定")

             dialog.OnClickListener= View.OnClickListener {
                 dialog.dismiss()
                 if (idCardSide == "front") {
                   frontStartActivity()
                 }
                 if (idCardSide == "back") {
                   backStartActivity()
                 }
             }
                dialog.show()
         }

            return
        }
        SmApplication.getApp().setData(idCardSide, result)
        if ("front" == idCardSide) {
            front_ok = true
            hideLoading()
            ToastUtil.show("正面识别成功")
        }
        if ("back" == idCardSide) {
            back_ok = true
            hideLoading()
            ToastUtil.show("背面识别成功")
        }
        if (front_ok && back_ok) {
            btnSure.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        } else {
            btnSure.setBackgroundColor(ContextCompat.getColor(this, R.color.grayBtn))
        }
    }

    var frontPath: String? = null
    var backPath: String? = null
    val REQUEST_CODE_CAMERA = 102

    override fun setLayout(): Int = R.layout.activity_author_guide

    override fun initView() {
        titleManager.defaultTitle(getString(R.string.newAuthen))
        initAccessToken()
        id_card_back.setOnClickListener(this)
        id_card_front.setOnClickListener(this)
        btnSure.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.id_card_back -> backStartActivity()
            R.id.id_card_front -> frontStartActivity()

            R.id.btnSure -> {
                if (front_ok && back_ok) {
//                    mPresenter.tokenRequest()
                }
            }
        }
    }

    private fun backStartActivity() {
        val intent = Intent(this@AuthorGuideActivity, CameraActivity::class.java)
        backPath = FileUtil.getSaveFile(application, "back").absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, backPath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun frontStartActivity() {
        val intent = Intent(this@AuthorGuideActivity, CameraActivity::class.java)
        frontPath = FileUtil.getSaveFile(application, "front").absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, frontPath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT == contentType) {
                        GlideUtils.noCacheload(this, frontPath, id_card_front.getImageView())
                        mPresenter.recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, frontPath!!)
                        showLoading()

                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK == contentType) {
                        GlideUtils.noCacheload(this, backPath, id_card_back.getImageView())
                        mPresenter.recIDCard(IDCardParams.ID_CARD_SIDE_BACK, backPath!!)
                        showLoading()
                    }
                }
            }
        }
    }

    fun tokenResult(token: String) {
        LogUtil.d(token)
        //把token存起来
        SmApplication.getApp().setData(DataCode.QINIU_TOKEN, token)

        showLoading()
        if (!NetUtil.isConnected(this)) {
            hideLoading()
            ToastUtil.show("网络未连接")
            return
        }
        Thread(Runnable {

            val uploadManager = UploadManager()
            val app = SmApplication.getApp()

            val keyFront = UserManager.getUserId() + "front.jpg"//活体图片
            val keyBack = UserManager.getUserId() + "back.jpg"//活体图片

            //向七牛发送front图片
            uploadManager.put(frontPath, keyFront, token, { key, info, response ->
                LogUtil.d("key:" + key + "\r\n" + info + "\r\n" + response)
                if (response == null) {
                    runOnUiThread {
                        ToastUtil.show("七牛发送失败")
                    }
//                    finish()
//                    return
                }
                if (info.isOK) {
                    LogUtil.d("qiniuFront-----success")
                } else {
                    LogUtil.d("qiniuFront--fail")
                }

                app.setData(DataCode.FRONT_URL, key)
                val m = Message.obtain()
                m.what = 2
                handles.sendMessage(m)
            }, null)

            //向七牛发送back图片
            uploadManager.put(backPath, keyBack, token, { key, info, response ->
                LogUtil.d("key:" + key + "\r\n" + info + "\r\n" + response)
                if (info.isOK) {
                    LogUtil.d("qiniuBack-----success")
                } else {
                    LogUtil.d("qiniuBack--fail")
                    if (info.statusCode == ResponseInfo.InvalidToken) {
                        SmApplication.getApp().removeData(DataCode.QINIU_TOKEN)
                    }
                }

                app.setData(DataCode.BACK_URL, key)
                val m = Message.obtain()
                m.what = 1
                handles.sendMessage(m)
            }, null)

        }).start()

    }

    private fun initAccessToken() {
        OCR.getInstance().initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {

            }
            override fun onError(error: OCRError) {
                error.printStackTrace()
            }
        }, applicationContext)
    }

}