package com.cocosh.shmstore.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.AddressListModel
import kotlinx.android.synthetic.main.item_address_list.view.*

/**
 * Created by lmg on 2018/4/12.
 * 地址管理
 */
class AddressListAdapter(var mContext: Context, list: ArrayList<AddressListModel>) : BaseRecycleAdapter<AddressListModel>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (getData(position).isDefault == "1") {
            holder.itemView.rbCheck.setChecked(true)
            holder.itemView.tvCheck.text = "默认地址"
        } else {
            holder.itemView.rbCheck.setChecked(false)
            holder.itemView.tvCheck.text = "设为默认地址"
        }
        holder.itemView.name.text = getData(position).addressName
        holder.itemView.phone.text = getData(position).addressPhone
        holder.itemView.address.text = getData(position).address

        holder.itemView.select.setOnClickListener {
            mOnSelectItemListener?.itemSelect(position)
        }
        holder.itemView.rbCheck.setOnClickListener {
            mOnSelectItemListener?.checkedChange(position)
        }

        holder.itemView.edit.setOnClickListener {
            mOnSelectItemListener?.onEdit(position)
        }
        holder.itemView.tvEdit.setOnClickListener {
            mOnSelectItemListener?.onEdit(position)
        }
        holder.itemView.delete.setOnClickListener {
            mOnSelectItemListener?.onDelete(position)
        }
        holder.itemView.tvDelete.setOnClickListener {
            mOnSelectItemListener?.onDelete(position)
        }
    }


    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_address_list, parent, false)

    interface OnSelectItemListener {
        fun onEdit(index: Int)
        fun onDelete(index: Int)
        fun checkedChange(index: Int)
        fun itemSelect(index: Int)
    }

    var mOnSelectItemListener: OnSelectItemListener? = null

    fun setOnSelectItemListener(mOnSelectItemListener: OnSelectItemListener) {
        this.mOnSelectItemListener = mOnSelectItemListener
    }
}