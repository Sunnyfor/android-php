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
import com.cocosh.shmstore.mine.model.Order
import kotlinx.android.synthetic.main.item_goods_comment.view.*

class GoodsCommentAdapter(list: ArrayList<Order.Goods>) : BaseRecycleAdapter<Order.Goods>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context)
                .load(getData(position).image)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.imgPhoto)


        val desc = when (getData(position).ratingNum) {
            1 -> "非常差"
            2 -> "差"
            3 -> "一般"
            4 -> "好"
            5 -> "非常好"
            else -> "非常好"
        }
        holder.itemView.txt_detail.text = desc

        holder.itemView.txt_name.text = getData(position).goods_name

        holder.itemView.ratingBar.setOnRatingChangeListener {
            getData(position).ratingNum = it.toInt()
            notifyDataSetChanged()
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_goods_comment, parent, false)
        view.ratingBar.setStar(5f)
        return view
    }
}