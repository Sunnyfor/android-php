package com.cocosh.shmstore.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.home.BonusPoolActivity
import com.cocosh.shmstore.mine.ui.AddressMangerActivity
import com.cocosh.shmstore.mine.ui.mywallet.RedAccountActivity
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.ShareDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_web.*
import org.json.JSONObject
import android.view.ViewGroup
import com.cocosh.shmstore.utils.IntentCode


/**
 *
 * Created by zhangye on 2018/5/30.
 */
class WebViewActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_web

    @SuppressLint("JavascriptInterface", "AddJavascriptInterface", "SetJavaScriptEnabled")
    override fun initView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true// 打开本地缓存提供JS调用,至关重要
        webView.settings.setAppCacheEnabled(true)
        webView.settings.setAppCachePath(application.cacheDir.absolutePath)
        webView.settings.databaseEnabled = true

        webView.addJavascriptInterface(this, "android")
        webView.setWebViewClient(object : WebViewClient() {})
        webView.setWebChromeClient(object : WebChromeClient() {})
        val url = intent.getStringExtra("url")
        titleManager.defaultTitle(intent.getStringExtra("title") ?: "")

        webView.loadUrl(url)
//        webView.loadUrl("http://10.10.100.104:10002/prize/detail.html")

        intent.getBooleanExtra("showButton",false).let {
            if (it){
                btnNext.visibility = View.VISIBLE
            }
        }

        btnNext.setOnClickListener {
            setResult(IntentCode.IS_TERM)
            finish()
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    /**
     * 获取城市信息
     */
    @JavascriptInterface
    fun getLocation(): String = Gson().toJson(SmApplication.getApp().getData<Location>(DataCode.LOCATION, false))
            ?: ""

    /**
     * 分享
     */
    @JavascriptInterface
    fun share(title: String, description: String, url: String) {
        ShareDialog(this).showShareBase(title, description, url)
    }

    /**
     * 分享
     */
    @JavascriptInterface
    fun share(title: String, description: String, url: String, imageUrl: String?) {
        ShareDialog(this).showShareBase(title, description, url, imageUrl)
    }

    /**
     * 获取用户资料
     */
    @JavascriptInterface
    fun getUserInfo(): String {
        val login = UserManager.getLogin()
        val memberEntrance = UserManager.getMemberEntrance()
        val jsonObj = JSONObject()
        jsonObj.put("userId", login?.userId)
        jsonObj.put("token", login?.token)
        jsonObj.put("userNick", memberEntrance?.userNick)
        jsonObj.put("headPic", memberEntrance?.headPic)
        return jsonObj.toString()
    }

    /**
     * 获取用户Token
     */
    @JavascriptInterface
    fun getUserToken(): String  = UserManager.getUserToken()

    /**
     * 跳转红包资金池
     */
    @JavascriptInterface
    fun intentBonus() {
        startActivity(Intent(this, BonusPoolActivity::class.java))
    }

    /**
     * 跳转红包账户
     */
    @JavascriptInterface
    fun intentBonusAccount() {
        RedAccountActivity.start(this)
    }

    /**
     * 跳转收货地址
     */
    @JavascriptInterface
    fun intentAddress() {
        val intent = Intent(this,AddressMangerActivity::class.java)
        intent.putExtra("type","web")
        startActivityForResult(intent,IntentCode.IS_INPUT)
    }

    /**
     * 是否登录
     */
    @JavascriptInterface
    fun isLogin(): Boolean = UserManager.isLogin()



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_INPUT && resultCode == Activity.RESULT_OK){
            val jsonObject = JSONObject()
            jsonObject.put("idUserAddressInfo",data?.getStringExtra("idUserAddressInfo"))
            jsonObject.put("addressName",data?.getStringExtra("addressName"))
            jsonObject.put("addressPhone",data?.getStringExtra("addressPhone"))
            jsonObject.put("address",data?.getStringExtra("address"))
            jsonObject.put("areaName",data?.getStringExtra("areaName"))
            webView.loadUrl("javascript:getAddressInfo($jsonObject)")
        }
    }


    override fun onDestroy() {
        if (webView != null){
            val parent = webView.parent
            if (parent != null){
                (parent as ViewGroup).removeView(webView)
            }
            webView.stopLoading()
            webView.settings.javaScriptEnabled = false
            webView.clearCache(true)
            webView.clearHistory()
            webView.clearView()
            webView.removeAllViews()
            webView.destroy()
        }
        super.onDestroy()
    }
}