package com.cocosh.shmstore.widget.popupwindow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.ADRechargeMoney
import kotlinx.android.synthetic.main.layout_select_money_item.view.*

class SelectMoneyAdapter(mList: ArrayList<ADRechargeMoney>) : BaseRecycleAdapter<ADRechargeMoney>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvMoney.text = ("¥${getData(position).money}")
        holder.itemView.tvDesc.text = ("送 ¥${getData(position).give}")
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_select_money_item, parent, false)
}