package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.vouchers.model.Vouchers
import kotlinx.android.synthetic.main.layout_ad_recharge_list_item.view.*

class ADRechargeSelectAdapter(mList: ArrayList<Vouchers>) : BaseRecycleAdapter<Vouchers>(mList) {

    var selectMap = hashMapOf<String, Vouchers>()

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvMoney.text = getData(position).face_value
        holder.itemView.tvTime.text = ("有效期：${getData(position).stime}-${getData(position).etime}")


        holder.itemView.setOnClickListener { _ ->
            getData(position).code.let {
                if (selectMap[it] == null) {
                    selectMap[it] = list[position]
                } else {
                    selectMap.remove(it)
                }
                notifyDataSetChanged()

//                if (selectMap.size == list.size) {
//                    onChangeText("取消")
//                } else {
//                    onChangeText("全选")
//                }
            }
        }

        if (selectMap[getData(position).code] != null) {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
        } else {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_no)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(context).inflate(R.layout.layout_ad_recharge_list_item, parent, false)
}