package com.cocosh.shmstore.newhome.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.model.GoodsDetail
import kotlinx.android.synthetic.main.item_goods_params.view.*

class GoodsParamsAdapter(list:ArrayList<GoodsDetail.Goods.Params>):BaseRecycleAdapter<GoodsDetail.Goods.Params>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.txt_params_name.text = getData(position).name
        holder.itemView.txt_params_value.text = getData(position).value
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_goods_params,parent,false)
}