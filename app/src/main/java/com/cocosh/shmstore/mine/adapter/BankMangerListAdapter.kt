package com.cocosh.shmstore.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.BankModel
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_bankcard_manger.view.*

/**
 * 我的顶部导航适配器
 * Created by zhangye on 2018/3/13.
 */
class BankMangerListAdapter(var mContext: Context, list: ArrayList<BankModel>) : BaseRecycleAdapter<BankModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvBankName.text = getData(position).bank_name
        if (getData(position).card_kind == "0") {
            holder.itemView.tvBankType.text = "储蓄卡"
        } else {
            holder.itemView.tvBankType.text = "信用卡"
        }
        holder.itemView.tvBankNum.text = getData(position).card_no
        GlideUtils.loadDefault(mContext, getData(position).bank_log, holder.itemView.rivLogo)
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
//        if (viewType == 2) {
//            return LayoutInflater.from(parent.context).inflate(R.layout.item_bankcard_manger_bottom, parent, false)
//        }

        return LayoutInflater.from(parent.context).inflate(R.layout.item_bankcard_manger, parent, false)
    }
//
//    override fun getItemCount(): Int {
//        return list.size + 1
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        if (position == itemCount - 1) {
//            return 2
//        } else {
//            return 1
//        }
//    }
}