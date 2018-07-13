package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.MessageType
import com.cocosh.shmstore.home.model.MsgModel
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_message_reply.view.*

/**
 * 回复消息适配器
 * Created by zhangye on 2018/3/13.
 */
class MessageReplyAdapter(var mContext: Context, list: ArrayList<MsgModel>) : BaseRecycleAdapter<MsgModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvTime.text = getData(position).createDate
        GlideUtils.loadHead(mContext, getData(position).replyPersonHeadPic, holder.itemView.head)
        if (getData(position).messageType == MessageType.REPLY_MESSAGE.type) {
            holder.itemView.tvName.text = getData(position).replyPersonName + " 回复 你"
            holder.itemView.tvDesc.text = getData(position).details
        } else {
            holder.itemView.tvName.text = getData(position).replyPersonName
            holder.itemView.tvDesc.text = "评论内容：" + getData(position).details
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_message_reply, parent, false)

}