package com.cocosh.shmstore.newhome.adapter

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.GoodsShoppingActivity
import com.cocosh.shmstore.newhome.model.CreateGoodsBean
import kotlinx.android.synthetic.main.item_create_order_shop.view.*

class CreateOrderListAdapter(list: ArrayList<CreateGoodsBean>) : BaseRecycleAdapter<CreateGoodsBean>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).store_name
        holder.itemView.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.itemView.recyclerView.setHasFixedSize(true)
        holder.itemView.recyclerView.isNestedScrollingEnabled = false
        holder.itemView.recyclerView.adapter = CreateOrderItemAdapter(getData(position).goodsList
                ?: arrayListOf())

        holder.itemView.ll_shop.setOnClickListener {
            GoodsShoppingActivity.start(context, getData(position).store_name, getData(position).store_id)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_create_order_shop, parent, false)
}