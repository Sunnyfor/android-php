package com.cocosh.shmstore.home

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiCommentAdapter
import com.cocosh.shmstore.home.model.CommentData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.SoftKeyBoardListener
import com.cocosh.shmstore.widget.dialog.ReportDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.activity_shoumei_comment.*
import kotlinx.android.synthetic.main.item_shoumei_comment_top.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


/**
 *
 * Created by lmg on 2018/6/4.
 */
class ShouMeiCommentActivity : BaseActivity() {
    //    var isPop = false
    var comment_id: String? = ""
    var currentPage = 1
    var replyId = -1
    var lastTimeStamp: String? = ""
    val historyMap = hashMapOf<String, String>()
    val mList = arrayListOf<CommentData.Portion>()
    lateinit var headView: LinearLayout
    private lateinit var adapter: ShouMeiCommentAdapter
    var isSend = false
    var followType: String? = ""
    override fun setLayout(): Int = R.layout.activity_shoumei_comment

    override fun initView() {
        comment_id = intent.getStringExtra("comment_id")
        val headUrl: String? = intent.getStringExtra("headUrl")
        val name = intent.getStringExtra("name")
        val time: String? = intent.getStringExtra("time")
        val desc: String? = intent.getStringExtra("desc")
        followType = intent.getStringExtra("followType")
        val silence: String? = intent.getStringExtra("silence")

        titleManager.rightText("评论", "举报", View.OnClickListener {
            val dialog = ReportDialog(this, comment_id ?: "", "1")
            dialog.show()
        }, false)

        //禁言
        if (silence == "1") {
            etcontent.isFocusable = false
            tvError.visibility = View.VISIBLE
        } else {
        etcontent.isFocusable = true
        tvError.visibility = View.GONE
        }

        headView = LayoutInflater.from(this).inflate(R.layout.item_shoumei_comment_top, all, false) as LinearLayout
        recyclerView.recyclerView.addHeaderView(headView)

        GlideUtils.loadRound(2, this, headUrl, headView.ivLogo)
        headView.tvTopName.text = name
        headView.tvTopTime.text = time
        headView.tvTopDesc.text = desc

        recyclerView.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.recyclerView.addItemDecoration(SpaceVItem(0, 0))
        adapter = ShouMeiCommentAdapter(this, comment_id ?: "", mList)
        recyclerView.recyclerView.adapter = adapter
        adapter.setOnSubBtnClickListener(object : ShouMeiCommentAdapter.OnSubBtnClickListener {
            override fun itemOnClick(position: Int) {
                if (followType == "0") {
                    showDialog()
                    return
                }
//                etcontent.hint = "回复 " + mList[position].nickName
                replyId = position
                showSoftInputFromWindow(etcontent)
            }

            override fun deleteClick(position: Int) {
                showDeleteDialog(position)
            }
        })
        //设置数据
        recyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                getCommentDetail()
            }

            override fun onLoadMore(page: Int) {
                mList.last().id?.let {
                    currentPage = it.toInt()
                }
                getCommentDetail()
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
                        sendRervet(etcontent.text.toString())
                        etcontent.text = null
                    }
                }
                else -> {
                }
            }
            return@setOnTouchListener true
        }

        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
