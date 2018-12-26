package com.cocosh.shmstore.newhome.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.model.Courier

class SelectCourierAdapter(list:ArrayList<Courier>):BaseRecycleAdapter<Courier>(list) {
    var index = -1

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.getView<TextView>(R.id.tvName).text = getData(position).com
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_select_item, parent, false)
}