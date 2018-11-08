package com.cocosh.shmstore.widget.dialog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.vouchers.model.Vouchers
import kotlinx.android.synthetic.main.item_vouchers_simple.view.*

class VouchersListDialogAdapter(list: ArrayList<Vouchers>) : BaseRecycleAdapter<Vouchers>(list) {

    var selectMap = hashMapOf<String, Vouchers>()

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = ("投放金额为${getData(position).limit}元时可使用，可累计")
        holder.itemView.tvTime.text = ("有效时间: ${getData(position).stime}-${getData(position).etime}")
        holder.itemView.tvMoney.text = getData(position).face_value

        if (selectMap[getData(position).id] != null) {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes2)
        } else {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_no2)
        }

        holder.itemView.setOnClickListener { _ ->
            getData(position).id.let {
                if (selectMap[it] == null) {
                    selectMap[it] = list[position]
                } else {
                    selectMap.remove(it)
                }
                notifyDataSetChanged()

            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_vouchers_simple, parent, false)
}