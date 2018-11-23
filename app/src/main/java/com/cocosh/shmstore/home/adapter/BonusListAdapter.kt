package com.cocosh.shmstore.home.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.Bonus2
import com.cocosh.shmstore.home.model.BonusModel
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.layout_bonus_list_item.view.*

/**
 * 红包列表
 * Created by zhangye on 2018/4/19.
 */
class BonusListAdapter(bonusList: ArrayList<Bonus2>) : BaseRecycleAdapter<Bonus2>(bonusList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).name
//        if (getData(position).typeInfo == 1 || getData(position).typeInfo == 2 || getData(position).typeInfo == 3) {
        holder.itemView.tvMoney.visibility = View.GONE
//            holder.itemView.tvMoney.text = ("最大金额 ：${getData(position).redpacketMax}元")



        if (getData(position).draw == "1") {
            holder.itemView.tvSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.w43))
            holder.itemView.tvSure.setBackgroundResource(R.color.grayBtn)
            //已抢
            holder.itemView.tvSure.text = "已抢"
        } else {
            holder.itemView.tvSure.text = "未抢"
            holder.itemView.tvSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.w43))
            holder.itemView.tvSure.setBackgroundResource(R.color.red)
        }
//        } else {
//            holder.itemView.tvMoney.visibility = View.GONE
//            holder.itemView.tvSure.setBackgroundResource(R.color.red)
//            holder.itemView.tvSure.text = "爱心刮刮乐"
//            holder.itemView.tvSure.text = "爱心大转盘"
//        }
        Glide.with(context).load(list[position].image).placeholder(R.drawable.default_home_bonus).into(holder.itemView.ivPhoto)


    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_bonus_list_item, parent, false)
}