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
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import kotlinx.android.synthetic.main.layout_shopping_goods_item.view.*

class ShoppingListItemGoodsAdapter(mList: ArrayList<ShoppingCarts>,var notifyData:()->Unit) : BaseRecycleAdapter<ShoppingCarts>(mList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvGoodsName.text = getData(position).goods_name
        holder.itemView.tvMoney.text = getData(position).sku_price
        holder.itemView.tvCount.text = getData(position).num
        Glide.with(context)
                .load(getData(position).sku_image)
                .placeholder(ColorDrawable(ContextCompat.getColor(context,R.color.activity_bg)))
                .into(holder.itemView.ivPhoto)

        if (getData(position).isChecked){
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
        }else{
            holder.itemView.vSelect.setBackgroundResource(R.drawable.bg_select_round_gray_no)
        }

        holder.itemView.rlSelect.setOnClickListener {
            getData(position).isChecked = !getData(position).isChecked
            notifyDataSetChanged()
            notifyData()
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_shopping_goods_item, parent, false)
}