package com.cocosh.shmstore.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.FollowListModel
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_concern.view.*

/**
 * Created by lmg on 2018/4/12.
 * 关注 adapter 0未关注 1已关注
 */
class ConcernAdapter(var mContext: Context, list: ArrayList<FollowListModel.Follow>) : BaseRecycleAdapter<FollowListModel.Follow>(list) {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        GlideUtils.loadRound(2, mContext, getData(position).companyLogo, holder.itemView.ivLogo)
        holder.itemView.tvName.text = getData(position).companyName
        holder.itemView.number.text = "关注粉丝人数：" + getData(position).followCount + "人"
        holder.itemView.tvFollow.setOnClickListener {
            mOnItemClickListener?.onItemClick(position)
        }
    }


    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_concern, parent, false)
}