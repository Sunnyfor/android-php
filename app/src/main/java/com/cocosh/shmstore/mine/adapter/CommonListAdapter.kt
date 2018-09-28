package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.CommonModel
import com.cocosh.shmstore.widget.view.ItemShowView
import kotlinx.android.synthetic.main.item_common_list_sub.view.*

/**
 * 适配器
 * Created by lmg on 2018/3/13.
 */
class CommonListAdapter(list: ArrayList<CommonModel>, var dataType: Int) : BaseRecycleAdapter<CommonModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            val itemView = (holder.itemView as ItemShowView)
            itemView.setName(list[position].name)

            if (dataType == 0) {
                itemView.setNoIconValue("小红娘号:${list[position].smno}")
            } else {
                itemView.setValue("小红娘号:${list[position].name}")
                if (getData(position).isExpand == true) {
                    itemView.setIcon(context.getString(R.string.iconMoreDown))
                } else {
                    itemView.setIcon(context.getString(R.string.iconMore))
                }
            }
        }

        if (getItemViewType(position) == 1) {
            holder.itemView.nameSub.text = list[position].name
            holder.itemView.shouMeiNumberSub.text = ("小红娘号:${list[position].smno}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataType == 0) {
            return 0
        } else {
            if (list[position].itemType == "0") {
                return 0
            }
            if (list[position].itemType == "1") {
                return 1
            }
            return 2
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 0) {
            return ItemShowView(context)
        }
        if (viewType == 1) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_common_list_sub, parent, false)
        }
        return LayoutInflater.from(parent.context).inflate(R.layout.item_common_list_title, parent, false)
    }

}