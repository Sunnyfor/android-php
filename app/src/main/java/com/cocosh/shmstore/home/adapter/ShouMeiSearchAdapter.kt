package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.SMCompanyData
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_shoumei_serach.view.*


/**
 * 首媒之家 评论页适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiSearchAdapter(var mContext: Context, list: ArrayList<SMCompanyData>) : BaseRecycleAdapter<SMCompanyData>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(mContext).load(list[position].logo).placeholder(R.drawable.default_content).into(holder.itemView.ivLogo)

        holder.itemView.tvName.text = list[position].name
        if (list[position].follow == "0") {
            holder.itemView.tvFollow.text = "+关注"
            holder.itemView.tvFollow.setTextColor(Color.WHITE)
            holder.itemView.tvFollow.setBackgroundResource(R.drawable.shape_rectangle_round_red)
        } else {
            holder.itemView.tvFollow.text = "已关注"
            holder.itemView.tvFollow.setTextColor(ContextCompat.getColor(mContext,R.color.blackText))
            holder.itemView.tvFollow.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
        }
        holder.itemView.tvFollow.setOnClickListener {
            mOnSubBtnClickListener?.followClick(position)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_serach, parent, false)

    interface OnSubBtnClickListener {
        fun followClick(position: Int)
    }

    private var mOnSubBtnClickListener: OnSubBtnClickListener? = null

    fun setOnSubBtnClickListener(mOnSubBtnClickListener: OnSubBtnClickListener?) {
        this.mOnSubBtnClickListener = mOnSubBtnClickListener
    }
}