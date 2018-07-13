package com.cocosh.shmstore.mine.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.listener.InterestingDataListener
import com.cocosh.shmstore.mine.model.InterestModel
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.item_archive_interesting_list_sub.view.*

/**
 *
 * Created by lmg on 2018/4/12.
 */
class InterestingBottomSubAdapter(list: ArrayList<InterestModel.Data>, private val selectData:ArrayList<InterestModel.Data>,var position: Int) : BaseRecycleAdapter<InterestModel.Data>(list) {
    private var onDataListener: InterestingDataListener? = null
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = list[position].interestName

        if(getData(position).status != 1){
            getData(position).status = 1
        }

        if(selectData.contains(getData(position))){
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.itemView.tvName.setBackgroundResource(R.drawable.shape_btn_red)
        }else{
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context,R.color.blackText))
            holder.itemView.tvName.setBackgroundResource(R.color.transparent)
        }

        holder.itemView.setOnClickListener {
            onDataListener?.dataCallBack(this.position,position,getData(position))
        }
    }


    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_archive_interesting_list_sub, parent, false)

    fun setOnDataListener(onDataListener: InterestingDataListener): InterestingBottomSubAdapter {
        this.onDataListener = onDataListener
        return this@InterestingBottomSubAdapter
    }
}