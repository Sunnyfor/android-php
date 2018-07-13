package com.cocosh.shmstore.widget.dialog.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.IndustryModel

/**
 *
 * Created by zhangye on 2018/4/12.
 */
class SelectDialogAdapter(list: ArrayList<IndustryModel>) : BaseRecycleAdapter<IndustryModel>(list) {

    var index = -1

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.getView<TextView>(R.id.tvName).text = getData(position).industryName
//
//        if(index == position){
//            holder.getView<RelativeLayout>(R.id.rlayoutBg).setBackgroundResource(R.color.red)
//            holder.getView<View>(R.id.ivLine).visibility = View.GONE
//            holder.getView<TextView>(R.id.tvName).setTextColor(ContextCompat.getColor(activity,R.color.white))
//        }else{
//            holder.getView<RelativeLayout>(R.id.rlayoutBg).setBackgroundResource(R.color.white)
//            holder.getView<View>(R.id.ivLine).visibility = View.VISIBLE
//            holder.getView<TextView>(R.id.tvName).setTextColor(ContextCompat.getColor(activity,R.color.textGray))
//        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_select_item, parent, false)
}