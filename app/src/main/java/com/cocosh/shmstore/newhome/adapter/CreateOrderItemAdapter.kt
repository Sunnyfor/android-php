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
import com.cocosh.shmstore.newhome.model.CreateGoodsBean
import kotlinx.android.synthetic.main.item_create_order_goods.view.*

class CreateOrderItemAdapter(list: ArrayList<CreateGoodsBean>) : BaseRecycleAdapter<CreateGoodsBean>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context)
                .load(getData(position).goods_img)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        holder.itemView.tvGoodsName.text = getData(position).goods_name
        holder.itemView.tvDesc.text = getData(position).sku_str
        holder.itemView.tvMoney.text = ("¥" + getData(position).price)
        holder.itemView.tvShowCount.text = ("x" + getData(position).num)
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_create_order_goods, parent, false)
}