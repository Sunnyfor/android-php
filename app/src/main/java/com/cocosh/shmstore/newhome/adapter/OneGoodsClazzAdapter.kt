package com.cocosh.shmstore.newhome.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.model.GoodsClazz
import kotlinx.android.synthetic.main.layout_clazz_text.view.*

class OneGoodsClazzAdapter(mList: ArrayList<GoodsClazz.Bean>) : BaseRecycleAdapter<GoodsClazz.Bean>(mList) {

    var index = 0

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).name
        if (position == index){
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context,R.color.red))
        }else{
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context,R.color.blackText))
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_clazz_text, parent,false)
}