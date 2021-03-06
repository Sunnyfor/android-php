package com.cocosh.shmstore.vouchers.apdater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.utils.StringUtils
import com.cocosh.shmstore.vouchers.model.Vouchers
import kotlinx.android.synthetic.main.item_vouchers_select.view.*

class VouchersListSelectAdapter(arrayList: ArrayList<Vouchers>, var index: Int, var onChangeText: (String) -> Unit) : BaseRecycleAdapter<Vouchers>(arrayList) {

    var selectMap = hashMapOf<String, Vouchers>()

    init {
        if (index != -1) {
            list[index].let {
                selectMap[it.code] = it //默认选中
            }
        }
    }

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        holder.itemView.tvSelectMoney.text = getData(position).face_value
        holder.itemView.tvSelectDesc.text = ("投放金额为${getData(position).face_value}元时可使用，可累计")
        holder.itemView.tvSelectTime.text = ("${StringUtils.timeStampFormatDateYYMMdd(getData(position).stime,"yyyy.MM.dd")}-${StringUtils.timeStampFormatDateYYMMdd(getData(position).etime,"yyyy.MM.dd")}")

        holder.itemView.setOnClickListener { _ ->
            getData(position).code.let {
                if (selectMap[it] == null) {
                    selectMap[it] = list[position]
                } else {
                    selectMap.remove(it)
                }
                notifyDataSetChanged()

                if (selectMap.size == list.size) {
                    onChangeText("取消")
                } else {
                    onChangeText("全选")
                }
            }
        }

        if (selectMap[getData(position).code] != null) {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
        } else {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_no)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(context).inflate(R.layout.item_vouchers_select, parent, false)


    fun allSelect() {
        if (selectMap.size < list.size) {
            list.forEach {
                selectMap[it.code] = it
            }
            onChangeText("取消")
        } else {
            selectMap.clear()
            onChangeText("全选")
        }
        notifyDataSetChanged()
    }
}