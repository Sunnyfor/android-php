package com.cocosh.shmstore.term

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_term.*
import java.util.*

/**
 * 服务条款
 * Created by zhangye on 2018/1/25.
 */
class ServiceTermActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_term

    override fun initView() {
        titleManager.defaultTitle("首媒服务条款")
        val ruleUrl = intent.getStringExtra("OPEN_TYPE")
        if (ruleUrl != null) {
            if (ruleUrl == Constant.CHARGE_SERVICE_RULE) {
                getRuleUrl(ruleUrl)
                return
            }
            initWebView(ruleUrl, true)
        }
        btnSure.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                setResult(IntentCode.IS_TERM)
                finish()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(url: String, boolean: Boolean) {
        if (boolean) {
            webView.loadUrl(ApiManager.SALE_URL + url)
        } else {
            webView.loadUrl(url)
        }

        val webSettings = webView.settings
        //JS交互支持
        webSettings.javaScriptEnabled = true
        //设置缓存
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        val appCachePath = applicationContext.cacheDir.absolutePath
        webSettings.setAppCachePath(appCachePath)
        webSettings.setAppCacheEnabled(true)
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        //        webSettings.setAppCacheEnabled(true);
        //        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        //放大或者缩小
        webSettings.builtInZoomControls = false
        //显示放大缩小控制按钮
        webSettings.displayZoomControls = false
        webSettings.setSupportZoom(false)
        webSettings.useWideViewPort = true
        //多窗口打开界面
        webSettings.setSupportMultipleWindows(false)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        //设置网页初始放大倍数


        //使用本地窗口打开
        webView.setWebViewClient(MyWebViewClient())

        webView.setWebChromeClient(object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
//                h5Binding.progressBar1.setVisibility(View.VISIBLE)
//                h5Binding.progressBar1.setProgress(newProgress)
//                if (newProgress == 100) {
//                    h5Binding.progressBar1.setVisibility(View.GONE)
//                }
                super.onProgressChanged(view, newProgress)
            }
        })
        webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack()
                    return@OnKeyListener true
                }
            }
            false
        })

    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun onReceivedHttpError(view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse) {
            super.onReceivedHttpError(view, request, errorResponse)
        }

        /**
         * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
         */
        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        /**
         * 当接收到https错误时，会回调此函数，在其中可以做错误处理
         */
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            LogUtil.e(error.toString())
            super.onReceivedSslError(view, handler, error)
        }

        /**
         * 拦截 url 跳转,在里边添加点击链接跳转或者操作
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView, url: String) {
            LogUtil.e("onPageFinished:" + url)
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //退出H5界面
            if (url.contains("native_smapp/newsDetail_imageBrowse")) {//跳转图集

            } else if (url.contains("native_smapp/newsDetail_labelsClick")) {//跳转到搜索

            } else {
                return false
            }
            return true
        }

    }

    fun getRuleUrl(url: String) {
        val map = HashMap<String, String>()
        ApiManager.get(1, this, map, url, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success && data.code == 200) {
                    initWebView(data.entity ?: "", false)
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<String>) {

            }
        })
    }
}