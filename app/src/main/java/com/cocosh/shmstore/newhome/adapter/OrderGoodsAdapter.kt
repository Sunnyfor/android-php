package com.cocosh.shmstore.newhome.adapter

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.mine.ui.OrderDetailActivity
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.GoodsErrorActivity
import com.cocosh.shmstore.newhome.RefundActivity
import com.cocosh.shmstore.utils.DataCode
import kotlinx.android.synthetic.main.item_order_goods.view.*

class OrderGoodsAdapter(list: ArrayList<Order.Goods>, var isDesc: Boolean, var orderStatus: String,var sum:String) : BaseRecycleAdapter<Order.Goods>(list) {

    var order: Order? = null

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvGoodsName.text = getData(position).goods_name

        val descSB = StringBuilder()

        getData(position).attr.forEach {
            descSB.append(it.value).append("，")
        }
        descSB.deleteCharAt(descSB.lastIndex)

        holder.itemView.tvDesc.text = descSB.toString()
        holder.itemView.tvMoney.text = ("¥" + getData(position).price)
        holder.itemView.tvShowCount.text = ("x" + getData(position).salenum)
        Glide.with(context)
                .load(getData(position).image)
                .dontAnimate()
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        holder.itemView.txt_refund.visibility = View.GONE
        holder.itemView.txt_return.visibility = View.GONE
        holder.itemView.txt_desc.visibility = View.GONE

        val desc = when (getData(position).style) {
            1 -> {

                "退款中，等待卖家处理"
            }
            2 -> {
                "退货中，等待卖家处理"
            }
            3 -> {
                "卖家拒绝退款"
            }
            4 -> {
                if (orderStatus == "1") {
                    "已退款"
                } else {
                    "同意退货，等待买家发货"
                }
            }
            5 -> {
                if (orderStatus == "1") {
                    "卖家拒绝退款"
                } else {
                    "卖家拒绝退货"
                }
            }
            6 -> {
                "已退款"
            }
            7 -> {
                "等待卖家收货"
            }
            else -> ""

        }

        if (!isDesc) {
            holder.itemView.txt_desc.visibility = View.VISIBLE
            holder.itemView.txt_desc.text = desc

            holder.itemView.setOnClickListener {
                SmApplication.getApp().setData(DataCode.ORDER, order)
                OrderDetailActivity.start(context, order?.order_sn ?: "", order?.status ?: "",sum)
            }

        } else {

            when (getData(position).style) {
                0 -> {
                    if (orderStatus == "1") {
                        holder.itemView.txt_refund.visibility = View.VISIBLE
                        holder.itemView.txt_refund.setOnClickListener {
                            RefundActivity.start(context, 1, getData(position), order?.order_sn
                                    ?: "")
                        }
                    } else if (orderStatus == "3") {
                        holder.itemView.txt_return.visibility = View.VISIBLE
                        holder.itemView.txt_return.setOnClickListener {
                            RefundActivity.start(context, 2, getData(position), order?.order_sn
                                    ?: "")
                        }
                    }
                }
                else -> {

                    holder.itemView.txt_desc.visibility = View.VISIBLE
                    holder.itemView.txt_desc.setBackgroundResource(R.drawable.shape_rectangle_red_to_white)
                    holder.itemView.txt_desc.text = "查看详情"
                }

            }

            holder.itemView.txt_desc.setOnClickListener {
                if (getData(position).style == 1) {
                    RefundActivity.start(context, 3, getData(position), order?.order_sn
                            ?: "", desc)
                } else if (getData(position).style == 2) {
                    RefundActivity.start(context, 4, getData(position), order?.order_sn
                            ?: "", desc)
                } else if (getData(position).style == 4) {
                    if (orderStatus != "1") {
                        RefundActivity.start(context, 5, getData(position), order?.order_sn
                                ?: "", desc)
                    }
                } else if(getData(position).style == 5) {
                    RefundActivity.start(context,6, getData(position), order?.order_sn
                            ?: "", desc)
                }else {
                    RefundActivity.start(context,getData(position).style, getData(position), order?.order_sn
                            ?: "", desc)
                }
            }


            holder.itemView.setOnClickListener {
                if (getData(position).off == "0") {
                    GoodsDetailActivity.start(context, getData(position).goods_name, getData(position).goods_id)
                } else {
                    context.startActivity(Intent(context, GoodsErrorActivity::class.java))
                }
            }

        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_order_goods, parent, false)
}