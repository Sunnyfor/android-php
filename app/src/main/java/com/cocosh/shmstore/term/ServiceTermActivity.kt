package com.cocosh.shmstore.term

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.model.ValueByKey
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.OpenType
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_term.*


/**
 * 协议相关web加载页面
 * Created by zhangye on 2018/1/25.
 */
class ServiceTermActivity : BaseActivity() {

    private val fac = "f_cert"
    private val recharge = "recharge"
    private val register = "register"
    private val newcer = "x_cert"
    private val businessman = "b_cert"
    private val help = "help" //帮助中心
    private val bonus = "rp_rules"//红包规则

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_term

    override fun initView() {
        val ruleUrl = intent.getStringExtra("OPEN_TYPE")
        val title = intent.getStringExtra("title")
        if (title == null) {
            titleManager.defaultTitle("首媒服务条款")
        } else {
            titleManager.defaultTitle(title)
        }

        btnSure.setOnClickListener(this)

        if (ruleUrl == OpenType.Fac.name) {
            getUrl(fac)
        }
        if (ruleUrl == OpenType.Cer.name) {
            getUrl(newcer)
        }
        if (ruleUrl == OpenType.Charge.name) {
            getUrl(recharge)
        }
        if (ruleUrl == OpenType.BusinessMan.name) {
            getUrl(businessman)
        }
        if (ruleUrl == OpenType.Help.name){
            btnSure.visibility = View.GONE
            getUrl(help)
        }
        if (ruleUrl == OpenType.Bonus.name){
            btnSure.visibility = View.GONE
            getUrl(bonus)
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                setResult(IntentCode.IS_TERM)
                finish()
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

    private fun getUrl(key: String) {
        val params = hashMapOf<String, String>()
        params["type"] = key
        ApiManager2.get(this, params, Constant.COMMON_AGREEMENT, object : ApiManager2.OnResult<BaseBean<ValueByKey>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<ValueByKey>) {
                    initWebView(data.message?.url ?: "")
            }

            override fun onCatch(data: BaseBean<ValueByKey>) {
            }
        })

    }
}