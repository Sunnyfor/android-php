package com.cocosh.shmstore.newhome.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder

class GoodsDetailPhotoAdapter(mList: ArrayList<String>) : BaseRecycleAdapter<String>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context).load(getData(position)).into(holder.itemView as ImageView).getSize { width, height ->
            holder.itemView.layoutParams.width = width
            holder.itemView.layoutParams.height = height
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_goods_detail_image, parent, false)
}