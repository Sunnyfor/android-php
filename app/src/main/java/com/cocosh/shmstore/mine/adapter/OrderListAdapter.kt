package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_order_list.view.*

/**
 * 订单
 * Created by lmg on 2018/3/13.
 */
class OrderListAdapter(list: ArrayList<Order>) : BaseRecycleAdapter<Order>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        when (list[position].status) {
            "0" -> { //待付款
                holder.itemView.timeLeave.visibility = View.VISIBLE
                holder.itemView.orderStatus.text = "待付款"
                holder.itemView.look.visibility = View.GONE
                holder.itemView.service.visibility = View.GONE
                holder.itemView.pay.visibility = View.VISIBLE
            }
            "1" -> { //待发货
                holder.itemView.timeLeave.visibility = View.GONE
                holder.itemView.orderStatus.text = "待发货"
                holder.itemView.look.visibility = View.GONE
                holder.itemView.service.visibility = View.VISIBLE
                holder.itemView.pay.visibility = View.GONE
            }
            "2" -> { //待收货
                holder.itemView.timeLeave.visibility = View.GONE
                holder.itemView.orderStatus.text = "待收货"
                holder.itemView.look.visibility = View.VISIBLE
                holder.itemView.service.visibility = View.VISIBLE
                holder.itemView.pay.visibility = View.VISIBLE
                holder.itemView.pay.text = "确认收货"
            }
            "5" -> { //交易完成
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

        GlideUtils.loadPhoto(context,getData(position).image,holder.itemView.goodsPic,0)
        holder.itemView.number.text = getData(position).sn
        holder.itemView.shopName.text = getData(position).name //商品名称
        holder.itemView.goodsPrice.text = ("￥"+getData(position).price) //单价
        holder.itemView.goodsNum.text = ("x${getData(position).salenum}")
        holder.itemView.tvMoney.text = ("共计${getData(position).actual}元 包邮")

        holder.itemView.pay.setOnClickListener {
            if (list[position].status == "2") {
                mOnBtnClickListener.realBtn(list[position].id, position)
            }
            if (list[position].status == "5") {
                mOnBtnClickListener.deleteBtn(list[position].id, position)
            }
            if (list[position].status == "0") {
                mOnBtnClickListener.payBtn(list[position].id, position)
            }
        }
        holder.itemView.service.setOnClickListener {
            mOnBtnClickListener.serviceBtn(list[position].id, position)
        }
        holder.itemView.look.setOnClickListener {
            mOnBtnClickListener.lookBtn(list[position].id, position)
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