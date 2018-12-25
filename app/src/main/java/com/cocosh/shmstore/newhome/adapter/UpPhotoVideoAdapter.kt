package com.cocosh.shmstore.newhome.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import kotlinx.android.synthetic.main.item_update_img_video.view.*

class UpPhotoVideoAdapter(list: ArrayList<String>,var bottomPhotoDialog: BottomPhotoDialog) : BaseRecycleAdapter<String>(list) {

    var isPreView = false

    override fun getItemCount(): Int {
        if (list.size < 3 && !isPreView) {
            return list.size + 1
        }

        return list.size
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_update_img_video, parent, false)

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (position == list.size && list.size < 3 && !isPreView) {
            holder.itemView.view_delete.visibility = View.GONE
            holder.itemView.img_photo.setImageResource(R.mipmap.ic_update_refund)
            holder.itemView.setOnClickListener {
                bottomPhotoDialog.show()
            }
        } else {
            if (isPreView){
                holder.itemView.view_delete.visibility = View.GONE
            }else{
                holder.itemView.view_delete.visibility = View.VISIBLE
            }
            holder.itemView.setOnClickListener(null)
            Glide.with(context)
                    .load(getData(position))
                    .placeholder(ContextCompat.getColor(context, R.color.activity_bg))
                    .dontAnimate()
                    .into(holder.itemView.img_photo)

            holder.itemView.view_delete.setOnClickListener {
                list.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }
}