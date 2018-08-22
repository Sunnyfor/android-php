package com.cocosh.shmstore.widget.dialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.BankModel
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_select_bank.view.*

/**
 *
 * Created by lmg on 2018/4/12.
 */
class BankListAdapter(var mContext: Context, list: ArrayList<BankModel>) : BaseRecycleAdapter<BankModel>(list) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (getItemViewType(position) == 3) {
            holder.itemView.tvName.text = getData(position - 1).bank_name
            var url = getData(position - 1).bank_log
            var tag = holder.itemView.ivPic
            GlideUtils.loadHead(mContext, url, tag)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 1
        }
        if (position == itemCount - 1) {
            return 2
        }
        return 3
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 1) {
            return LayoutInflater.from(context).inflate(R.layout.item_select_bank_top, parent, false)
        }

        if (viewType == 2) {
            return LayoutInflater.from(context).inflate(R.layout.item_select_bank_bottom, parent, false)
        }
        return LayoutInflater.from(context).inflate(R.layout.item_select_bank, parent, false)
    }
}