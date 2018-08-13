package com.cocosh.shmstore.newCertification.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.Ethnic

/**
 * 选择服务商适配器
 * Created by zhangye on 2018/4/2.
 */
class SelectServiceAdapter(lsit: ArrayList<Ethnic>) : BaseRecycleAdapter<Ethnic>(lsit) {

    var index = -1

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.getView<TextView>(R.id.sheng).text = getData(position).name
        if (position == index) {
            holder.getView<TextView>(R.id.sheng).setTextColor(ContextCompat.getColor(context, R.color.red))
            holder.getView<ImageView>(R.id.iv).visibility = View.VISIBLE
        } else {
            holder.getView<TextView>(R.id.sheng).setTextColor(ContextCompat.getColor(context, R.color.blackText))
            holder.getView<ImageView>(R.id.iv).visibility = View.GONE
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_address_tab, parent, false)

}