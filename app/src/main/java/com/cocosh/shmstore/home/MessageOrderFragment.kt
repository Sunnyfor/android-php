package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.adapter.MessageOrderAdapter
import com.cocosh.shmstore.home.model.MsgModel
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_message_order.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


/**
 * 消息 订单消息
 */
class MessageOrderFragment : BaseFragment() {
    private var index = 1
    val list = arrayListOf<MsgModel>()

    override fun setLayout(): Int = R.layout.fragment_message_order

    override fun initView() {
        getLayoutView().recyclerView.recyclerView.layoutManager = LinearLayoutManager(activity)
        getLayoutView().recyclerView.recyclerView.adapter = MessageOrderAdapter(list)

        getLayoutView().recyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                index = page
                getMessage()
            }

            override fun onLoadMore(page: Int) {
                if (list.size > 0) {
                    index = list.last().id
                    getMessage()
                }
            }
        }
    }


    fun getMessage() {
        val map = HashMap<String, String>()
        map["channel"] = "o"
        if (index != 1){
            map["id"] = index.toString()
        }
        map["num"] = getLayoutView().recyclerView.pageCount.toString()

        ApiManager2.post(0, getBaseActivity(), map, Constant.LETTER_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<MsgModel>>>() {
            override fun onFailed(code: String, message: String) {
                getBaseActivity().isShowLoading = false
                getLayoutView().recyclerView.isRefreshing = false
                getLayoutView().recyclerView.recyclerView.loadMoreError(code.toInt(),message)
            }

            override fun onSuccess(data: BaseBean<ArrayList<MsgModel>>) {
                getBaseActivity().isShowLoading = false
                if (index == 1) {
                    list.clear()
                    getLayoutView().recyclerView.update(data.message)
                } else {
                    getLayoutView().recyclerView.loadMore(data.message)
                }
                list.addAll(data.message ?: arrayListOf())
                getLayoutView().recyclerView.recyclerView.adapter.notifyDataSetChanged()
            }


            override fun onCatch(data: BaseBean<ArrayList<MsgModel>>) {

            }

        })
    }

    override fun reTryGetData() {
        if (isInit) {
            index = 1
            getMessage()
        }
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }


    var isInit = false
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            index = 1
            if (!isInit) {
                isInit = true
                launch(UI) {
                    delay(500)
                    getMessage()
                }
                return
            }
            getBaseActivity().showLoading()
            getMessage()
        }
    }
}