//                isPop = true
                if (replyId == -1) {
                    etcontent.hint = getString(R.string.comment_def)
                } else {
                    etcontent.hint = "回复:" + mList[replyId].user?.nickname
                }

            }

            override fun keyBoardHide(height: Int) {
                launch(UI) {
                    delay(100)
                    etcontent.hint = getString(R.string.comment_def)
                    etcontent.text = null
                    replyId = -1
                }
            }
        })
        getCommentDetail()
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    private fun showDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("评论前请先关注此企业")
        dialog.singleButton()
        dialog.show()
    }


    companion object {
        fun start(mContext: Context, id: String, headUrl: String, name: String, time: String, desc: String, followType: String, silence: String) {
            mContext.startActivity(Intent(mContext, ShouMeiCommentActivity::class.java).putExtra("comment_id", id)
                    .putExtra("headUrl", headUrl)
                    .putExtra("name", name)
                    .putExtra("time", time)
                    .putExtra("desc", desc)
                    .putExtra("followType", followType)
                    .putExtra("silence", silence))
        }
    }


    fun showDeleteDialog(position: Int) {
        val dialog = SmediaDialog(this)
        dialog.setTitle("确定要删除当前内容吗？")
        dialog.setDesc("删除后该评论下的所有回复也将被删除")
        dialog.show()
        dialog.OnClickListener = View.OnClickListener {
            //删除评论
            deleteComment(mList[position].id ?: "", position)
        }
    }

    private fun deleteComment(idCompanyHomeThemeComment: String, index: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["reply_id"] = idCompanyHomeThemeComment
        ApiManager2.post(this, params, Constant.EHOME_REPLY_DELETE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<String>) {
                isShowLoading = false
                mList.removeAt(index)
                adapter.notifyItemRemoved(index)
                if (index != mList.size) {
                    adapter.notifyItemRangeChanged(index, mList.size - index)
                }
                ObserverManager.getInstance().notifyObserver(2, comment_id
                        ?: "", -1 as Any, mList as Any)
            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }

    private fun sendRervet(commentDesc: String) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["comment_id"] = comment_id.toString()
        if (replyId != -1) {
            params["parent"] = (mList[replyId].id ?: "0")
        }
        params["content"] = commentDesc
        ApiManager2.post(this, params, Constant.EHOME_REPLY_CREATE, object : ApiManager2.OnResult<BaseBean<CommentData.Portion>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<CommentData.Portion>) {
                isShowLoading = false
                data.message?.let {
                    mList.add(it)
                    adapter.notifyDataSetChanged()
                    //更改评论回复数
                    ObserverManager.getInstance().notifyObserver(2, comment_id
                            ?: "", 1 as Any, mList as Any)
                }

            }

            override fun onCatch(data: BaseBean<CommentData.Portion>) {
            }

        })
    }

    private fun getCommentDetail() {
        isShowLoading = true
        val params = HashMap<String, String>()

        params["comment_id"] = comment_id ?: ""
        if (currentPage != 1) {
            params["reply_id"] = currentPage.toString()
        }
        params["num"] = "20"
        ApiManager2.post(0, this, params, Constant.EHOME_REPLIES, object : ApiManager2.OnResult<BaseBean<ArrayList<CommentData.Portion>>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
                recyclerView.update(null)
                recyclerView.loadMore<CommentData.Portion>(null)
            }

            override fun onSuccess(data: BaseBean<ArrayList<CommentData.Portion>>) {
                isShowLoading = false
                recyclerView.isRefreshing = false
                if (currentPage == 1) {
//                        totalComment = data.message?.totalResult?.toInt() ?: 0
//                        ObserverManager.getInstance().notifyObserver(2, this@ShouMeiCommentActivity.comment_id
//                                ?: "", totalComment as Any, mList as Any)
                    mList.clear()
                    recyclerView.update(data.message)
                } else {
                    recyclerView.loadMore(data.message)
                }
                mList.addAll(data.message ?: arrayListOf())
                adapter.notifyDataSetChanged()
            }

            override fun onCatch(data: BaseBean<ArrayList<CommentData.Portion>>) {
            }

        })
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    fun showSoftInputFromWindow(editText: EditText) {
//        if (isPop) {
//            hideKeyboard(editText.windowToken)
//            etcontent.hint = "说说你的看法..."
//            etcontent.text = null
//            isPop = false
//        } else {
        editText.requestFocus()
        showKeyboard(editText)
//        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        comment_id = intent?.getStringExtra("comment_id")
        val headUrl: String? = intent?.getStringExtra("headUrl")
        val name: String? = intent?.getStringExtra("name")
        val time: String? = intent?.getStringExtra("time")
        val desc: String? = intent?.getStringExtra("desc")
        followType = intent?.getStringExtra("followType")
        val silence: String? = intent?.getStringExtra("silence")

        GlideUtils.loadRound(2, this, headUrl, headView.ivLogo)
        headView.tvTopName.text = name
        headView.tvTopTime.text = time
        headView.tvTopDesc.text = desc

        //禁言
        if (silence == "1") {
            etcontent.isFocusable = false
            tvError.visibility = View.VISIBLE
        } else {
            etcontent.isFocusable = true
            tvError.visibility = View.GONE
        }

        getCommentDetail()
    }
}