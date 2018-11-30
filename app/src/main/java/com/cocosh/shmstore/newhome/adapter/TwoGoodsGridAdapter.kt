package com.cocosh.shmstore.newhome.adapter

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.GoodsListActivity
import com.cocosh.shmstore.newhome.model.GoodsClazz
import kotlinx.android.synthetic.main.layout_two_goods_body_item.view.*

class TwoGoodsGridAdapter(mList: ArrayList<GoodsClazz.Bean>) : BaseRecycleAdapter<GoodsClazz.Bean>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvDesc.text = getData(position).name
        Glide.with(context)
                .load(getData(position).logo)
                .placeholder(ColorDrawable(ContextCompat.getColor(context,R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        holder.itemView.setOnClickListener {
            GoodsListActivity.start(context,getData(position).id,getData(position).name,false)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_two_goods_body_item, parent, false)
}