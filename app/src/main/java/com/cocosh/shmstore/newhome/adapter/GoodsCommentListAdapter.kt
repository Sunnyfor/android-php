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
import com.cocosh.shmstore.newhome.model.CommentBean
import kotlinx.android.synthetic.main.item_comment.view.*

class GoodsCommentListAdapter(list: ArrayList<CommentBean>) : BaseRecycleAdapter<CommentBean>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        Glide.with(context)
                .load(getData(position).avatar)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.imgPhoto)

        holder.itemView.txt_name.text = getData(position).nickname
        holder.itemView.ratingBar.setStar(getData(position).stars.toFloat())

        val desc = when (getData(position).stars) {
            1 -> "非常差"
            2 -> "差"
            3 -> "一般"
            4 -> "好"
            5 -> "非常好"
            else -> "非常好"
        }

        holder.itemView.txt_detail.text = desc
        holder.itemView.txt_content.text = getData(position).words
        holder.itemView.txt_time.text = getData(position).time
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
}