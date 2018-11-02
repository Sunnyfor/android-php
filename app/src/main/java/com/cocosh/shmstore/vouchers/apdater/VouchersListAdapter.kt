package com.cocosh.shmstore.vouchers.apdater

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.vouchers.VouchersListSelectActivity
import com.cocosh.shmstore.vouchers.model.Vouchers
import kotlinx.android.synthetic.main.item_vouchers_default.view.*

class VouchersListAdapter(arrayList: ArrayList<Vouchers>, var type: Int) : BaseRecycleAdapter<Vouchers>(arrayList) {


    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        when (type) {
            1 -> {
                changeState(holder)
                holder.itemView.vState.setBackgroundResource(R.mipmap.ic_vouchers_state_use)
            }
            2 -> {
                changeState(holder)
                holder.itemView.vState.setBackgroundResource(R.mipmap.ic_vouchers_state_give)
            }
            3 -> {
                changeState(holder)
                holder.itemView.vState.setBackgroundResource(R.mipmap.ic_vouchers_state_outtime)
            }
        }


        holder.itemView.tvMoney.text = getData(position).money
        holder.itemView.tvDesc.text = ("投放金额为${getData(position).money}元时可使用，可累计")
        holder.itemView.tvTime.text = getData(position).date


        holder.itemView.tvUse.setOnClickListener {
            val intent = Intent(context, VouchersListSelectActivity::class.java)
            intent.putExtra("index", position)
            SmApplication.getApp().setData(DataCode.VOUCHERS,list)
            context.startActivity(intent)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.item_vouchers_default, parent, false)
    }

    private fun changeState(holder: BaseRecycleViewHolder) {
        holder.itemView.llOption.visibility = View.GONE
        holder.itemView.vState.visibility = View.VISIBLE
        holder.itemView.tvUnit.setTextColor(ContextCompat.getColor(context, R.color.grayText))
        holder.itemView.tvMoney.setTextColor(ContextCompat.getColor(context, R.color.grayText))
    }

}