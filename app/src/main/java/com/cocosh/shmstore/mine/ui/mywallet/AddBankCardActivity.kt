package com.cocosh.shmstore.mine.ui.mywallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduFace.utils.FileUtil
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.model.BankCardModel
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.utils.ocr.RecognizeService
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_add_bankcard.*

/**
 * Created by lmg on 2018/4/17.
 */
class AddBankCardActivity : BaseActivity() {
    private var bankCardPath = ""
    private var hasGotToken = false
    private val BANK_CARD = 123
    var isNumberOk = false

    override fun setLayout(): Int = R.layout.activity_add_bankcard

    override fun initView() {
        titleManager.defaultTitle("添加银行卡").setLeftOnClickListener(View.OnClickListener {
            if (SmApplication.getApp().activityName != null) {
                startActivity(Intent(this@AddBankCardActivity, SmApplication.getApp().activityName))
                return@OnClickListener
            }
            finish()
        })
        nameDialog.setOnClickListener(this)
        scan.setOnClickListener(this)
        next.setOnClickListener(this)
        edtPersonName.setText(UserManager.getMemberEntrance()?.realName)
        edtAccount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!edtAccount.text.isNullOrEmpty() && edtAccount.text.length < 20 && edtAccount.text.length > 11) {
                    isNumberOk = true
                    next.setBackgroundResource(R.color.red)
                    return
                }
                isNumberOk = false
                next.setBackgroundResource(R.color.grayBtn)
            }
        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            nameDialog.id -> {
                val desc = "为了您的账户资金安全，只能绑定持卡人本人的银行卡。"
                val title = "持卡人说明"
                showDescDialog(desc, title)
            }
            scan.id -> {
                startCamera()
            }
            next.id -> {
                if (isNumberOk) {
                    //存储
                    val map = HashMap<String, String>()
                    map["cardUserName"] = edtPersonName.text.toString()
                    map["cardNumber"] = edtAccount.text.toString()
                    SmApplication.getApp().setData(DataCode.ADDBANK_KEY_MAP, map)
                    InputBankCardInfo.start(this)
                }
            }
        }
    }

    override fun reTryGetData() {

    }

    //相机结果回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 识别成功回调，营业执照识别
        if (requestCode == BANK_CARD && resultCode == Activity.RESULT_OK) {
            showLoading()
            initAccessToken()
        }
    }

    /*
        解析图片数据
     */
    fun scanData() {
        runOnUiThread {
            showLoading()
        }

        RecognizeService.recBankCard(bankCardPath, RecognizeService.ServiceListener {
            LogUtil.e(it)
            runOnUiThread {
                hideLoading()
            }

            if (!TextUtils.isEmpty(it)) {
                try {
                    var datas = it.split("\n")
                    var bankCardModel = BankCardModel(datas[0].split("：")[1], datas[1].split("：")[1], datas[2].split("：")[1])
                    if (TextUtils.equals(bankCardModel.卡号, "无")) {
                        runOnUiThread {
                            showScanErrorDialog()
                        }
                        return@ServiceListener
                    }
                    //赋值
                    edtAccount.setText(bankCardModel.卡号?.replace(" ", ""))
                } catch (e: Exception) {
                    runOnUiThread { showScanErrorDialog() }
                    return@ServiceListener
                }
            }

        })
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
                    runOnUiThread {
                        hideLoading()
                        ToastUtil.show("获取token失败,请重试")
                    }
                }
            }, applicationContext)
        }
    }

    /**
     * 打开扫描相机
     */
    private fun startCamera() {
        var intent = Intent(this@AddBankCardActivity, CameraActivity::class.java)
        bankCardPath = FileUtil.getSaveFile(application, System.currentTimeMillis().toString()).absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, bankCardPath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD)
        startActivityForResult(intent, BANK_CARD)
    }

    private fun showDescDialog(desc: String, title: String) {
        val dialog = SmediaDialog(this)
        dialog.singleButton()
        dialog.setTitle(title)
        dialog.setDesc(desc)
        dialog.setPositiveText("知道了")
        dialog.show()
    }

    private fun showScanErrorDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("识别失败，请重新拍照")
        dialog.singleButton()
        dialog.show()
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, AddBankCardActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if (SmApplication.getApp().activityName != null) {
            startActivity(Intent(this@AddBankCardActivity, SmApplication.getApp().activityName))
            return
        }
        finish()
        return
    }
}