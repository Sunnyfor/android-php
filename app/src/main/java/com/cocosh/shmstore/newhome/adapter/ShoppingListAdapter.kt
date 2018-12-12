package com.cocosh.shmstore.newhome.adapter

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newhome.GoodsShoppingActivity
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import kotlinx.android.synthetic.main.layout_shopping_shop_item.view.*

class ShoppingListAdapter(mList: ArrayList<ShoppingCarts.Shopping>, var action: () -> Unit) : BaseRecycleAdapter<ShoppingCarts.Shopping>(mList) {

    var isEdit: Boolean = false

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).store_name
        holder.itemView.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.itemView.recyclerView.isNestedScrollingEnabled = false
        holder.itemView.recyclerView.isFocusableInTouchMode = false

        val adapter = ShoppingListItemGoodsAdapter(getData(position).goodsList) {
            action()
            list.removeAll(list.filter { it.goodsList.size == 0 })
            notifyDataSetChanged()
        }
        adapter.isEdit = isEdit
        holder.itemView.recyclerView.adapter = adapter

        holder.itemView.llSelect.setOnClickListener { _ ->
            getData(position).isChecked = !getData(position).isChecked
            getData(position).goodsList.forEach {
                it.isChecked = getData(position).isChecked
            }
            action()
            notifyDataSetChanged()
        }

        if (getData(position).goodsList.filter { it.isChecked == true }.size == getData(position).goodsList.size) {
            holder.itemView.vSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
            getData(position).isChecked = true
        } else {
            holder.itemView.vSelect.setBackgroundResource(R.drawable.bg_select_round_gray_no)
            getData(position).isChecked = false
        }

        holder.itemView.ll_shop.setOnClickListener {
            GoodsShoppingActivity.start(context,getData(position).store_name,getData(position).store_id)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_shopping_shop_item, parent, false)

    //全选
    fun selectAll(isAll: Boolean) {
        list.forEach { it ->
            it.goodsList.forEach {
                it.isChecked = isAll
            }
        }
        action()
        notifyDataSetChanged()
    }

    fun setModel(isEdit: Boolean) {
        this.isEdit = isEdit
    }
}