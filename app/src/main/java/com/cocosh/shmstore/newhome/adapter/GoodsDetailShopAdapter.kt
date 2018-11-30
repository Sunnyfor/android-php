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
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.model.Goods
import kotlinx.android.synthetic.main.layout_goods_detail_shop.view.*

class GoodsDetailShopAdapter(mList: ArrayList<Goods>) : BaseRecycleAdapter<Goods>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context).load(getData(position).image)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        holder.itemView.tvName.text = getData(position).name
        holder.itemView.tvMoney.text = ("¥" + getData(position).price)

        holder.itemView.setOnClickListener {
            GoodsDetailActivity.start(context, getData(position).name, getData(position).id)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_goods_detail_shop, parent, false)
}