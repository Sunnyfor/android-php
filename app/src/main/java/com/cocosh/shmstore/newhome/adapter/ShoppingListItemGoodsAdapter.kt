package com.cocosh.shmstore.newhome.adapter

import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import kotlinx.android.synthetic.main.layout_shopping_goods_item.view.*

class ShoppingListItemGoodsAdapter(mList: ArrayList<ShoppingCarts>, var notifyData: () -> Unit) : BaseRecycleAdapter<ShoppingCarts>(mList) {

    var isEdit: Boolean = false

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvGoodsName.text = getData(position).goods_name
        holder.itemView.tvMoney.text = getData(position).sku_price

        val descSb = StringBuilder()
        getData(position).sku_attrs.values.forEach {
            descSb.append("$it，")
        }
        descSb.deleteCharAt(descSb.lastIndex)
        holder.itemView.tvDesc.text = descSb.toString()


        holder.itemView.tvCount.text = getData(position).num

        holder.itemView.tvShowCount.text = ("x ${getData(position).num}")

        Glide.with(context)
                .load(getData(position).sku_image)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        if (getData(position).isChecked) {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
        } else {
            holder.itemView.vSelect.setBackgroundResource(R.drawable.bg_select_round_gray_no)
        }

        if (isEdit) {
            holder.itemView.vJian.visibility = View.VISIBLE
            holder.itemView.vAdd.visibility = View.VISIBLE
            holder.itemView.tvCount.visibility = View.VISIBLE
            holder.itemView.tvShowCount.visibility = View.GONE
        } else {
            holder.itemView.vJian.visibility = View.INVISIBLE
            holder.itemView.vAdd.visibility = View.INVISIBLE
            holder.itemView.tvCount.visibility = View.GONE
            holder.itemView.tvShowCount.visibility = View.VISIBLE
        }

        holder.itemView.rlSelect.setOnClickListener {
            getData(position).isChecked = !getData(position).isChecked
            notifyDataSetChanged()
            notifyData()
        }

        holder.itemView.vAdd.setOnClickListener {
            val num = getData(position).num.toInt() + 1
            if (num < 999) {
                getData(position).num = num.toString()
                notifyData()
                notifyDataSetChanged()
            }
        }

        holder.itemView.vJian.setOnClickListener {
            val num = getData(position).num.toInt() - 1
            if (num >= 0) {
                getData(position).num = num.toString()
                notifyData()
                notifyDataSetChanged()
            }
        }

        holder.itemView.setOnClickListener {
            if (!isEdit) {
                GoodsDetailActivity.start(context, getData(position).goods_name, getData(position).goods_id)
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_shopping_goods_item, parent, false)
}