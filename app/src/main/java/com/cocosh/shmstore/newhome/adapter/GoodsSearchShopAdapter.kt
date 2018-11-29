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
import com.cocosh.shmstore.newhome.model.Shop
import kotlinx.android.synthetic.main.layout_shop_search_item.view.*

class GoodsSearchShopAdapter(mList: ArrayList<Shop>) : BaseRecycleAdapter<Shop>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        val moneyList = arrayListOf(
                holder.itemView.tvMoney1,
                holder.itemView.tvMoney2,
                holder.itemView.tvMoney3
        )

        val photoList = arrayListOf(
                holder.itemView.ivPhoto1,
                holder.itemView.ivPhoto2,
                holder.itemView.ivPhoto3
        )

        val llGoods = arrayListOf(
                holder.itemView.llGoods1,
                holder.itemView.llGoods2,
                holder.itemView.llGoods3
        )

        Glide.with(context)
                .load(getData(position).logo)
                .dontAnimate()
                .centerCrop()
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        holder.itemView.tvName.text = getData(position).name
        holder.itemView.tvDesc.text = (getData(position).total + "件在售商品")

        if (getData(position).attention == "1") {
            holder.itemView.btnFollow.setBackgroundResource(R.mipmap.ic_shop_cancel_follow)
        } else {
            holder.itemView.btnFollow.setBackgroundResource(R.mipmap.ic_shop_follow)
        }


        getData(position).goods?.forEachIndexed { index, goods ->
            llGoods[index].visibility = View.VISIBLE
            moneyList[index].text = ("¥"+goods.price)
            Glide.with(context)
                    .load(goods.image)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                    .into(photoList[index])
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_shop_search_item, parent, false)
}