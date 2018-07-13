package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.RedWaterModel
import kotlinx.android.synthetic.main.item_red_account_list.view.*

/**
 * 适配器
 * Created by zhangye on 2018/3/13.
 */
class RedAccountListAdapter(list: ArrayList<RedWaterModel>) : BaseRecycleAdapter<RedWaterModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.nav_gray)
        } else {
            holder.itemView.setBackgroundResource(R.color.white)
        }
        holder.itemView.tvType.text = getData(position).recordDesc
        holder.itemView.tvNum.text ="流水号："+ getData(position).runningNumber
        holder.itemView.tvTime.text = getData(position).createTime

        if (getData(position).resultType == "1" || getData(position).resultType == "4") {
            holder.itemView.tvMoney.text = "+" + getData(position).accountMoney
            holder.itemView.civPic.setBackgroundResource(R.drawable.inwater)
            return
        } else {
            holder.itemView.tvMoney.text = getData(position).accountMoney
            holder.itemView.civPic.setBackgroundResource(R.drawable.outwater)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_red_account_list, parent, false)

}