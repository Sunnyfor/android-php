package com.cocosh.shmstore.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.CollectionModel
import com.cocosh.shmstore.mine.model.NewCollection
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_collection.view.*

/**
 * Created by lmg on 2018/4/12.
 * 收藏
 */
class CollectionAdapter(var mContext: Context, list: ArrayList<NewCollection>) : BaseRecycleAdapter<NewCollection>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.name.text = getData(position).name
        holder.itemView.status.text = getData(position).status
        holder.itemView.status.setBackgroundResource(R.color.red)
//        if (getData(position).status == "1") {
//            holder.itemView.status.text = "未打开"
//            holder.itemView.status.setBackgroundResource(R.color.red)
//        } else {
//            holder.itemView.status.setBackgroundResource(R.color.grayBtn)
//            holder.itemView.status.text = "已打开"
//        }
//
//        if (getData(position).status == "1") {
//            holder.itemView.status.text = "已赠送"
//            holder.itemView.status.setBackgroundResource(R.color.grayBtn)
//        }
//        holder.itemView.number.text = "红包总金额:￥" + getData(position).redPacketMoney
        GlideUtils.loadFullScreen(mContext, getData(position).image, holder.itemView.pic)
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
    }
}