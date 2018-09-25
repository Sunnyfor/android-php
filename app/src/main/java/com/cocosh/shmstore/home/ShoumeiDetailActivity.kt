package com.cocosh.shmstore.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.EditText
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiDetailAdapter
import com.cocosh.shmstore.home.model.CommentData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.FileUtlis
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.SMediaWebView
import com.cocosh.shmstore.widget.SoftKeyBoardListener
import com.cocosh.shmstore.widget.dialog.ShouMeiCommentDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.cocosh.shmstore.widget.observer.ObserverListener
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.activity_shoumei_detail.*
import kotlinx.android.synthetic.main.item_shoumei_detail_webview.view.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.net.URL


/**
 * 首媒之家 新闻详情页
 * Created by lmg on 2018/5/31.
 */
class ShoumeiDetailActivity : BaseActivity(), ObserverListener {
    var replyPosition = -1
    var currentPage = 1
    //    val historyMap = hashMapOf<String, String>()
    val mList = arrayListOf<CommentData>()
    var lastTimeStamp: String? = ""
    var isSend = false
    lateinit var headView: LinearLayout
    private lateinit var adapter: ShouMeiDetailAdapter
    var silence: String = "0"
    var job: Deferred<Uri>? = null
    override fun setLayout(): Int = R.layout.activity_shoumei_detail

    override fun observerUpData(type: Int, data: Any, content: Any, dataExtra: Any) {
        if (type == 2) {
            mList.let {
                val index = it.indexOfFirst {
                    it.id == data as String
                }
                if (index != -1) {
                    it[index].replies += content as Int
                    adapter.notifyItemRangeChanged(index, it.size - index)
                    ObserverManager.getInstance().notifyObserver(1, post_id
                            ?: "", content, "")
                }
            }
        }

        if (type == 3) {
            if (data as String == post_id) {
                followType = content as String
                headView.webView.loadUrl("javascript:atCallBack($followType)")
            }
        }
    }

    var eid: String? = ""
    var followType: String? = ""
    var post_id: String? = ""
    override fun initView() {
        post_id = intent.getStringExtra("post_id")
        followType = intent?.getStringExtra("followType")
        silence = intent?.getStringExtra("silence") ?: "0"
        eid = intent?.getStringExtra("eid")
        post_id = intent?.getStringExtra("post_id")
        val themeUrl = intent.getStringExtra("THEMEURL")
        //禁言
        if (silence == "1") {
            etcontent.isFocusable = false
            tvError.visibility = View.VISIBLE
        } else {
            etcontent.isFocusable = true
            tvError.visibility = View.GONE
        }

        titleManager.defaultTitle(intent.getStringExtra("title")).setLeftOnClickListener(View.OnClickListener {
            if (headView.webView.canGoBack()) {
                headView.webView.goBack()
            } else {
                finish()
            }
        })
        headView = LayoutInflater.from(this).inflate(R.layout.item_shoumei_detail_webview, null, false) as LinearLayout
        initWebView(headView.webView, themeUrl)
        vRecyclerView.recyclerView.addHeaderView(headView)
        vRecyclerView.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShouMeiDetailAdapter(this, mList)
        vRecyclerView.recyclerView.adapter = adapter
        vRecyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                getCommentList(true)
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                getCommentList(false)
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
                            sendComment(etcontent.text.toString())
                        } else {
                            sendRervet(mList[replyPosition], etcontent.text.toString())
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
                ShouMeiCommentActivity.start(this@ShoumeiDetailActivity, mList[position].id
                        , mList[position].user?.avatar ?: ""
                        , mList[position].user?.nickname ?: ""
                        , mList[position].time ?: ""
                        , mList[position].content ?: ""
                        , followType ?: "", silence)
            }

