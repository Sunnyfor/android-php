package com.cocosh.shmstore.newhome.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.model.Goods
import kotlinx.android.synthetic.main.layout_recommend_item.view.*

class RecommendAdapter(mList: ArrayList<Goods>) : BaseRecycleAdapter<Goods>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).goods_name
        holder.itemView.tvMoney.text = getData(position).price
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_recommend_item, parent, false)
}