package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.CommentData
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_shoumei_comment.view.*
import kotlinx.android.synthetic.main.item_shoumei_comment_top.view.*


/**
 * 首媒之家 评论页适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiCommentAdapter(var mContext: Context, var id: String, list: ArrayList<CommentData.SubComment>) : BaseRecycleAdapter<CommentData.SubComment>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvTime.text = list[position].commentCreateTime
        if (list[position].replayNickName.isNullOrEmpty()) {
            holder.itemView.tvDesc.text = Html.fromHtml("<font color='#2587ec'>${list[position].nickName}:</font>" + list[position].commentDesc)
        } else {
            holder.itemView.tvDesc.text = Html.fromHtml("<font color='#2587ec'>${list[position].nickName}</font> 回复 " + list[position].replayNickName + ":" + list[position].commentDesc)
        }

        if (list[position]?.myselfComment == "1") {
            holder.itemView.tvDelete.visibility = View.VISIBLE
        } else {
            holder.itemView.tvDelete.visibility = View.GONE
        }

        holder.itemView.tvDelete.setOnClickListener {
            mOnSubBtnClickListener?.deleteClick(position)
        }
        holder.itemView.setOnClickListener {
            mOnSubBtnClickListener?.itemOnClick(position)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_comment, parent, false)
    }

    interface OnSubBtnClickListener {
        fun deleteClick(position: Int)
        fun itemOnClick(position: Int)
    }

    private var mOnSubBtnClickListener: OnSubBtnClickListener? = null

    fun setOnSubBtnClickListener(mOnSubBtnClickListener: OnSubBtnClickListener?) {
        this.mOnSubBtnClickListener = mOnSubBtnClickListener
    }
}