package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.ShouMeiBrandActivity
import com.cocosh.shmstore.home.ShoumeiDetailActivity
import com.cocosh.shmstore.home.model.SMCompanyData
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.mine.adapter.SpaceHItem
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.StringUtils
import kotlinx.android.synthetic.main.item_shoumei_t.view.*
import kotlinx.android.synthetic.main.item_shoumei_v.view.*

/**
 * 我的顶部导航适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiVAdapter(var type: Int, private var mList: ArrayList<SMCompanyData>, var mContext: Context, list: ArrayList<SMCompanyThemeData>) : BaseRecycleAdapter<SMCompanyThemeData>(list) {
    lateinit var adapter: ShouMeiHAdapter
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) =
            if (getItemViewType(position) == 1) {
                holder.itemView.hRecyclerView.layoutManager = LinearLayoutManager(mContext, OrientationHelper.HORIZONTAL, false) as RecyclerView.LayoutManager?
                if (holder.itemView.tag != position) {
                    holder.itemView.hRecyclerView.addItemDecoration(SpaceHItem(0, mContext.resources.getDimension(R.dimen.w40).toInt()))
                }

                if (mList.size == 0) {
                    holder.itemView.visibility = View.GONE
                } else {
                    holder.itemView.visibility = View.VISIBLE
                }
                adapter = ShouMeiHAdapter(type, mList)
                holder.itemView.hRecyclerView.adapter = adapter
                holder.itemView.tag = position
                adapter.setOnFollowClick(object : ShouMeiHAdapter.OnFollowClick {
                    override fun follow(data: Int) {
                        mOnFollowClick?.follow(1, data)
                    }
                })
            } else {
                list[position - 1].run {

                }
                with(list[position - 1]) {
                    GlideUtils.loadRound(1, mContext, bbs.logo, holder.itemView.ivLogo)
                    holder.itemView.tvName.text = bbs.company
                    holder.itemView.tvTime.text = StringUtils.timeStampFormatDateYYMMdd(posts.time?:"")
                    holder.itemView.tvDesc.text = posts.title       //帖子标题
                    holder.itemView.showNumber.text = posts.views   //浏览次数
                    holder.itemView.commentNumber.text = posts.sum.toString()  //评论数量
                    if (bbs.follow == "0") {
                        holder.itemView.tvStatus.text = "+关注"
                        holder.itemView.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                        holder.itemView.tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_red)
                    } else {
                        holder.itemView.tvStatus.text = "已关注"
                        holder.itemView.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.blackText))
                        holder.itemView.tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
                    }

                    //                if (isRead == "1") {
                    //                    holder.itemView.lookIcon.setTextColor(mContext.resources.getColor(R.color.red))
                    //                } else {
                    //                    holder.itemView.lookIcon.setTextColor(mContext.resources.getColor(R.color.grayText))
                    //                }

                    //                when (resCompanyHomeInfoVO?.userType) {
                    //                    "1" -> {
                    //                        holder.itemView.tvType.text = "企业主"
                    //                    }
                    //                    "2" -> {
                    //                        holder.itemView.tvType.text = "新媒人"
                    //                    }
                    //                    "3" -> {
                    //                        holder.itemView.tvType.text = "服务商"
                    //                    }
                    //                    "4" -> {
                    //                        holder.itemView.tvType.text = "用户"
                    //                    }
                    //                }
                    posts.images?.split(",")?.let {
                        if (it.isNotEmpty()) {
                            when {
                                it.size == 1 -> {
                                    holder.itemView.imageOne.visibility = View.VISIBLE
                                    holder.itemView.imageTwo.visibility = View.GONE
                                    holder.itemView.imageThree.visibility = View.GONE
                                    GlideUtils.loadPhoto(context, it[0], holder.itemView.imageOne, 0)
                                }

                                it.size == 2 -> {
                                    holder.itemView.imageOne.visibility = View.VISIBLE
                                    holder.itemView.imageTwo.visibility = View.VISIBLE
                                    holder.itemView.imageThree.visibility = View.GONE
                                    GlideUtils.loadPhoto(context, it[0], holder.itemView.imageOne, 0)
                                    GlideUtils.loadPhoto(context, it[1], holder.itemView.imageTwo, 0)
                                }
                                else -> {
                                    holder.itemView.imageOne.visibility = View.VISIBLE
                                    holder.itemView.imageTwo.visibility = View.VISIBLE
                                    holder.itemView.imageThree.visibility = View.VISIBLE

                                    GlideUtils.loadPhoto(context, it[0], holder.itemView.imageOne, 0)
                                    GlideUtils.loadPhoto(context, it[1], holder.itemView.imageTwo, 0)
                                    GlideUtils.loadPhoto(context, it[2], holder.itemView.imageThree, 0)

                                }
                            }

                        } else {
                            holder.itemView.imageOne.visibility = View.GONE
                            holder.itemView.imageTwo.visibility = View.GONE
                            holder.itemView.imageThree.visibility = View.GONE
                        }
                    }


                    holder.itemView.detailLL.setOnClickListener {
                        //新闻详情
                        val oldNumber = posts.views?.toInt()
                        val number = (oldNumber ?: 0+1)
                        posts.views = number.toString()
//                        isRead = "1"
                        notifyItemChanged(position - 1)
                        mOnFollowClick?.read(10, position - 1)
                        ShoumeiDetailActivity.start(context, posts.title ?: "", posts.url
                                ?: "", bbs.eid ?: "", posts.id
                                ?: "", getData(position - 1).bbs.follow
                                ?: "0", getData(position - 1).bbs.silence ?: "0")
                    }
                    holder.itemView.ivLogo.setOnClickListener {
                        //品牌专属论坛
                        ShouMeiBrandActivity.start(context, bbs)
                    }
                    holder.itemView.nameLl.setOnClickListener {
                        //品牌专属论坛
                        ShouMeiBrandActivity.start(context, bbs)
                    }
                    holder.itemView.tvStatus.setOnClickListener {
                        mOnFollowClick?.follow(2, position - 1)
                    }
                }
            }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 1) {
            return if (type == -1) {
                LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_t, parent, false)
            } else {
                LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_t_s, parent, false)
            }
        }
        return LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_v, parent, false)
    }

    override fun getItemCount(): Int = list.size + 1

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 1
        }
        return 2
    }

    /**
     * 1 company  2 themecompany
     */
    interface OnFollowClick {
        fun follow(type: Int?, themeCompanyIndex: Int?)
        fun read(type: Int?, themeCompanyIndex: Int?)
    }

    var mOnFollowClick: OnFollowClick? = null

    fun setOnFollowClick(mOnFollowClick: OnFollowClick) {
        this.mOnFollowClick = mOnFollowClick
    }

    fun hNotify() {
        adapter.notifyDataSetChanged()
    }

}