            override fun moreClick(position: Int) {
                if (followType == "0") {
                    showDialog()
                    return
                }

                //回复或者删除 弹窗
                if (mList[position].user?.smno == UserManager2.getLogin()?.code) {
                    showReplyDialog(false, position)
                    return
                }
                showReplyDialog(true, position)
            }

        })

        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) = if (replyPosition == -1) {
                etcontent.hint = getString(R.string.comment_def)
//                    etcontent.setText(historyMap["main"])
            } else {
//                    etcontent.setText(historyMap[mList[replyPosition].comment_id])
                etcontent.hint = "回复:" + mList[replyPosition].user?.nickname
            }

            override fun keyBoardHide(height: Int) {
//                if (replyPosition == -1) {
//                    historyMap["main"] = etcontent.text.toString().trim()
//                } else {
//                    historyMap[mList[replyPosition].comment_id] = etcontent.text.toString().trim()
//                }
                launch(UI) {
                    delay(100)
                    etcontent.hint = getString(R.string.comment_def)
                    replyPosition = -1
                    etcontent.text = null
                }

            }
        })

        ObserverManager.getInstance().add(this)
        getCommentList(true)
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface")
    private fun initWebView(webView: SMediaWebView, url: String) {
        val mobile = Mobile()
//        webView.loadUrl("$url?at=$followType")
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

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if (url.contains(".jpg")) {
                    showLoading()
                    val intent = Intent(Intent.ACTION_VIEW)
                    job = async(CommonPool) {

                        val httpUrl = URL(url)
                        val inputStream = httpUrl.openStream()

                        val file = FileUtlis().getFile("preview.jpg")
                        file.writeBytes(inputStream.readBytes())

                        val uri: Uri
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(this@ShoumeiDetailActivity, "${this@ShoumeiDetailActivity.packageName}.provider", file)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//注意加上这句话

                        } else {
                            uri = Uri.fromFile(file)
                            LogUtil.i("uri内容："+uri)
                        }
                        return@async uri

                    }
                    launch(UI) {
                        val uri = job?.await()
                        hideLoading()
                        intent.setDataAndType(uri, "image/*")
                        startActivity(intent)
                    }
                } else {
                    return super.shouldOverrideUrlLoading(view, url)
                }
                return true
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LogUtil.i("web:$url")
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
                val hl = headView.layoutParams
                hl.height = measuredHeight
                headView.layoutParams = hl
            }
        }
    }

    companion object {
        fun start(mContext: Context, title: String, themeUrl: String, eid: String, commentId: String, followType: String, silence: String) {
            mContext.startActivity(Intent(mContext, ShoumeiDetailActivity::class.java)
                    .putExtra("title", title)
                    .putExtra("THEMEURL", themeUrl)
                    .putExtra("post_id", commentId)
                    .putExtra("followType", followType)
                    .putExtra("eid", eid) //企业ID
                    .putExtra("silence", silence))  //是否禁言

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
            deleteComment(mList[position], position)
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

    private fun getCommentList(boolean: Boolean) {
        isShowLoading = true
        val params = HashMap<String, String>()

        params["post_id"] = post_id.toString()
        params["num"] = "20"
        if (currentPage > 1) {
            params["comment_id"] = currentPage.toString()
        }
        ApiManager2.post(0, this, params, Constant.EHOME_COMMENTS, object : ApiManager2.OnResult<BaseBean<ArrayList<CommentData>>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
                vRecyclerView.isRefreshing = false
            }

            override fun onSuccess(data: BaseBean<ArrayList<CommentData>>) {
                vRecyclerView.isRefreshing = false
                isShowLoading = false
                if (boolean) {
//                        totalComment = data.message?.?.toInt()
                    mList.clear()
                    vRecyclerView.update(data.message)
                } else {
                    vRecyclerView.loadMore(data.message)
                }
                mList.addAll(data.message ?: arrayListOf())
                adapter.notifyDataSetChanged()
            }


            override fun onCatch(data: BaseBean<ArrayList<CommentData>>) {
            }

        })
    }


    private fun sendRervet(comment: CommentData, content: String) {
        isShowLoading = true

        val params = HashMap<String, String>()
        params["comment_id"] = comment.id
        params["content"] = content

        ApiManager2.post(this, params, Constant.EHOME_REPLY_CREATE, object : ApiManager2.OnResult<BaseBean<CommentData.Portion>>() {
            override fun onSuccess(data: BaseBean<CommentData.Portion>) {
                //回复
                data.message?.let {
                    comment.replies++
                    if (comment.portion?.size ?: 0 < 2) {
                        comment.portion?.add(it)
                    }
                    ObserverManager.getInstance().notifyObserver(1, post_id
                            ?: "", 1 as Any, "")

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseBean<CommentData.Portion>) {

            }


        })
    }


    //发送评论
    private fun sendComment(commentDesc: String) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["post_id"] = post_id.toString()
        params["content"] = commentDesc
        ApiManager2.post(this, params, Constant.EHOME_COMMENT_CREATE, object : ApiManager2.OnResult<BaseBean<CommentData>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<CommentData>) {
                isShowLoading = false
                data.message?.let {
                    it.replies += 1
                    ObserverManager.getInstance().notifyObserver(1, post_id
                            ?: "", 1 as Any, "")

                    mList.add(0, it)
                    adapter.notifyDataSetChanged()
                    vRecyclerView.recyclerView.smoothScrollToPosition(1)
                }
            }


            override fun onCatch(data: BaseBean<CommentData>) {
            }

        })
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    fun showSoftInputFromWindow(editText: EditText) {
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun deleteComment(commentData: CommentData, index: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["comment_id"] = commentData.id
        ApiManager2.post(this, params, Constant.EHOME_COMMENT_DELETE, object : ApiManager2.OnResult<BaseBean<String>>() {

            override fun onSuccess(data: BaseBean<String>) {
                isShowLoading = false
                ObserverManager.getInstance().notifyObserver(1, post_id
                        ?: "", -1 as Any, "")
                mList.removeAt(index)
                adapter.notifyItemRemoved(index)
                if (index != mList.size) {
                    adapter.notifyItemRangeChanged(index, mList.size - index)
                }
            }

            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }

    @JavascriptInterface
    fun jsCancelOrConfirm() {
        val params = HashMap<String, String>()
        params["eid"] = eid ?: ""
        if (followType == "1") {
            params["op"] = "cancel"
        } else {
            params["op"] = "follow"
        }

        ApiManager2.post(this, params, Constant.EHOME_FOLLOW_OPERATE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<String>) {
                isShowLoading = false
                //调用html方法改变字段
                followType = if (params["op"] == "follow") "1" else "0"
                headView.webView.loadUrl("javascript:atCallBack($followType)")
                ObserverManager.getInstance().notifyObserver(3, eid
                        ?: "", followType ?: "", "")
            }


            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }

    @JavascriptInterface
    fun jsJumpToTheme() {
        //品牌专属论坛
//        mList.find { it.id == post_id }.
//        ShouMeiBrandActivity.start(this, post_id ?: "")
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManager.getInstance().remove(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        followType = intent?.getStringExtra("followType")
        silence = intent?.getStringExtra("silence") ?: "0"
        eid = intent?.getStringExtra("eid")
        post_id = intent?.getStringExtra("post_id")
        val themeUrl = intent?.getStringExtra("THEMEURL")
        //禁言
        if (silence == "1") {
            etcontent.isFocusable = false
            tvError.visibility = View.VISIBLE
        } else {
            etcontent.isFocusable = true
            tvError.visibility = View.GONE
        }
        initWebView(headView.webView, themeUrl ?: "")
        getCommentList(true)
    }

    //拦截返回键
    override fun onBackPressed() {
        if (headView.webView.canGoBack()) {
            headView.webView.goBack()
        } else {
            finish()
        }
    }
}