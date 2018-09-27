package com.cocosh.shmstore.home.adapter

import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.BonusRanking
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.layout_bonus_ranking_item.view.*

/**
 * 红包排行榜
 * Created by zhangye on 2018/4/24.
 */
class BonusRankingAdapter(bonusRanking: BonusRanking) : BaseRecycleAdapter<BonusRanking.Data>(bonusRanking.list?: arrayListOf()) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvNo.text = getData(position).rank
        holder.itemView.tvName.text = getData(position).nickname
        holder.itemView.tvMoney.text = (getData(position).amount + "元")

        if (getData(position).rank == "1" || getData(position).rank == "2" || getData(position).rank == "3") {
            holder.itemView.tvNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.w57))
            holder.itemView.tvNo.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            holder.itemView.tvNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.w49))
            holder.itemView.tvNo.setTextColor(ContextCompat.getColor(context, R.color.textGray))
        }

        getData(position).avatar?.let {
            if (it.isNotEmpty())
                GlideUtils.loadHead(context, it, holder.itemView.ivPhoto)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View =
            LayoutInflater.from(context).inflate(R.layout.layout_bonus_ranking_item, parent, false)
}