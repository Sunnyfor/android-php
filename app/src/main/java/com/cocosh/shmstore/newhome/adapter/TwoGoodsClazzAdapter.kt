package com.cocosh.shmstore.newhome.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.model.GoodsClazz
import kotlinx.android.synthetic.main.layout_two_goods_title_item.view.*

class TwoGoodsClazzAdapter(mList: ArrayList<GoodsClazz.Bean>) : BaseRecycleAdapter<GoodsClazz.Bean>(mList) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (getItemViewType(position) == 0 ){
            holder.itemView.tvName.text = getData(position).name
        }else {
            (holder.itemView as RecyclerView).adapter = TwoGoodsGridAdapter(list.filter { it.deep == "3" && it.parent == list[position-1].id} as ArrayList<GoodsClazz.Bean>)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 0) {
            return LayoutInflater.from(context).inflate(R.layout.layout_two_goods_title_item, parent, false)
        }

        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        return recyclerView
    }

    override fun getItemViewType(position: Int): Int {
        return if (getData(position).deep == "2") {
            0
        } else {
            1
        }
    }
}