package com.cocosh.shmstore.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.ShoumeiDetailActivity
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.item_shoumei_brand.view.*

/**
 * 我的顶部导航适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiBrandAdapter(list: ArrayList<SMCompanyThemeData.SubCompanyTheme>) : BaseRecycleAdapter<SMCompanyThemeData.SubCompanyTheme>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        holder.itemView.tvTime.text = list[position].createTime
        holder.itemView.tvDesc.text = list[position].themeTitle
        holder.itemView.showNumber.text = list[position].readNumber
        holder.itemView.commentNumber.text = list[position].commentsNumber

        if (list[position].isTop == "-1") {
            holder.itemView.tvTop.visibility = View.VISIBLE
        } else {
            holder.itemView.tvTop.visibility = View.GONE
        }

        if (list[position].isRead == "1") {
            holder.itemView.lookIcon.setTextColor(context.resources.getColor(R.color.red))
        } else {
            holder.itemView.lookIcon.setTextColor(context.resources.getColor(R.color.grayText))
        }

        if (list[position].isEssence == "1") {
            holder.itemView.tvName.visibility = View.VISIBLE
        } else {
            holder.itemView.tvName.visibility = View.GONE
        }

        if (list[position].imageUrl != null && list[position].imageUrl?.size != 0) {
            if (list[position].imageUrl?.size == 1) {
                holder.itemView.imageOne.visibility = View.VISIBLE
                holder.itemView.imageTwo.visibility = View.INVISIBLE
                holder.itemView.imageThree.visibility = View.INVISIBLE
                GlideUtils.loadDefault(context, list[position].imageUrl!![0], holder.itemView.imageOne)
            }
            if (list[position].imageUrl?.size == 2) {
                holder.itemView.imageOne.visibility = View.VISIBLE
                holder.itemView.imageTwo.visibility = View.VISIBLE
                holder.itemView.imageThree.visibility = View.INVISIBLE
                GlideUtils.loadDefault(context, list[position].imageUrl!![0], holder.itemView.imageOne)
                GlideUtils.loadDefault(context, list[position].imageUrl!![1], holder.itemView.imageTwo)
            }
            if (list[position].imageUrl?.size == 3) {
                holder.itemView.imageOne.visibility = View.VISIBLE
                holder.itemView.imageTwo.visibility = View.VISIBLE
                holder.itemView.imageThree.visibility = View.VISIBLE
                GlideUtils.loadDefault(context, list[position].imageUrl!![0], holder.itemView.imageOne)
                GlideUtils.loadDefault(context, list[position].imageUrl!![1], holder.itemView.imageTwo)
                GlideUtils.loadDefault(context, list[position].imageUrl!![2], holder.itemView.imageThree)
            }

        } else {
            holder.itemView.imageOne.visibility = View.INVISIBLE
            holder.itemView.imageTwo.visibility = View.INVISIBLE
            holder.itemView.imageThree.visibility = View.INVISIBLE
        }
//        holder.itemView.detailLL.setOnClickListener {
        //新闻详情
//            ShoumeiDetailActivity.start(context)
//        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_brand, parent, false)
    }
}