package com.cocosh.shmstore.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.LinearLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiDetailAdapter
import com.cocosh.shmstore.home.model.CommentData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.SMediaWebView
import com.cocosh.shmstore.widget.SoftKeyBoardListener
import com.cocosh.shmstore.widget.dialog.ShouMeiCommentDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.cocosh.shmstore.widget.observer.ObserverListener
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.activity_shoumei_detail.*
import kotlinx.android.synthetic.main.item_shoumei_detail_webview.view.*


/**
 * 首媒之家 新闻详情页
 * Created by lmg on 2018/5/31.
 */
class ShoumeiDetailActivity : BaseActivity(), ObserverListener {
    var totalComment: Int? = 0
    var replyPosition = -1
    var currentPage = 1
    val historyMap = hashMapOf<String, String>()
    val mList = arrayListOf<CommentData.SubComment>()
    var lastTimeStamp: String? = ""
    var isSend = false
    lateinit var headView: LinearLayout
    private lateinit var adapter: ShouMeiDetailAdapter
    override fun setLayout(): Int = R.layout.activity_shoumei_detail

    override fun observerUpData(type: Int, data: Any, content: Any, dataExtra: Any) {
        if (type == 2) {
            mList.let {
                var index = it.indexOfFirst {
                    it.idCompanyHomeThemeComment == data as String
                }
                if (index != -1) {
                    totalComment = totalComment!! - (it[index].numberOfReplies ?: "0").toInt()
                    it[index].numberOfReplies = content.toString()
                    totalComment = totalComment!! + (it[index].numberOfReplies ?: "0").toInt()
                    it[index].childResThemeCommentVoList = dataExtra as ArrayList<CommentData.SubComment>
                    adapter.notifyItemRangeChanged(index, it.size - index)
                    ObserverManager.getInstance().notifyObserver(1, id
                            ?: "", totalComment as Any, "")
                }
            }
        }

        if (type == 3) {
            if (data as String == commentId) {
                followType = content as String
                headView.webView.loadUrl("javascript:atCallBack($followType)")
            }
        }
    }

