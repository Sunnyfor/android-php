package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.WalletWaterModel
import kotlinx.android.synthetic.main.item_money_water_list.view.*

/**
 * 适配器
 * Created by lmg on 2018/3/13.
 */
class MoneyWaterListAdapter(list: ArrayList<WalletWaterModel>) : BaseRecycleAdapter<WalletWaterModel>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.nav_gray)
        } else {
            holder.itemView.setBackgroundResource(R.color.white)
        }

        getData(position).run {
            holder.itemView.tvType.text = memo
            holder.itemView.tvNum.text = ("流水号：$flowno")
            holder.itemView.tvTime.text = time
            holder.itemView.tvMoney.text = ("$amount")

            if (fine_type == "1") {
                holder.itemView.civPic.setBackgroundResource(R.drawable.inwater)
            } else {
                holder.itemView.civPic.setBackgroundResource(R.drawable.outwater)
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_money_water_list, parent, false)
}