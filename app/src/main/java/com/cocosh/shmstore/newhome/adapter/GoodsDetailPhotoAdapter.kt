package com.cocosh.shmstore.newhome.adapter

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import kotlinx.android.synthetic.main.layout_goods_detail_image.view.*

class GoodsDetailPhotoAdapter(mList: ArrayList<String>) : BaseRecycleAdapter<String>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context).load(getData(position))
                .asBitmap()
                .placeholder(ColorDrawable(ContextCompat.getColor(context,R.color.activity_bg)))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>?) {
                        val scale = Math.abs(resource.width.toFloat() / resource.height.toFloat())
                        val photoParams = holder.itemView.ivPhoto.layoutParams
                        photoParams.height = (context.resources.getDimension(R.dimen.w1080) / scale).toInt()
                        holder.itemView.ivPhoto.layoutParams = photoParams
                        holder.itemView.ivPhoto.setImageBitmap(resource)
                    }
                })

//                .into(holder.itemView as ImageView)
//                .getSize { width, height ->
//                    val scale = Math.abs(width.toFloat() / height.toFloat())
//                    holder.itemView.layoutParams.height = (width/scale).toInt()
//                }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_goods_detail_image, parent, false)
}