package com.cocosh.shmstore.baiduScan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.cocosh.shmstore.R
import com.cocosh.shmstore.baiduFace.utils.FileUtil
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.enterpriseCertification.ui.CompanyInformationActivity
import com.cocosh.shmstore.enterpriseCertification.ui.CorporateAccountActivty
import com.cocosh.shmstore.newCertification.contrat.CertificationContrat
import com.cocosh.shmstore.newCertification.presenter.CertificationPresenter
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.NetworkUtils
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_scan_lisence.*

/**
 * Created by lmg on 2018/3/23.
 */
class ScanLicenseActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    private var licensePath = ""
    private var hasGotToken = false
    private val REQUEST_CODE_BUSINESS_LICENSE = 123
    private var pageType = -1

    override fun setLayout(): Int = R.layout.activity_scan_lisence

    override fun initView() {
        titleManager.defaultTitle("营业执照验证")
        pageType = intent.getIntExtra("PAGE_TYPE", -1)
        iv_scan.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            iv_scan.id -> startCamera()
        }
    }

    private fun startCamera() {
        var intent = Intent(this@ScanLicenseActivity, CameraActivity::class.java)
        licensePath = FileUtil.getSaveFile(application, System.currentTimeMillis().toString()).absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, licensePath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL)
        startActivityForResult(intent, REQUEST_CODE_BUSINESS_LICENSE)
    }

    //相机结果回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 识别成功回调，营业执照识别
        if (requestCode == REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {
            showLoading()
//            GlideUtils.loadFullScreen(this, licensePath, iv_scan)
            initAccessToken()
        }
    }

    private fun openInfoPage() {
        val intent = Intent(this, CompanyInformationActivity::class.java)
        intent.putExtra("licensePath", licensePath)
        intent.putExtra("PAGE_TYPE", pageType)
        startActivity(intent)
    }

    //初始化Token并识别身份证信息
    private fun initAccessToken() {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.show(getString(R.string.networkError))
            return
        }

        if (hasGotToken) {
            openInfoPage()
        } else {
            OCR.getInstance().initAccessToken(object : OnResultListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {
                    hasGotToken = true
                    openInfoPage()
                }

                override fun onError(error: OCRError) {
                    error.printStackTrace()
                    hasGotToken = false
                    ToastUtil.show("获取token失败,请重试")
                }
            }, applicationContext)
        }
    }

    companion object {
        fun loadActivity(context: Context) {
            context.startActivity(Intent(context, ScanLicenseActivity::class.java))
        }

        fun loadActivity(context: Context, pageType: Int) {
            context.startActivity(Intent(context, ScanLicenseActivity::class.java).putExtra("PAGE_TYPE", pageType))
        }
    }
}