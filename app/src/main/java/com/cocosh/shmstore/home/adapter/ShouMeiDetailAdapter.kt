package com.cocosh.shmstore.home.adapter

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.item_shoumei_detail.view.*


/**
 * 我的顶部导航适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiDetailAdapter(var mContext: Context, list: ArrayList<CommentData>) : BaseRecycleAdapter<CommentData>(list) {
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        GlideUtils.loadRound(2,mContext, list[position].user.avatar, holder.itemView.ivLogo)
        holder.itemView.tvName.text = list[position].user.nickname
        holder.itemView.tvTime.text = list[position].time
        holder.itemView.tvDesc.text = list[position].content

//        //评论回复
        if (list[position].portion.size > 0) {
            holder.itemView.subLL.visibility = View.VISIBLE
            if (list[position].portion.size == 1) {
                holder.itemView.tvOneTip.visibility = View.VISIBLE
                holder.itemView.tvTwoTip.visibility = View.GONE
                holder.itemView.tvNumber.visibility = View.GONE
                if (list[position].portion[0].parent_user?.nickname.isNullOrEmpty()) {
                    holder.itemView.tvOneTip.text = Html.fromHtml("<font color='#2587ec'>${list[position].portion[0].user?.nickname} :</font>" + list[position].portion[0].content)
                } else {
                    holder.itemView.tvOneTip.text = Html.fromHtml("<font color='#2587ec'>${list[position].portion[0].user?.nickname}:</font>" + " 回复 " +
                            "<font color='#2587ec'>"+list[position].portion[0].parent_user?.nickname + "</font'>:" +
                            list[position].portion[0].content)
                }

            }
            if (list[position].portion.size >= 2) {
                holder.itemView.tvOneTip.visibility = View.VISIBLE
                holder.itemView.tvTwoTip.visibility = View.VISIBLE
                if (list[position].portion[0].parent_user?.nickname.isNullOrEmpty()) {
                    holder.itemView.tvOneTip.text = Html.fromHtml("<font color='#2587ec'>${list[position].portion[0].user?.nickname}:</font>" + list[position].portion[0].content)
                } else {
                    holder.itemView.tvOneTip.text = Html.fromHtml("<font color='#2587ec'>${list[position].portion[0].user?.nickname}:</font>" + " 回复 " +
                            "<font color='#2587ec'>"+list[position].portion[0].parent_user?.nickname + "</font'>:" +
                            list[position].portion[0].content)
                }

                if (list[position].portion[1].parent_user?.nickname.isNullOrEmpty()) {
                    holder.itemView.tvTwoTip.text = Html.fromHtml("<font color='#2587ec'>${list[position].portion[1].user?.nickname}:</font>" + list[position].portion[1].content)
                } else {
                    holder.itemView.tvTwoTip.text = Html.fromHtml("<font color='#2587ec'>${list[position].portion[1].user?.nickname}:</font>" + " 回复 " +
                            "<font color='#2587ec'>"+list[position].portion[1].parent_user?.nickname + "</font'>:" +
                            list[position].portion[1].content)
                }
                if (list[position].replies.toInt() > 2) {
                    holder.itemView.tvNumber.visibility = View.VISIBLE
                    holder.itemView.tvNumber.text = Html.fromHtml("<font color='#2587ec'>查看更多评论(${list[position].replies})</font>")
                } else {
                    holder.itemView.tvNumber.visibility = View.GONE
                }
            }
        } else {
            holder.itemView.subLL.visibility = View.GONE
        }


        holder.itemView?.tvMore?.setOnClickListener {
            mOnSubBtnClickListener?.moreClick(position)
        }

        holder.itemView?.subLL?.setOnClickListener {
            mOnSubBtnClickListener?.subCommentClick(position)
        }
        holder.itemView?.tvDesc?.setOnClickListener {
            mOnSubBtnClickListener?.subCommentClick(position)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_detail, parent, false)
    }

    interface OnSubBtnClickListener {
        fun moreClick(position: Int)
        fun moreLongClick(position: Int)
        fun subCommentClick(position: Int)
    }

    private var mOnSubBtnClickListener: OnSubBtnClickListener? = null

    fun setOnSubBtnClickListener(mOnSubBtnClickListener: OnSubBtnClickListener?) {
        this.mOnSubBtnClickListener = mOnSubBtnClickListener
    }
}