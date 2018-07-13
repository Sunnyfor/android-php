package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import kotlinx.android.synthetic.main.item_order_list.view.*

/**
 * 订单
 * Created by lmg on 2018/3/13.
 */
class OrderListAdapter(list: ArrayList<String>) : BaseRecycleAdapter<String>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        when (list[position]) {
            "待付款" -> {
                holder.itemView.timeLeave.visibility = View.VISIBLE
                holder.itemView.orderStatus.text = "待付款"
                holder.itemView.look.visibility = View.GONE
                holder.itemView.service.visibility = View.GONE
                holder.itemView.pay.visibility = View.VISIBLE
            }
            "待发货" -> {
                holder.itemView.timeLeave.visibility = View.GONE
                holder.itemView.orderStatus.text = "待发货"
                holder.itemView.look.visibility = View.GONE
                holder.itemView.service.visibility = View.VISIBLE
                holder.itemView.pay.visibility = View.GONE
            }
            "待收货" -> {
                holder.itemView.timeLeave.visibility = View.GONE
                holder.itemView.orderStatus.text = "待收货"
                holder.itemView.look.visibility = View.VISIBLE
                holder.itemView.service.visibility = View.VISIBLE
                holder.itemView.pay.visibility = View.VISIBLE
                holder.itemView.pay.text = "确认收货"
            }
            "交易完成" -> {
                holder.itemView.timeLeave.visibility = View.GONE
                holder.itemView.orderStatus.text = "交易完成"
                holder.itemView.look.visibility = View.GONE
                holder.itemView.service.visibility = View.GONE
                holder.itemView.pay.visibility = View.VISIBLE
                holder.itemView.pay.text = "删除订单"
            }
            else -> {
            }
        }

        holder.itemView.pay.setOnClickListener {
            if (list[position] == "待收货") {
                mOnBtnClickListener.realBtn(list[position], position)
            }
            if (list[position] == "交易完成") {
                mOnBtnClickListener.deleteBtn(list[position], position)
            }
            if (list[position] == "待付款") {
                mOnBtnClickListener.payBtn(list[position], position)
            }
        }
        holder.itemView.service.setOnClickListener {
            mOnBtnClickListener.serviceBtn(list[position], position)
        }
        holder.itemView.look.setOnClickListener {
            mOnBtnClickListener.lookBtn(list[position], position)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_order_list, parent, false)

    interface OnBtnClickListener {
        fun payBtn(data: String, position: Int)
        fun deleteBtn(data: String, position: Int)
        fun realBtn(data: String, position: Int)
        fun serviceBtn(data: String, position: Int)
        fun lookBtn(data: String, position: Int)
    }

    lateinit var mOnBtnClickListener: OnBtnClickListener

    fun setOnBtnClickListener(mOnBtnClickListener: OnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener
    }
}