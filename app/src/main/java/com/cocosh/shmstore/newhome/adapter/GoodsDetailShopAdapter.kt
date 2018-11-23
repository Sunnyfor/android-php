package com.cocosh.shmstore.newhome.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder

class GoodsDetailShopAdapter(mList:ArrayList<String>): BaseRecycleAdapter<String>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_goods_detail_shop,parent,false)
}