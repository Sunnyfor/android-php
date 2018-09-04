package com.cocosh.shmstore.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.mine.model.IndustryModel
import kotlinx.android.synthetic.main.layout_industry_item_body.view.*
import kotlinx.android.synthetic.main.layout_industry_item_title.view.*

/**
 * 行业适配器
 * Created by zhangye on 2018/9/4.
 */
class IndustryAdapter(list: ArrayList<IndustryModel>, private var industry: String?) : BaseRecycleAdapter<IndustryModel>(list) {

    private var position = -1

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        if (getData(position).parent == "0") {
            holder.itemView.tvName.text = getData(position).name
        } else {
            industry?.let {
                if (getData(position).id == it) {
                    this.position = position
                    holder.itemView.viewSelect.visibility = View.VISIBLE
                }else{
                    holder.itemView.viewSelect.visibility = View.GONE
                }
            }
            holder.itemView.tvDesc.text = getData(position).name
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 1) {
            return LayoutInflater.from(context).inflate(R.layout.layout_industry_item_title, parent, false)
        }
        return LayoutInflater.from(context).inflate(R.layout.layout_industry_item_body, parent, false)
    }

    override fun getItemViewType(position: Int): Int {
        if (getData(position).parent == "0") {
            return 1
        }
        return 2
    }

    fun updateState(position: Int){
        industry = getData(position).id
        notifyItemChanged(this.position)
        notifyItemChanged(position)
        this.position = position
    }
}