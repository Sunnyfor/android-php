package com.cocosh.shmstore.home.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.BonusModel
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.layout_bonus_list_item.view.*

/**
 * 红包列表
 * Created by zhangye on 2018/4/19.
 */
class BonusListAdapter(bonusList: ArrayList<BonusModel.Data>) : BaseRecycleAdapter<BonusModel.Data>(bonusList) {
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvName.text = getData(position).redPacketName
        if (getData(position).typeInfo == 1 || getData(position).typeInfo == 2 || getData(position).typeInfo == 3) {
            holder.itemView.tvMoney.visibility = View.GONE
            holder.itemView.tvMoney.text = ("最大金额 ：${getData(position).redpacketMax}元")

            when {

                getData(position).redPacketStatus == 1 -> {
                    //已抢
                    holder.itemView.tvSure.text = "已抢"
                    holder.itemView.tvSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.w43))
                    holder.itemView.tvSure.setBackgroundResource(R.color.grayBtn)
                }
                getData(position).redPacketStatus == 0 -> {
                    //未抢
                    holder.itemView.tvSure.text = "去抢"
                    holder.itemView.tvSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.w43))
                    holder.itemView.tvSure.setBackgroundResource(R.color.red)
                }
                else -> {
                    holder.itemView.tvSure.text = "已抢光"
//                    holder.itemView.tvSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.w36))
                    holder.itemView.tvSure.setBackgroundResource(R.color.grayBtn)
                }
            }
        } else {
            holder.itemView.tvMoney.visibility = View.GONE
            holder.itemView.tvSure.setBackgroundResource(R.color.red)
            holder.itemView.tvSure.text = "爱心刮刮乐"
//            holder.itemView.tvSure.text = "爱心大转盘"
        }


        GlideUtils.load(context, getData(position).advertisementBanner, holder.itemView.ivPhoto)


    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(context).inflate(R.layout.layout_bonus_list_item, parent, false)
}