    var id: String? = ""
    var followType: String? = ""
    var commentId: String? = ""
    override fun initView() {
        followType = intent.getStringExtra("FOLLOWTYPE")
        val blackType = intent.getStringExtra("BLACKTYPE")
        id = intent.getStringExtra("baseId")
        commentId = intent.getStringExtra("commentId")
        val themeUrl = intent.getStringExtra("THEMEURL")
        //禁言
        if (blackType == "1") {
            etcontent.isFocusable = false
            tvError.visibility = View.VISIBLE
        } else {
            etcontent.isFocusable = true
            tvError.visibility = View.GONE
        }

        titleManager.defaultTitle("")
        headView = LayoutInflater.from(this).inflate(R.layout.item_shoumei_detail_webview, null, false) as LinearLayout
        initWebView(headView.webView, themeUrl)
        vRecyclerView.recyclerView.addHeaderView(headView)
        vRecyclerView.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShouMeiDetailAdapter(this, mList)
        vRecyclerView.recyclerView.adapter = adapter
        vRecyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                getCommentList(true, currentPage.toString(), "", id ?: "", "1")
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                getCommentList(false, currentPage.toString(), lastTimeStamp ?: "", id ?: "", "1")
            }
        }

        etcontent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etcontent.text.isNullOrEmpty()) {
                    isSend = false
                    send.setTextColor(resources.getColor(R.color.grayText))
                } else {
                    send.setTextColor(resources.getColor(R.color.red))
                    isSend = true
                }
            }
        })

        etcontent.setOnTouchListener { _, event ->
            when (event.action) {
                KeyEvent.ACTION_DOWN -> {
                }
                KeyEvent.ACTION_UP -> {
                    replyPosition = -1
                    if (followType == "0") {
                        showDialog()
                        return@setOnTouchListener true
                    }
                }
                else -> {
                }
            }

            return@setOnTouchListener false
        }
        send.setOnTouchListener { _, event ->
            when (event.action) {
                KeyEvent.ACTION_DOWN -> {
                }
                KeyEvent.ACTION_UP -> {
                    if (isSend) {
                        if (replyPosition == -1) {
                            sendComment(id ?: "", etcontent.text.toString(), "0", 1)
                            historyMap.remove("main")
                        } else {
                            sendComment(id
                                    ?: "", etcontent.text.toString(), mList[replyPosition].idCompanyHomeThemeComment
                                    ?: "", 2)
                            historyMap.remove(mList[replyPosition].idCompanyHomeThemeComment
                                    ?: "")
                        }
                        etcontent.text = null
                    }
                }
                else -> {
                }
            }
            return@setOnTouchListener true
        }

        adapter.setOnSubBtnClickListener(object : ShouMeiDetailAdapter.OnSubBtnClickListener {
            override fun moreLongClick(position: Int) {
                //长按回复
            }

            override fun subCommentClick(position: Int) {
                //跳转评论详情
                ShouMeiCommentActivity.start(this@ShoumeiDetailActivity, mList[position].idCompanyHomeThemeComment
                        ?: "", mList[position].headImg
                        ?: "", mList[position].nickName
                        ?: "", mList[position].commentCreateTime
                        ?: "", mList[position].commentDesc
                        ?: "", id
                        ?: "", followType ?: "", blackType)
            }

            override fun moreClick(position: Int) {
                if (followType == "0") {
                    showDialog()
                    return
                }

                //回复或者删除 弹窗
                if (mList[position].myselfComment == "1") {
                    showReplyDialog(false, position)
                    return
                }
                showReplyDialog(true, position)
            }

        })

        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                if (replyPosition == -1) {
                    etcontent.setText(historyMap["main"])
                } else {
                    etcontent.setText(historyMap[mList[replyPosition].idCompanyHomeThemeComment
                            ?: ""])
                }
            }

            override fun keyBoardHide(height: Int) {
                if (replyPosition == -1) {
                    historyMap["main"] = etcontent.text.toString().trim()
                } else {
                    historyMap[mList[replyPosition].idCompanyHomeThemeComment
                            ?: ""] = etcontent.text.toString().trim()
                }
//                etcontent.text = null
            }
        })

        ObserverManager.getInstance().add(this)
        getCommentList(true, currentPage.toString(), "", id ?: "", "1")
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface")
    private fun initWebView(webView: SMediaWebView, url: String) {
        var mobile = Mobile()
        webView.loadUrl("$url?at=$followType")
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
        webView.setOnContentChangeListener(object : SMediaWebView.OnContentChangeListener {
            override fun onContentChange() {
                mobile.onGetWebContentHeight(webView)
            }
        })
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

    private inner class Mobile {
        fun onGetWebContentHeight(webView: SMediaWebView) {
            //重新调整webview高度
            webView.post {
                webView.measure(0, 0)
                val measuredHeight = webView.measuredHeight
                val layoutParams = webView.layoutParams
                layoutParams.height = measuredHeight
                webView.layoutParams = layoutParams
                var hl = headView.layoutParams
                hl.height = measuredHeight
                headView.layoutParams = hl
            }
        }
    }

    companion object {
        fun start(mContext: Context, followType: String, blackType: String, id: String, themeUrl: String, commentId: String) {
            mContext.startActivity(Intent(mContext, ShoumeiDetailActivity::class.java)
                    .putExtra("THEMEURL", themeUrl)
                    .putExtra("FOLLOWTYPE", followType)
                    .putExtra("BLACKTYPE", blackType)
                    .putExtra("baseId", id).putExtra("commentId", commentId))
        }
    }

    fun showReplyDialog(isSingle: Boolean, index: Int) {
        val dialog = ShouMeiCommentDialog(this)
        if (isSingle) {
            dialog.singleButton()
        }

        dialog.setOnItemClickListener(object : ShouMeiCommentDialog.OnItemClickListener {
            override fun cancel() {

            }

            override fun onDelete() {
                //删除
                showDeleteDialog(index)
            }

            override fun onReply() {
                //回复
                replyPosition = index
                showSoftInputFromWindow(etcontent)
            }

        })
        dialog.show()
    }

    private fun showDeleteDialog(position: Int) {
        val dialog = SmediaDialog(this)
        dialog.setTitle("确定要删除当前内容吗？")
        dialog.setDesc("删除后该评论下的所有回复也将被删除")
        dialog.OnClickListener = View.OnClickListener {
            //删除评论
            deleteComment(mList[position].idCompanyHomeThemeComment ?: "", position)
        }
        dialog.cancelOnClickListener = View.OnClickListener {

        }
        dialog.show()
    }

    private fun showDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("评论前请先关注此企业")
        dialog.singleButton()
        dialog.show()
    }

    private fun getCommentList(boolean: Boolean, currentPage: String, timeStamp: String, idCompanyHomeTheme: String, sortType: String) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["currentPage"] = currentPage
        params["showCount"] = "20"
        params["timeStamp"] = timeStamp
        params["idCompanyHomeTheme"] = idCompanyHomeTheme
        params["sortType"] = sortType
        ApiManager.get(0, this, params, Constant.SM_DETAIL_COMMENT, object : ApiManager.OnResult<BaseModel<CommentData>>() {
            override fun onSuccess(data: BaseModel<CommentData>) {
                vRecyclerView.isRefreshing = false
                isShowLoading = false
                if (data.success) {
                    if (boolean) {
                        totalComment = data.entity?.totalComment?.toInt()
                        mList.clear()
                        vRecyclerView.update(data.entity?.pageInfo?.data)
                    } else {
                        vRecyclerView.loadMore(data.entity?.pageInfo?.data)
                    }
                    lastTimeStamp = data.entity?.timeStamp
                    mList.addAll(data.entity?.pageInfo?.data ?: arrayListOf())
                    adapter.notifyDataSetChanged()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
                vRecyclerView.isRefreshing = false
            }

            override fun onCatch(data: BaseModel<CommentData>) {
            }

        })
    }


    private fun sendComment(idCompanyHomeTheme: String, commentDesc: String, replyId: String, type: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["replyId"] = replyId
        params["commentDesc"] = commentDesc
        params["idCompanyHomeTheme"] = idCompanyHomeTheme
        params["commentUserType"] = "2"
        ApiManager.post(this, params, Constant.SM_ADD_COMMENT, object : ApiManager.OnResult<BaseModel<CommentData.SubComment>>() {
            override fun onSuccess(data: BaseModel<CommentData.SubComment>) {
                isShowLoading = false
                if (data.success) {
                    totalComment = totalComment!! + 1
                    ObserverManager.getInstance().notifyObserver(1, id
                            ?: "", totalComment as Any, "")
                    if (type == 1) {
                        //发表评论
                        data.entity?.childResThemeCommentVoList = ArrayList()
                        mList.add(0, data.entity!!)
                        adapter.notifyDataSetChanged()
                        vRecyclerView.recyclerView.smoothScrollToPosition(1)
                    } else {
                        //回复
                        mList[replyPosition].childResThemeCommentVoList?.add(0, data.entity!!)
                        mList[replyPosition].numberOfReplies = (mList[replyPosition].numberOfReplies!!.toInt() + 1).toString()
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<CommentData.SubComment>) {
            }

        })
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    fun showSoftInputFromWindow(editText: EditText) {
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun deleteComment(idCompanyHomeThemeComment: String, index: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["idCompanyHomeThemeComment"] = idCompanyHomeThemeComment
        ApiManager.post(this, params, Constant.SM_DELETE_COMMENT, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                isShowLoading = false
                if (data.success) {
                    totalComment = totalComment!! - mList[index].numberOfReplies!!.toInt() - 1
                    ObserverManager.getInstance().notifyObserver(1, id
                            ?: "", totalComment as Any, "")
                    mList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    if (index != mList.size) {
                        adapter.notifyItemRangeChanged(index, mList.size - index);
                    }
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }

    @JavascriptInterface
    fun jsCancelOrConfirm() {
        val params = HashMap<String, String>()
        params["idCompanyHomeBaseInfo"] = commentId ?: ""
        if (followType == "0") {
            params["isFollow"] = "1"
        } else {
            params["isFollow"] = "0"
        }

        ApiManager.post(this, params, Constant.SM_FOLLOW_OR_CANCEL, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                isShowLoading = false
                if (data.success && data.code == 200) {
                    //调用html方法改变字段
                    followType = params["isFollow"]
                    headView.webView.loadUrl("javascript:atCallBack($followType)")
                    ObserverManager.getInstance().notifyObserver(3, commentId
                            ?: "", followType ?: "", "")
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }

    @JavascriptInterface
    fun jsJumpToTheme() {
        //品牌专属论坛
        ShouMeiBrandActivity.start(this, commentId ?: "")
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManager.getInstance().remove(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        followType = intent?.getStringExtra("FOLLOWTYPE")
        val blackType = intent?.getStringExtra("BLACKTYPE")
        id = intent?.getStringExtra("baseId")
        commentId = intent?.getStringExtra("commentId")
        val themeUrl = intent?.getStringExtra("THEMEURL")
        //禁言
        if (blackType == "1") {
            etcontent.isFocusable = false
            tvError.visibility = View.VISIBLE
        } else {
            etcontent.isFocusable = true
            tvError.visibility = View.GONE
        }
        initWebView(headView.webView, themeUrl?:"")
        getCommentList(true, currentPage.toString(), "", id ?: "", "1")
    }
}