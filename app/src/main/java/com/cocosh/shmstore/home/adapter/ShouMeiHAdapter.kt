package com.cocosh.shmstore.home.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R

import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.ShouMeiBrandActivity
import com.cocosh.shmstore.home.model.SMCompanyData
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_shoumei_h.view.*

/**
 * 我的顶部导航适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiHAdapter(var type: Int, list: ArrayList<SMCompanyData>) : BaseRecycleAdapter<SMCompanyData>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (type == -1) {
            holder.itemView.tvFollow.visibility = View.GONE
        } else {
            holder.itemView.tvFollow.visibility = View.VISIBLE
        }

        if (list[position].follow == "0") {
            holder.itemView.tvFollow.text = "+关注"
            holder.itemView.tvFollow.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.itemView.tvFollow.setBackgroundResource(R.drawable.shape_rectangle_round_red)
        } else {
            holder.itemView.tvFollow.text = "已关注"
            holder.itemView.tvFollow.setTextColor(ContextCompat.getColor(context,R.color.grayText))
            holder.itemView.tvFollow.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
        }

        GlideUtils.loadRound(1, context, list[position].logo, holder.itemView.ivLogo)
        holder.itemView.tvName.text = list[position].company

        holder.itemView.tvFollow.setOnClickListener {
            mOnFollowClick?.follow(position)
        }

        holder.itemView.setOnClickListener {
            //品牌专属论坛
            var bbs: SMCompanyThemeData.BBS? = null
            list[position].apply {
                bbs = SMCompanyThemeData.BBS(id, eid, company, name, logo, desc, follow, follow_nums, silence)
            }
            bbs?.let {
                ShouMeiBrandActivity.start(context, it)
            }
        }
    }

    interface OnFollowClick {
        fun follow(data: Int)
    }

    var mOnFollowClick: OnFollowClick? = null

    fun setOnFollowClick(mOnFollowClick: OnFollowClick) {
        this.mOnFollowClick = mOnFollowClick
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_h, parent, false)

}