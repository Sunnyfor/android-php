package com.cocosh.shmstore.mine.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.mine.model.Ethnic
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.item_archive_interesting_list_sub.view.*

/**
 * 兴趣爱好
 * Created by zhangye on 2018/8/10.
 */
class InterestingAdapter(list:ArrayList<Ethnic>, private var selectMap:HashMap<String,String>):BaseRecycleAdapter<Ethnic>(list) {

    var onitemClickListener:OnItemClickListener? = null

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).name

        if (selectMap.containsKey(getData(position).id)){
            holder.itemView.tvName.setTextColor(Color.WHITE)
            holder.itemView.tvName.setBackgroundResource(R.drawable.shape_rectangle_red)
        }else{
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context,R.color.blackText))
            holder.itemView.tvName.setBackgroundResource(R.drawable.shape_rectangle_gray_to_white)
        }

        holder.itemView.setOnClickListener {
            if (selectMap.containsKey(getData(position).id)){
                selectMap.remove(getData(position).id)
            }else{
                if (selectMap.size >=4 ){
                    ToastUtil.show("已达到上限")
                    return@setOnClickListener
                }
                selectMap[getData(position).id] = getData(position).name
            }
            notifyDataSetChanged()
            onitemClickListener?.onItemClick(it,position)
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            View.inflate(context, R.layout.item_archive_interesting_list_sub,null)

}