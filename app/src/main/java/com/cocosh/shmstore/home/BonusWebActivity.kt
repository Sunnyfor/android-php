package com.cocosh.shmstore.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.AddressMangerActivity
import com.cocosh.shmstore.model.ValueByKey
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.BonusErrorDialog
import com.cocosh.shmstore.widget.dialog.ReportDialog
import com.cocosh.shmstore.widget.dialog.ShareDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_bonus_web.*
import org.json.JSONObject

/**
 *
 * Created by zhangye on 2018/5/11.
 */
class BonusWebActivity : BaseActivity() {

    var type = 0
    var title: String? = null
    var id: String? = null
    var state: String? = null
    private var htmlURL: String? = null
    private var downURL: String? = null
    private var companyLogo: String? = null
    private var companyName: String? = null
    private var typeInfo: String? = null
    override fun setLayout(): Int = R.layout.activity_bonus_web

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(this, "android")
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true// 打开本地缓存提供JS调用,至关重要
        webView.settings.setAppCacheEnabled(true)
        webView.settings.setAppCachePath(application.cacheDir.absolutePath)
        webView.settings.databaseEnabled = true
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                result?.cancel()
                return true
            }
        })


        webView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()
                llayoutBtn.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()
                if (typeInfo == "1" || typeInfo == "2" || typeInfo == "3") {
                    llayoutBtn.visibility = View.VISIBLE
                }
            }
        })
        btnSure.setOnClickListener(this)
        btnOpen.setOnClickListener(this)

        id = intent.getStringExtra("comment_id")
        title = intent.getStringExtra("title")
        state = intent.getStringExtra("state")
        htmlURL = intent.getStringExtra("htmUrl")
        downURL = intent.getStringExtra("downUrl")
        companyLogo = intent.getStringExtra("companyLogo")
        companyName = intent.getStringExtra("companyName")
        type = intent.getIntExtra("advertisementBaseType", 0)
        typeInfo = intent.getStringExtra("typeInfo")

        htmlURL?.let { webView.loadUrl(it) }


        when (type) {
            1 -> {
                //App下载
                btnSure.text = "立即下载"
            }
            2 -> {
                //购买
                btnSure.visibility = View.VISIBLE
            }
            else -> {
                //不可购买
                btnSure.visibility = View.GONE
            }
        }


        state?.let {
            if (it == "RECEIVE") {
                btnOpen.text = "已领取"
                cannotReceive()
            }

            if (it == "ISGIVE") {
                btnOpen.text = "已赠送"
                cannotReceive()
            }

            if (it == "NONE") {
                BonusErrorDialog(this).showNone(companyLogo, companyName)
                btnOpen.text = "已抢光"
                cannotReceive()
            }

            if (it == "PREVIEW") {
                btnSure.isClickable = false
                btnOpen.isClickable = false
                titleManager.defaultTitle(title ?: "")
            } else {

                if (typeInfo == "1") {
                    titleManager.rightText(title ?: "", "分享", View.OnClickListener {
                        //                        val diaolog = ReportDialog(this, comment_id.toString(), "1")
//                        diaolog.show()
                        share(true)
                    })
                } else if (typeInfo == "2" || typeInfo == "3") {
                    titleManager.defaultTitle(title ?: "")
                } else {
                    titleManager.rightText(title ?: "", "分享", View.OnClickListener {
                        share(false)
                    })
                }
            }
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                if (type == 1) {
                    try {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse(downURL)
                        startActivity(intent)
                    } catch (e: Exception) {
                        ToastUtil.show("下载地址错误")
                    }
                } else {
                    ToastUtil.show("敬请期待！")
                    //确认订单页
//                    CreateOrderActivity.start(this)
                }
            }
            btnOpen.id -> {
                if (typeInfo == "3"){
                    checkFollow()
                }else{
                    openBonus()
                }
            }
        }
    }

    private fun openBonus(){
        val intent = Intent(this, BonusDetailActivity::class.java)
        intent.putExtra("comment_id", id)
        intent.putExtra("typeInfo", typeInfo)
        intent.putExtra("title", intent.getStringExtra("title"))
        intent.putExtra("companyLogo", companyLogo)
        intent.putExtra("companyName", companyName)
        startActivity(intent)
    }


    override fun reTryGetData() {
    }

    fun loadData() {

    }

    //领取红包按钮不可点击状态
    private fun cannotReceive() {
        SmApplication.getApp().removeData(DataCode.BONUS_ID)
        btnOpen.isClickable = false
        btnOpen.setTextColor(ContextCompat.getColor(this, R.color.grayText))
        btnOpen.setBackgroundResource(R.drawable.shape_btn_border_gray)

    }


    override fun onResume() {
        super.onResume()
        SmApplication.getApp().getData<BonusAction>(DataCode.BONUS, false)?.let {
            cannotReceive()
            if (it == BonusAction.GIVE) {
                btnOpen.text = "已赠送"
            } else {
                btnOpen.text = "已领取"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SmApplication.getApp().removeData(DataCode.BONUS_ID)
        if (webView != null) {
            val parent = webView.parent
            if (parent != null) {
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
    }

    @JavascriptInterface
    fun smAlert(message: String?) {
        val mDialog = SmediaDialog(this@BonusWebActivity)
        mDialog.setTitle(message)
        mDialog.singleButton()
        mDialog.show()
//        hideLoading()
    }


    /**
     * 获取用户Token
     */
    @JavascriptInterface
    fun getUserToken(): String = UserManager.getUserToken()


    @JavascriptInterface
    fun intentAddress() {
        val intent = Intent(this, AddressMangerActivity::class.java)
        intent.putExtra("type", "web")
        startActivityForResult(intent, IntentCode.IS_INPUT)
    }

    @JavascriptInterface
    fun intentHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_INPUT && resultCode == Activity.RESULT_OK) {
            val jsonObject = JSONObject()
            jsonObject.put("idUserAddressInfo", data?.getStringExtra("idUserAddressInfo"))
            jsonObject.put("addressName", data?.getStringExtra("addressName"))
            jsonObject.put("addressPhone", data?.getStringExtra("addressPhone"))
            jsonObject.put("mAddress", data?.getStringExtra("mAddress"))
            jsonObject.put("areaName", data?.getStringExtra("areaName"))
            webView.loadUrl("javascript:getAddressInfo($jsonObject)")
        }
    }

    private fun share(type: Boolean) {
        val params = HashMap<String, String>()
        params["dictionaryKey"] = "index_share_url"
        ApiManager.get(0, this, params, Constant.GET_SHARE_URL, object : ApiManager.OnResult<BaseModel<ValueByKey>>() {
            override fun onSuccess(data: BaseModel<ValueByKey>) {
                if (data.success) {
                    val diaolog = ShareDialog(this@BonusWebActivity)
                    if (typeInfo == "4" || typeInfo == "5") {
                        if (type) {
                            diaolog.showShareBase("温暖中国  媒体扶贫行动！", "参与扶贫，无需捐款，一次点击就是一次扶贫行动！", data.entity?.url
                                    ?: "")
                        } else {
                            diaolog.showShareApp(data.entity?.url ?: "")
                        }
                    } else {
                        diaolog.showShareApp(data.entity?.url ?: "")
                    }
                    diaolog.show()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<ValueByKey>) {
            }

        })
    }

    private fun checkFollow() {
        val params = HashMap<String, String>()
        params["redPacketOrderId"] = id ?: ""
        ApiManager.get(0, this, params, Constant.BONUS_CHECKFOLLOW, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) = if (data.entity == "1"){
                openBonus()
            }else{
               val dialog = SmediaDialog(this@BonusWebActivity)
                dialog.setTitle("该企业将获取你的头像和昵称等信息")
                dialog.setDesc("点击同意同时关注该企业")
                dialog.setDescColor(ContextCompat.getColor(this@BonusWebActivity,R.color.grayText))
                dialog.OnClickListener = View.OnClickListener {
                    openBonus()
                }
                dialog.show()
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }
}