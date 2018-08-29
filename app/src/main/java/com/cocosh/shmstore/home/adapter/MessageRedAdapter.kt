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
        holder.itemView.tvTime.text = getData(position).time
        holder.itemView.tvTitle.text = getData(position).body?.name

        when (list[position].kind) {
            "1" -> {
                holder.itemView.tvStatus.text = "红包已投放"
                holder.itemView.tvDesc.visibility = View.VISIBLE
                holder.itemView.tvDesc.text = ("投放时间：" + getData(position).body?.actual_time)
            }

            "2" -> {
                holder.itemView.tvStatus.text = "红包被驳回"
                holder.itemView.tvDesc.visibility = View.VISIBLE
                holder.itemView.tvDesc.text = ("驳回原因：" + getData(position).body?.reason)
            }

            "3" -> {
                val desc = if (getData(position).body?.refund == 1) "（有退款）" else ""
                holder.itemView.tvStatus.text = ("投放结束$desc")
                holder.itemView.tvDesc.visibility = View.INVISIBLE
            }


            "4" -> {
                holder.itemView.tvStatus.text = "赠送红包被领取"
                holder.itemView.tvDesc.visibility = View.VISIBLE
                holder.itemView.tvDesc.text = ("您赠送的红包已被${getData(position).body?.nickname}（${getData(position).body?.smno}）领取！")
            }


            "5" -> {
                holder.itemView.tvStatus.text = "赠送红包退回"
                holder.itemView.tvTitle.text = "您赠送的红包被退回"
                holder.itemView.tvDesc.visibility = View.INVISIBLE
            }
        }

        holder.itemView.setOnClickListener {

        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_message_red, parent, false)

}