package com.cocosh.shmstore.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.MsgModel
import kotlinx.android.synthetic.main.item_shoumei_h.view.*

/**
 * 红包消息适配器
 * Created by zhangye on 2018/3/13.
 */
class MessageOrderAdapter(list: ArrayList<MsgModel>) : BaseRecycleAdapter<MsgModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_message_order, parent, false)

}