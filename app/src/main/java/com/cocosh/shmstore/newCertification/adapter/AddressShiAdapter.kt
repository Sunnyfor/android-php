package com.coco_sh.shmstore.mine.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.newCertification.model.AddressServiceModel


/**
 * Created by cjl on 2017/11/3.
 * type 类型 0是新媒人 1 是服务商
 */
class AddressShiAdapter(list: ArrayList<AddressServiceModel>, var type: Int) : BaseRecycleAdapter<AddressServiceModel>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        val childrensBean = getData(position)
        holder.getView<TextView>(R.id.sheng).text = childrensBean.areaName + ""
        if (type == 333) {
            if (childrensBean.addOperator == false) {
                holder.getView<TextView>(R.id.sheng).setTextColor(Color.parseColor("#999999"))
                holder.getView<ImageView>(R.id.iv).visibility = View.GONE
            } else {
                holder.getView<TextView>(R.id.sheng).setTextColor(Color.parseColor("#333333"))
                holder.getView<ImageView>(R.id.iv).visibility = View.GONE
            }
        } else {
            if (childrensBean.isChecked == 1) {
                holder.getView<TextView>(R.id.sheng).setTextColor(Color.parseColor("#d8263c"))
                holder.getView<ImageView>(R.id.iv).visibility = View.VISIBLE
            } else {
                holder.getView<TextView>(R.id.sheng).setTextColor(Color.parseColor("#333333"))
                holder.getView<ImageView>(R.id.iv).visibility = View.GONE
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_address_tab, parent, false)
}