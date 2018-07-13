package com.cocosh.shmstore.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.MessageType
import com.cocosh.shmstore.home.model.MsgModel
import kotlinx.android.synthetic.main.item_message_red.view.*

/**
 * 红包消息适配器
 * Created by zhangye on 2018/3/13.
 */
class MessageRedAdapter(list: ArrayList<MsgModel>) : BaseRecycleAdapter<MsgModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvTime.text = getData(position).createDate
        holder.itemView.tvStatus.text = getData(position).title
        holder.itemView.tvTitle.text = getData(position).details
        when (list[position].messageType) {
            MessageType.RED_PACKET_IN_THE_LAUNCH_MESSAGE.type -> {
                holder.itemView.tvDesc.visibility = View.VISIBLE
                holder.itemView.tvDesc.text = "投放时间：" + getData(position).redPacketStartTime
            }
            MessageType.RED_PACKET_PUT_TO_THE_END_MESSAGE.type -> {
                holder.itemView.tvDesc.visibility = View.INVISIBLE
            }
            MessageType.RED_PACKET_PUT_TO_THE_END_RETURN_MONEY_MESSAGE.type -> {
                holder.itemView.tvDesc.visibility = View.INVISIBLE
            }
            MessageType.RED_PACKET_GRANT_AND_RECEIVE_MESSAGE.type -> {
                holder.itemView.tvDesc.visibility = View.INVISIBLE
            }
            MessageType.RED_PACKET_GIVE_BACK_MESSAGE.type -> {
                holder.itemView.tvDesc.visibility = View.INVISIBLE
            }
            MessageType.RED_PACKET_REJECT_MESSAGE.type -> {
                holder.itemView.tvDesc.visibility = View.VISIBLE
                holder.itemView.tvDesc.text = "驳回原因：" + getData(position).causeWhy
            }
        }

        holder.itemView.setOnClickListener {

        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_message_red, parent, false)

}