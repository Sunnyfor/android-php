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
class ShouMeiBrandAdapter(list: ArrayList<SMCompanyThemeData>) : BaseRecycleAdapter<SMCompanyThemeData>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
//        holder.itemView.tvTime.text = list[position].createTime
        holder.itemView.tvDesc.text = list[position].posts.title
        holder.itemView.showNumber.text = list[position].posts.views
//        holder.itemView.commentNumber.text = list[position].commentsNumber

//        if (list[position].isTop == "-1") {
//            holder.itemView.tvTop.visibility = View.VISIBLE
//        } else {
//            holder.itemView.tvTop.visibility = View.GONE
//        }
//
//        if (list[position].isRead == "1") {
//            holder.itemView.lookIcon.setTextColor(context.resources.getColor(R.color.red))
//        } else {
//            holder.itemView.lookIcon.setTextColor(context.resources.getColor(R.color.grayText))
//        }
//
//        if (list[position].isEssence == "1") {
//            holder.itemView.tvName.visibility = View.VISIBLE
//        } else {
//            holder.itemView.tvName.visibility = View.GONE
//        }
        list[position].posts.images.let {
            if (it.isNotEmpty()) {
                if (it.size == 1) {
                    holder.itemView.imageOne.visibility = View.VISIBLE
                    holder.itemView.imageTwo.visibility = View.GONE
                    holder.itemView.imageThree.visibility = View.GONE
                    GlideUtils.load(context, it[0], holder.itemView.imageOne)
                }
                if (it.size == 2) {
                    holder.itemView.imageOne.visibility = View.VISIBLE
                    holder.itemView.imageTwo.visibility = View.VISIBLE
                    holder.itemView.imageThree.visibility = View.GONE
                    GlideUtils.load(context, it[0], holder.itemView.imageOne)
                    GlideUtils.load(context, it[1], holder.itemView.imageTwo)
                }
                if (it.size == 3) {
                    holder.itemView.imageOne.visibility = View.VISIBLE
                    holder.itemView.imageTwo.visibility = View.VISIBLE
                    holder.itemView.imageThree.visibility = View.VISIBLE
                    GlideUtils.load(context, it[0], holder.itemView.imageOne)
                    GlideUtils.load(context, it[1], holder.itemView.imageTwo)
                    GlideUtils.load(context, it[2], holder.itemView.imageThree)
                }

            } else {
                holder.itemView.imageOne.visibility = View.GONE
                holder.itemView.imageTwo.visibility = View.GONE
                holder.itemView.imageThree.visibility = View.GONE
            }
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