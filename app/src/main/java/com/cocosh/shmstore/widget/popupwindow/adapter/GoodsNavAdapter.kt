package com.cocosh.shmstore.widget.popupwindow.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.model.GoodsNav
import kotlinx.android.synthetic.main.layout_goods_nav_item.view.*

class GoodsNavAdapter(mList: ArrayList<GoodsNav.Data>, var index:Int,var result:(Int)->Unit,var popupWindow: PopupWindow) : BaseRecycleAdapter<GoodsNav.Data>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).name
        if (index == position){
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context,R.color.red))
            holder.itemView.tvName.setBackgroundResource(R.drawable.bg_goods_nav_select)
        }else{
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context,R.color.blackText))
            holder.itemView.tvName.setBackgroundResource(R.color.white)
        }
        holder.itemView.setOnClickListener {
            result(position)
            popupWindow.dismiss()
        }
    }
    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_goods_nav_item,parent,false)
}