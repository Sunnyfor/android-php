package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.adapter.MessageOrderAdapter
import kotlinx.android.synthetic.main.fragment_message_order.view.*


/**
 * 消息 订单消息
 */
class MessageOrderFragment : BaseFragment() {

    override fun setLayout(): Int = R.layout.fragment_message_order

    override fun initView() {
        val list = arrayListOf<String>()
        list.add("1")
        list.add("1")
        list.add("1")
        list.add("1")
        list.add("1")
        list.add("1")
        list.add("1")
        list.add("1")
        getLayoutView().recyclerView.layoutManager = LinearLayoutManager(activity)
        getLayoutView().recyclerView.adapter = MessageOrderAdapter(list)
    }

    override fun reTryGetData() {

    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }


}
