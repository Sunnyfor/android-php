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
import com.cocosh.shmstore.newhome.model.CollectionGoods
import kotlinx.android.synthetic.main.item_collect_goods.view.*

class CollectionGoodsAdapter(list: ArrayList<CollectionGoods>, var onDelete: (index: Int) -> Unit) : BaseRecycleAdapter<CollectionGoods>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context)
                .load(getData(position).url)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .dontAnimate()
                .into(holder.itemView.imgPhoto)
        holder.itemView.txt_goods_name.text = getData(position).name
        holder.itemView.txt_collect_count.text = (getData(position).favs + "人收藏")
        holder.itemView.txt_money.text = getData(position).price


        if (getData(position).sale == "1") {
            holder.itemView.txt_fail.visibility = View.GONE
            holder.itemView.ll_money.visibility = View.VISIBLE
        } else {
            holder.itemView.ll_money.visibility = View.GONE
            holder.itemView.txt_fail.visibility = View.VISIBLE
        }

        holder.itemView.ll_delete.setOnClickListener {
            onDelete(position)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_collect_goods, parent, false)
}