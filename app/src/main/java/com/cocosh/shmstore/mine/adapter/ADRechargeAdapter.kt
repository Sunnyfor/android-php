package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.ADRecharge
import kotlinx.android.synthetic.main.layout_ad_recharge_item.view.*

class ADRechargeAdapter(arrayList: ArrayList<ADRecharge>) : BaseRecycleAdapter<ADRecharge>(arrayList) {

    private var isGone = false

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvMoney.text = getData(position).money
        holder.itemView.tvDesc.text = ("¥" + getData(position).money)
        holder.itemView.tvName.text = ("¥" + getData(position).money + "充值券")
        if (isGone) {
            holder.itemView.rlGive.visibility = View.GONE
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int):
            View = LayoutInflater.from(context).inflate(R.layout.layout_ad_recharge_item, parent, false)


    fun goneGive() {
        isGone = true
        notifyDataSetChanged()
    }
}