package com.cocosh.shmstore.mine.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.InterestModel
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.item_archive_interesting_top.view.*

/**
 *
 * Created by lmg on 2018/4/12.
 */
class InterestingTopAdapter(list: ArrayList<InterestModel.Data>) : BaseRecycleAdapter<InterestModel.Data>(list) {

    private var defaultCustemData = InterestModel.Data("sm1168", "sm1168", "+自定义", 2)
    var onDeleteResult: OnDeleteResult? = null

    init {
        if (list.size < 3 && !list.contains(defaultCustemData) && list.none { it.status == 2 }) {
            list.add(defaultCustemData)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        holder.itemView.tvName.text = list[position].interestName
        holder.itemView.deleteRl.setOnClickListener {
            //删除
            if (position < list.size) {
                getData(position).let {
                    if (it.status == 2 || list.size == 1 || list.none { it.status == 2 }) {
                        list.add(defaultCustemData)
                    }
                    onDeleteResult?.onDeleteResult(it)
                    list.remove(it)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                }

            }
        }


        if (getData(position).status == 2) {
            holder.itemView.tvName.setBackgroundResource(R.drawable.shape_btn_red)
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context, R.color.white))
            if (getData(position).idUserDescribeGroup == "sm1168") {  //添加自定义按钮 隐藏减号
                holder.itemView.deleteRl.visibility = View.GONE
            } else {
                holder.itemView.deleteRl.visibility = View.VISIBLE
            }
        } else {
            holder.itemView.tvName.setBackgroundResource(R.drawable.shape_btn_grayline)
            holder.itemView.tvName.setTextColor(ContextCompat.getColor(context, R.color.textGray))
            holder.itemView.deleteRl.visibility = View.VISIBLE
        }

    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_archive_interesting_top, parent, false)


    interface OnDeleteResult {
        fun onDeleteResult(data: InterestModel.Data)
    }

    fun removeDefaultCustemData() {
        list.remove(defaultCustemData)
    }

    fun addData(data: InterestModel.Data):Boolean {
        if (list.contains(data)) {
            return false
        }

        if (list.size == 3) {
            if (list.contains(defaultCustemData)) {
                list.remove(defaultCustemData)
                list.add(data)
            }else{
                ToastUtil.show("已到上限")
            }
        } else {
            if (list.contains(defaultCustemData)) {
                list.add(list.size - 1, data)
            } else {
                list.add(data)
            }

        }
        return true
    }

    fun getDataSize():Int{
        if (list.contains(defaultCustemData))
            return list.size-1

        return list.size
    }
}