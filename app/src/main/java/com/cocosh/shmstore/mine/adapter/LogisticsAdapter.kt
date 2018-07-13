package com.cocosh.shmstore.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import kotlinx.android.synthetic.main.item_logistics.view.*

/**
 * Created by lmg on 2018/4/12.
 * 收藏
 */
class LogisticsAdapter(var mContext: Context, list: ArrayList<String>) : BaseRecycleAdapter<String>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.itemView.lineUp.visibility = View.INVISIBLE
            }
            itemCount - 1 -> {
                holder.itemView.lineDown.visibility = View.INVISIBLE
            }
            else -> {
                holder.itemView.lineUp.visibility = View.VISIBLE
                holder.itemView.lineDown.visibility = View.VISIBLE
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_logistics, parent, false)
}