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
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.home.model.GetRedPackage
import com.cocosh.shmstore.home.model.RedPackage
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
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

    var type = "comm_person"  //红包类型
    var title: String? = null
    var no: String? = null
    var state: String? = null
    private var htmlURL: String? = null
    private var downURL: String? = null
    private var companyLogo: String? = null
    private var companyName: String? = null
    override fun setLayout(): Int = R.layout.activity_bonus_web

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
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
//                if (typeInfo == "1" || typeInfo == "2" || typeInfo == "3") {
                llayoutBtn.visibility = View.VISIBLE
//                }
            }
        })
        btnSure.setOnClickListener(this)
        btnOpen.setOnClickListener(this)

        title = intent.getStringExtra("title")
        state = intent.getStringExtra("state")  //用于预览模式

        companyLogo = intent.getStringExtra("companyLogo")
        companyName = intent.getStringExtra("companyName")


        titleManager.defaultTitle(title ?: "")



        state?.let {
            if (it == "PREVIEW") {
                btnSure.isClickable = false
                btnOpen.isClickable = false
            }
//            else {
//
//                if (typeInfo == "1") {
//                    titleManager.rightText(title ?: "", "分享", View.OnClickListener {
//                        //                        val diaolog = ReportDialog(this, comment_id.toString(), "1")
////                        diaolog.show()
//                        share(true)
//                    })
//                } else if (typeInfo == "2" || typeInfo == "3") {
//                    titleManager.defaultTitle(title ?: "")
//                } else {
//                    titleManager.rightText(title ?: "", "分享", View.OnClickListener {
//                        share(false)
//                    })
//                }
//            }
        }

        loadData()
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                if (btnSure.text.toString() == "立即下载") {
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
                if (type == "fans") {
                    checkFollow()
                } else {
                    getBonus()
                }
            }
        }
    }


    override fun reTryGetData() {
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
//        SmApplication.getApp().getData<BonusAction>(DataCode.BONUS, false)?.let {
//            cannotReceive()
//            if (it == BonusAction.GIVE) {
//                btnOpen.text = "已赠送"
//            } else {
//                btnOpen.text = "已领取"
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        SmApplication.getApp().removeData(DataCode.BONUS_ID)
//        if (webView != null) {
//            val parent = webView.parent
//            if (parent != null) {
//                (parent as ViewGroup).removeView(webView)
//            }
//            webView.stopLoading()
//            webView.settings.javaScriptEnabled = false
//            webView.clearCache(true)
//            webView.clearHistory()
//            webView.clearView()
//            webView.removeAllViews()
//            webView.destroy()
//        }
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

//    private fun share(type: Boolean) {
//        val params = HashMap<String, String>()
//        params["dictionaryKey"] = "index_share_url"
//        ApiManager.get(0, this, params, Constant.GET_SHARE_URL, object : ApiManager.OnResult<BaseModel<ValueByKey>>() {
//            override fun onSuccess(data: BaseModel<ValueByKey>) {
//                if (data.success) {
//                    val diaolog = ShareDialog(this@BonusWebActivity)
//                    if (typeInfo == "4" || typeInfo == "5") {
//                        if (type) {
//                            diaolog.showShareBase("温暖中国  媒体扶贫行动！", "参与扶贫，无需捐款，一次点击就是一次扶贫行动！", data.entity?.url
//                                    ?: "")
//                        } else {
//                            diaolog.showShareApp(data.entity?.url ?: "")
//                        }
//                    } else {
//                        diaolog.showShareApp(data.entity?.url ?: "")
//                    }
//                    diaolog.show()
//                } else {
//                    ToastUtil.show(data.message)
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//            }
//
//            override fun onCatch(data: BaseModel<ValueByKey>) {
//            }
//
//        })
//    }

    private fun checkFollow() {
        val params = HashMap<String, String>()
        params["redPacketOrderId"] = no ?: ""
        ApiManager.get(0, this, params, Constant.BONUS_CHECKFOLLOW, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) = if (data.entity == "1") {
//                openBonus()
            } else {
                val dialog = SmediaDialog(this@BonusWebActivity)
                dialog.setTitle("该企业将获取你的头像和昵称等信息")
                dialog.setDesc("点击同意同时关注该企业")
                dialog.setDescColor(ContextCompat.getColor(this@BonusWebActivity, R.color.grayText))
                dialog.OnClickListener = View.OnClickListener {
//                    openBonus()
                }
                dialog.show()
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }

    fun loadData() {
        val params = HashMap<String, String>()
        params["no"] = intent.getStringExtra("no")
        ApiManager2.get(1, this, params, Constant.RP_DETAIL, object : ApiManager2.OnResult<BaseBean<RedPackage>>() {
            override fun onSuccess(data: BaseBean<RedPackage>) {
                data.message?.let {

                    type = it.base?.type ?: "comm_person"
                    no = it.base?.no

                    when (it.hold) {
                        "1" -> {
                            //成功占位(注意:占位有效期为30分钟),可领取状态
                        }
                        "2" -> {
                            //已占位未领取,可领取状态
                        }
                        "3" -> {
                            //已占位已领取(注意:领取但未拆的有效期为10分钟),跳转至<拆红包UI>
                        }

                        "4" -> {
                            //占位已满,不可领取状态
                            cannotReceive()
                        }

                        "5" -> {
                            //已抢过该红包,不可领取状态,显示广告对象相关按钮(立即下载或立即购买)
                            btnOpen.text = "已领取"
                            cannotReceive()
                        }

                        "8" -> {
                            //已抢完,跳转至<哭脸UI>
                            val dialog = BonusErrorDialog(this@BonusWebActivity)
                            dialog.setOnDismissListener {
                                finish()
                            }
                            dialog.showNone(companyLogo, companyName)
                            cannotReceive()
                        }

                        "9" -> {
                            //数据异常,跳转至<数据异常UI>
                        }
                    }

                    it.attrs?.forEach {
                        if (it["item"] as String == "ad") {
                            when (it["val"].toString()) {
                                "2" -> {
                                    //App下载
                                    btnSure.text = "立即下载"
                                }
                                "3" -> {
                                    //购买
                                    btnSure.visibility = View.VISIBLE
                                }
                                else -> {
                                    //不可购买
                                    btnSure.visibility = View.GONE
                                }
                            }
                        }

                        if (it["item"] as String == "ado_url_apk") {
                            downURL = it["val"] as String  //获取下载地址
                        }


                    }
                    it.h5url?.let { webView.loadUrl(it) }
                }


            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<RedPackage>) {
            }
        })

    }


    private fun getBonus() {
        val params = hashMapOf<String, String>()
        params["no"] = no ?: ""
        ApiManager2.post(this, params, Constant.RP_DO_RECIVE, object : ApiManager2.OnResult<BaseBean<GetRedPackage>>() {
            override fun onSuccess(data: BaseBean<GetRedPackage>) {
                data.message?.let {
                    when (it.hold) {
                        "1" -> {
                            //跳转拆红包页面
                            val intent = Intent(this@BonusWebActivity, BonusDetailActivity::class.java)
                            intent.putExtra("comment_id", no)
                            intent.putExtra("typeInfo", type)
                            intent.putExtra("title", intent.getStringExtra("title"))
                            intent.putExtra("token",it.token)
                            intent.putExtra("companyLogo", companyLogo)
                            intent.putExtra("companyName", companyName)
                            startActivity(intent)
                        }

                        "8" -> {
                            val dialog = BonusErrorDialog(this@BonusWebActivity)
                            dialog.setOnDismissListener {
                                finish()
                            }
                            dialog.showNone(companyLogo, companyName)
                        }

                        else -> {
                            ToastUtil.show(it.tip)
                        }
                    }

                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<GetRedPackage>) {
            }

        })
    }
}