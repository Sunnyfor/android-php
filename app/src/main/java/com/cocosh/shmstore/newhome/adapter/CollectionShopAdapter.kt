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
import com.cocosh.shmstore.newhome.model.CollectionShop
import kotlinx.android.synthetic.main.item_collect_shop.view.*

class CollectionShopAdapter(list: ArrayList<CollectionShop>, var onDelete: (index: Int) -> Unit) : BaseRecycleAdapter<CollectionShop>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context)
                .load(getData(position).logo)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .dontAnimate()
                .into(holder.itemView.imgPhoto)
        holder.itemView.txt_goods_name.text = getData(position).name


        holder.itemView.txt_cancel.setOnClickListener {
            onDelete(position)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_collect_shop, parent, false)
}