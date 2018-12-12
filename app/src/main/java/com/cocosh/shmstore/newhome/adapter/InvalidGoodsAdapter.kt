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
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import kotlinx.android.synthetic.main.item_invalid_goods.view.*

class InvalidGoodsAdapter(list:ArrayList<ShoppingCarts.Shopping>): BaseRecycleAdapter<ShoppingCarts.Shopping>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        Glide.with(context)
                .load(getData(position).sku_image)
                .dontAnimate()
                .placeholder(ColorDrawable(ContextCompat.getColor(context,R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        holder.itemView.tvGoodsName.text = getData(position).goods_name

        val status = when(getData(position).status){
            "1" -> "该商品已下架"
            else -> "商品库存不足"
        }

        holder.itemView.txt_status.text = "失效"
        holder.itemView.txt_detail.text = status

        holder.itemView.setOnClickListener {
            if (getData(position).status == "1"){
                GoodsDetailActivity.start(context,getData(position).goods_name,getData(position).goods_id)
            }
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_invalid_goods,parent,false)
}