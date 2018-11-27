package com.cocosh.shmstore.newhome.adapter

import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.model.Goods
import kotlinx.android.synthetic.main.layout_goods_item.view.*

class GoodsListAdapter(mList:ArrayList<Goods>):BaseRecycleAdapter<Goods>(mList) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).name
        holder.itemView.tvMoney.text = getData(position).price
        holder.itemView.tvDesc.text = (getData(position).comments + "条评价")

        Glide.with(context)
                .load(getData(position).image)
                .placeholder(ColorDrawable(ContextCompat.getColor(context,R.color.activity_bg)))
                .into( holder.itemView.ivPhoto)
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_goods_item,parent,false)
}