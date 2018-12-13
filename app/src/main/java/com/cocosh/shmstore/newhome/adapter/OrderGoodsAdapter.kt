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
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.item_order_goods.view.*

class OrderGoodsAdapter(list: ArrayList<Order.Goods>, var isDesc: Boolean) : BaseRecycleAdapter<Order.Goods>(list) {

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

        if (isDesc) {
            holder.itemView.setOnClickListener {
                if (getData(position).off == "0"){
                    GoodsDetailActivity.start(context, getData(position).goods_name, getData(position).goods_id)
                }else{
                    context.startActivity(Intent(context, GoodsErrorActivity::class.java))
                }
            }
        } else {
            holder.itemView.setOnClickListener {
                SmApplication.getApp().setData(DataCode.ORDER_GOODS, order)
                OrderDetailActivity.start(context, order?.order_sn ?: "", order?.status ?: "")
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.item_order_goods, parent, false)
}