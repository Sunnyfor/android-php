package com.cocosh.shmstore.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.NewCollection
import kotlinx.android.synthetic.main.layout_bonus_list_item.view.*

/**
 * Created by lmg on 2018/4/12.
 * 收藏
 */
class CollectionAdapter(var mContext: Context, list: ArrayList<NewCollection>) : BaseRecycleAdapter<NewCollection>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).name

        holder.itemView.tvSure.setBackgroundResource(R.color.red)

        when (getData(position).status) {
            "0" -> {
                holder.itemView.tvSure.text = "未打开"
                holder.itemView.tvSure.setBackgroundResource(R.color.red)
            }
            "1" -> {
                holder.itemView.tvSure.setBackgroundResource(R.color.grayBtn)
                holder.itemView.tvSure.text = "已打开"
            }
            "2" -> {
                holder.itemView.tvSure.text = "已赠送"
                holder.itemView.tvSure.setBackgroundResource(R.color.grayBtn)
            }
        }
//        holder.itemView.number.text = "红包总金额:￥" + getData(position).redPacketMoney
        Glide.with(mContext).load(getData(position).image).placeholder(R.drawable.default_long).into(holder.itemView.ivPhoto)
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_bonus_list_item, parent, false)
}