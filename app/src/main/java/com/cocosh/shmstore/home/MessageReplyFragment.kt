package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.MessageReplyAdapter
import com.cocosh.shmstore.home.model.MsgModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_message_reply.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


/**
 * 消息 回复我的消息
 */
class MessageReplyFragment : BaseFragment() {
    val list = arrayListOf<MsgModel>()
    override fun setLayout(): Int = R.layout.fragment_message_reply

    override fun initView() {
        getLayoutView().recyclerView.recyclerView.layoutManager = LinearLayoutManager(activity)
        getLayoutView().recyclerView.recyclerView.adapter = MessageReplyAdapter(getBaseActivity(), list)

        getLayoutView().recyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                getMessage(0, true, "")
            }

            override fun onLoadMore(page: Int) {
                if (list.size > 0) {
                    getMessage(0, false, list[list.size - 1].messageId)
                }
            }
        }
    }

    override fun reTryGetData() {
        if (isInit) {
            getMessage(0, true, "")
        }
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }

    /**
     *   获取回复消息
     */
    fun getMessage(flag: Int, boolean: Boolean, messageId: String?) {
        getBaseActivity().isShowLoading = true
        val map = HashMap<String, String>()
        map["id"] = messageId ?: ""
        map["showCount"] = getLayoutView().recyclerView.pageCount.toString()
        ApiManager.get(flag, getBaseActivity(), map, Constant.MSG_REPLY, object : ApiManager.OnResult<BaseModel<ArrayList<MsgModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<MsgModel>>) {
                getBaseActivity().isShowLoading = false
                if (data.success && data.code == 200) {
                    if (boolean) {
                        list.clear()
                        getLayoutView().recyclerView.update(data.entity)
                    } else {
                        getLayoutView().recyclerView.loadMore(data.entity)
                    }
                    list.addAll(data.entity ?: arrayListOf())
                    getLayoutView().recyclerView.recyclerView.adapter.notifyDataSetChanged()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                getBaseActivity().isShowLoading = false
            }

            override fun onCatch(data: BaseModel<ArrayList<MsgModel>>) {

            }

        })
    }

    var isInit = false
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (!isInit) {
                isInit = true
                launch(UI) {
                    delay(500)
                    getMessage(0, true, "")
                }
                return
            }
            getBaseActivity().showLoading()
            getMessage(0, true, "")
        }
    }

}
