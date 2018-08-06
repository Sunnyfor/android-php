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
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiCommentAdapter
import com.cocosh.shmstore.home.model.CommentData
import com.cocosh.shmstore.home.model.SmCommentDatail
import com.cocosh.shmstore.http.ApiManager
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


/**
 * Created by lmg on 2018/6/4.
 */
class ShouMeiCommentActivity : BaseActivity() {
    var isPop = false
    var id: String? = ""
    var totalComment: Int = 0
    var currentPage = 1
    var replyId = -1
    var lastTimeStamp: String? = ""
    val historyMap = hashMapOf<String, String>()
    val mList = arrayListOf<CommentData.SubComment>()
    lateinit var headView: LinearLayout
    private lateinit var adapter: ShouMeiCommentAdapter
    var isSend = false
    var themeId: String? = ""
    var followType: String? = ""
    override fun setLayout(): Int = R.layout.activity_shoumei_comment

    override fun initView() {
        id = intent.getStringExtra("themeId")
        themeId = intent.getStringExtra("id")
        var headUrl: String? = intent.getStringExtra("headUrl")
        var name: String? = intent.getStringExtra("name")
        var time: String? = intent.getStringExtra("time")
        var desc: String? = intent.getStringExtra("desc")
        followType = intent.getStringExtra("followType")
        var blackType: String? = intent.getStringExtra("blackType")

        titleManager.rightText("评论", "举报", View.OnClickListener {
            val dialog = ReportDialog(this, id ?: "", "2")
            dialog.show()
        }, false)

        //禁言
        if (blackType == "1") {
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
        adapter = ShouMeiCommentAdapter(this, id ?: "", mList)
        recyclerView.recyclerView.adapter = adapter
        adapter.setOnSubBtnClickListener(object : ShouMeiCommentAdapter.OnSubBtnClickListener {
            override fun itemOnClick(position: Int) {
                if (followType == "0") {
                    showDialog()
                    return
                }

                etcontent.hint = "回复 " + mList[position].nickName
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
                getCommentDetail(true, currentPage, lastTimeStamp ?: "", id ?: "")
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                getCommentDetail(false, currentPage, lastTimeStamp ?: "", id ?: "")
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
                    replyId = -1
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
                        if (replyId == -1) {
                            sendComment(themeId ?: "", etcontent.text.toString(), id ?: "")
                            historyMap.remove(id ?: "")
                        } else {
                            sendComment(themeId
                                    ?: "", etcontent.text.toString(), mList[replyId].idCompanyHomeThemeComment
                                    ?: "")
                            historyMap.remove(mList[replyId].idCompanyHomeThemeComment
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

        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                isPop = true
                if (replyId == -1) {
                    etcontent.setText(historyMap[id ?: ""])
                } else {
                    etcontent.setText(historyMap[mList[replyId].idCompanyHomeThemeComment ?: ""])
                }
            }

            override fun keyBoardHide(height: Int) {
                if (replyId == -1) {
                    historyMap.put(id ?: "", etcontent.text.toString().trim())
                } else {
                    historyMap.put(mList[replyId].idCompanyHomeThemeComment
                            ?: "", etcontent.text.toString().trim())
                }
                etcontent.hint = "说说你的看法..."
            }
        })
        getCommentDetail(true, currentPage, lastTimeStamp ?: "", id ?: "")
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
        fun start(mContext: Context, id: String, headUrl: String, name: String, time: String, desc: String, themeId: String, followType: String, blackType: String) {
            mContext.startActivity(Intent(mContext, ShouMeiCommentActivity::class.java).putExtra("themeId", id)
                    .putExtra("headUrl", headUrl)
                    .putExtra("name", name)
                    .putExtra("time", time)
                    .putExtra("desc", desc)
                    .putExtra("id", themeId)
                    .putExtra("followType", followType)
                    .putExtra("blackType", blackType))
        }
    }


    fun showDeleteDialog(position: Int) {
        val dialog = SmediaDialog(this)
        dialog.setTitle("确定要删除当前内容吗？")
        dialog.setDesc("删除后该评论下的所有回复也将被删除")
        dialog.show()
        dialog.OnClickListener = View.OnClickListener {
            //删除评论
            deleteComment(mList[position].idCompanyHomeThemeComment ?: "", position)
        }
    }

    private fun deleteComment(idCompanyHomeThemeComment: String, index: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["idCompanyHomeThemeComment"] = idCompanyHomeThemeComment
        ApiManager.post(this, params, Constant.SM_DELETE_COMMENT, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                isShowLoading = false
                if (data.success) {
                    mList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    if (index != mList.size) {
                        adapter.notifyItemRangeChanged(index, mList.size - index);
                    }

                    //更改评论回复数
                    totalComment -= 1
                    ObserverManager.getInstance().notifyObserver(2, id
                            ?: "", totalComment as Any, mList as Any)
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

    private fun sendComment(idCompanyHomeTheme: String, commentDesc: String, replyId: String) {
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
                    mList.add(data.entity!!)
                    adapter.notifyDataSetChanged()
                    //更改评论回复数
                    totalComment += 1
                    ObserverManager.getInstance().notifyObserver(2, id
                            ?: "", totalComment as Any, mList as Any)
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

    private fun getCommentDetail(boolean: Boolean, currentPage: Int, timeStamp: String, idCompanyHomeThemeComment: String) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["currentPage"] = currentPage.toString()
        params["showCount"] = 20.toString()
        params["timeStamp"] = timeStamp
        params["idCompanyHomeThemeComment"] = idCompanyHomeThemeComment
        ApiManager.get(0, this, params, Constant.SM_COMMONT_DETAIL, object : ApiManager.OnResult<BaseModel<SmCommentDatail>>() {
            override fun onSuccess(data: BaseModel<SmCommentDatail>) {
                isShowLoading = false
                recyclerView.isRefreshing = false
                if (data.success) {
                    if (boolean) {
                        totalComment = data.entity?.totalResult?.toInt() ?: 0
                        ObserverManager.getInstance().notifyObserver(2, id
                                ?: "", totalComment as Any, mList as Any)
                        mList.clear()
                        recyclerView.update(data.entity?.data)
                    } else {
                        recyclerView.loadMore(data.entity?.data)
                    }
                    lastTimeStamp = data.entity?.timeStamp
                    mList.addAll(data.entity?.data
                            ?: arrayListOf())
                    adapter.notifyDataSetChanged()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                recyclerView.isRefreshing = false
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<SmCommentDatail>) {
            }

        })
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    fun showSoftInputFromWindow(editText: EditText) {
        if (isPop) {
            hideKeyboard(editText.windowToken)
            etcontent.hint = "说说你的看法..."
            etcontent.text = null
            isPop = false
        } else {
            editText.requestFocus()
            showKeyboard(editText)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        id = intent?.getStringExtra("themeId")
        themeId = intent?.getStringExtra("id")
        var headUrl: String? = intent?.getStringExtra("headUrl")
        var name: String? = intent?.getStringExtra("name")
        var time: String? = intent?.getStringExtra("time")
        var desc: String? = intent?.getStringExtra("desc")
        followType = intent?.getStringExtra("followType")
        var blackType: String? = intent?.getStringExtra("blackType")

        GlideUtils.loadRound(2, this, headUrl, headView.ivLogo)
        headView.tvTopName.text = name
        headView.tvTopTime.text = time
        headView.tvTopDesc.text = desc

        //禁言
        if (blackType == "1") {
            etcontent.isFocusable = false
            tvError.visibility = View.VISIBLE
        } else {
            etcontent.isFocusable = true
            tvError.visibility = View.GONE
        }

        getCommentDetail(true, currentPage, lastTimeStamp ?: "", id ?: "")
    }
}