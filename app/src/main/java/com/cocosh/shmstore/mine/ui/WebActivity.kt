package com.cocosh.shmstore.mine.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cocosh.shmstore.R
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorFailActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorSplashActivity
import com.cocosh.shmstore.facilitator.ui.PayFranchiseFeeActivity
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.mine.ui.authentication.IncomeActivity
import com.cocosh.shmstore.newCertification.ui.PartherPendingPayActivity
import com.cocosh.shmstore.newCertification.ui.PartnerSplashActivity
import com.cocosh.shmstore.utils.OpenType
import com.cocosh.shmstore.utils.PermissionCode
import com.cocosh.shmstore.utils.PermissionUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Created by lmg on 2018/5/29.
 */
class WebActivity : BaseActivity() {
    var type: String? = null
    override fun setLayout(): Int = R.layout.activity_web_view
    var status: String? =null
    override fun initView() {
        status = intent.getStringExtra("status")
        val ruleUrl: String? = intent.getStringExtra("loadUrl")
        type = intent.getStringExtra("TYPE")
        permissionUtil = PermissionUtil(this)
        btnSure.setOnClickListener(this)
        when (type) {
            OpenType.Cer.name -> {
                titleManager.defaultTitle("人人为媒，人脉资源变现")
                btnSure.text = "我要成为新媒人"
                if (status == "5") {
                    btnSure.visibility = View.GONE
                } else {
                    btnSure.visibility = View.VISIBLE
                }
            }
            OpenType.Fac.name -> {
                titleManager.defaultTitle("造船出海，不如借船起航")
                btnSure.text = "我要成为服务商"
                if (status == "5") {
                    btnSure.visibility = View.GONE
                } else {
                    btnSure.visibility = View.VISIBLE
                }
            }
            OpenType.SysMessage.name -> {
                titleManager.defaultTitle(status ?: "")
                btnSure.visibility = View.GONE
            }
        }

        if (ruleUrl != null) {
            initWebView(ruleUrl)
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                when (type) {
                    OpenType.Cer.name -> {

                        if (UserManager.getMemberEntrance()?.personStatus == AuthenStatus.PERSION_OK.type) {
                            //UNCERTIFIED("未认证"),PRE_DRAFT("待付款"), PRE_PASS("已认证")
                            when (status) {
                                AuthenStatus.UNCERTIFIED.type -> PartnerSplashActivity.start(this)
                                AuthenStatus.PRE_DRAFT.type -> startActivity(Intent(this, PartherPendingPayActivity::class.java))
                                AuthenStatus.PRE_PASS.type -> IncomeActivity.start(this, CommonType.CERTIFICATION_INCOME.type)
                                AuthenStatus.PRE_AUTH.type -> startActivity(Intent(this, PartherPendingPayActivity::class.java))
                            }
                        } else {
                            val dialog = SmediaDialog(this)
                            dialog.setTitle("请先完成实人认证")
                            dialog.setDesc("确保实人认证信息与新媒人认证信息一致")

                            dialog.OnClickListener = View.OnClickListener {
                                val intent = Intent(this, ScanIdCardActivity::class.java)
                                intent.putExtra("type", "实人认证")
                                startActivity(intent)
                            }
                            dialog.show()
                        }
                    }
                    OpenType.Fac.name -> {
                        when (status) {
                            AuthenStatus.UNCERTIFIED.type -> FacilitatorSplashActivity.start(this)
                            AuthenStatus.PRE_DRAFT.type -> PayFranchiseFeeActivity.start(this, 666)
                            AuthenStatus.PRE_PASS.type -> IncomeActivity.start(this, CommonType.FACILITATOR_INCOME.type)
                            AuthenStatus.AUTH_FAILED.type -> FacilitatorFailActivity.start(this, -1)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface")
    private fun initWebView(url: String) {
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(this, "android")
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true// 打开本地缓存提供JS调用,至关重要
        webView.settings.setAppCacheEnabled(true)
        webView.settings.setAppCachePath(application.cacheDir.absolutePath)
        webView.settings.databaseEnabled = true
//        webView.setLayerType(View.LAYER_TYPE_NONE, null);
        webView.setWebChromeClient(WebChromeClient())
        webView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()
            }
        })

    }

    companion object {
        fun start(mContext: Context, type: String?, url: String?, status: String?) {
            mContext.startActivity(Intent(mContext, WebActivity::class.java).putExtra("TYPE", type).putExtra("loadUrl", url).putExtra("status", status))
        }
    }

    private lateinit var permissionUtil: PermissionUtil
    var phoneNumber: String? = null

    @JavascriptInterface
    fun jsCallPhone(number: String) {
        phoneNumber = number
        val dialog = SmediaDialog(this)
        dialog.setTitle("首媒客服电话")
        dialog.setDesc(number)
        dialog.setPositiveText("呼叫")
        dialog.OnClickListener = View.OnClickListener {
            if (permissionUtil.callPermission()) {
                if (!number.isNullOrEmpty()) {
                    callPhone(number)
                }
            }
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCode.PHONE.type) {
            if (permissionUtil.checkPermission(permissions)) {
                if (!phoneNumber.isNullOrEmpty()) {
                    callPhone(phoneNumber ?: "")
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun callPhone(number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    override fun reTryGetData() {

    }
}