package com.cocosh.shmstore.baiduScan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity
import com.cocosh.shmstore.R
import com.cocosh.shmstore.baiduFace.utils.FileUtil
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.model.BankCardModel
import com.cocosh.shmstore.mine.ui.mywallet.CheckBankCardInfoActivity
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.NetworkUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.ocr.RecognizeService
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_scan_lisence.*

/**
 * Created by lmg on 2018/3/23.
 */
class ScanBankCardActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    private var bankCardPath = ""
    private var hasGotToken = false
    private val BANK_CARD = 123
    private var pageType = -1

    override fun setLayout(): Int = R.layout.activity_scan_bankcard

    override fun initView() {
        titleManager.defaultTitle("添加银行卡")
        pageType = intent.getIntExtra("PAGE_TYPE", -1)
        iv_scan.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            iv_scan.id -> startCamera()
        }
    }

    private fun startCamera() {
        var intent = Intent(this@ScanBankCardActivity, CameraActivity::class.java)
        bankCardPath = FileUtil.getSaveFile(application, System.currentTimeMillis().toString()).absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, bankCardPath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD)
        startActivityForResult(intent, BANK_CARD)
    }

    //相机结果回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 识别成功回调，营业执照识别
        if (requestCode == BANK_CARD && resultCode == Activity.RESULT_OK) {
            showLoading()
//            GlideUtils.loadFullScreen(this, licensePath, iv_scan)
            initAccessToken()
        }
    }

    private fun openInfoPage() {
        val intent = Intent(this, CheckBankCardInfoActivity::class.java)
        intent.putExtra("bankNumber", bankCardPath)
        intent.putExtra("bankType", bankCardPath)
        intent.putExtra("name", bankCardPath)
        startActivity(intent)
        finish()
    }

    //初始化Token并识别身份证信息
    private fun initAccessToken() {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.show(getString(R.string.networkError))
            return
        }

        if (hasGotToken) {
            scanData()
        } else {
            OCR.getInstance().initAccessToken(object : OnResultListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {
                    hasGotToken = true
                    scanData()
                }

                override fun onError(error: OCRError) {
                    error.printStackTrace()
                    hasGotToken = false
                    ToastUtil.show("获取token失败,请重试")
                }
            }, applicationContext)
        }
    }

    fun scanData() {
        showLoading()
        RecognizeService.recBankCard(bankCardPath, RecognizeService.ServiceListener {
            LogUtil.e(it)
            hideLoading()
            if (!TextUtils.isEmpty(it)) {
                try {
                    var datas = it.split("\n")
                    var bankCardModel = BankCardModel(datas[0].split("：")[1], datas[1].split("：")[1], datas[2].split("：")[1])
                    if (TextUtils.equals(bankCardModel.卡号, "无") || TextUtils.equals(bankCardModel.发卡行, "无")) {
                        showScanErrorDialog()
                        return@ServiceListener
                    }
                    openInfoPage()
                } catch (e: Exception) {
                    showScanErrorDialog()
                    return@ServiceListener
                }
            }

        })
    }

    private fun showScanErrorDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("识别失败，请重新拍照")
        dialog.singleButton()
        dialog.show()
    }

    companion object {
        fun loadActivity(context: Context) {
            context.startActivity(Intent(context, ScanBankCardActivity::class.java))
        }

        fun loadActivity(context: Context, pageType: Int) {
            context.startActivity(Intent(context, ScanBankCardActivity::class.java).putExtra("PAGE_TYPE", pageType))
        }
    }
}