package com.cocosh.shmstore.home.adapter

import android.content.Context
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
import kotlinx.android.synthetic.main.item_shoumei_t.view.*
import kotlinx.android.synthetic.main.item_shoumei_v.view.*

/**
 * 我的顶部导航适配器
 * Created by zhangye on 2018/3/13.
 */
class ShouMeiVAdapter(var type: Int, var mList: ArrayList<SMCompanyData>, var mContext: Context, list: ArrayList<SMCompanyThemeData.SubCompanyTheme>) : BaseRecycleAdapter<SMCompanyThemeData.SubCompanyTheme>(list) {
    lateinit var adapter: ShouMeiHAdapter
    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
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
                GlideUtils.loadRound(1, mContext, resCompanyHomeInfoVO?.forumHeadImg, holder.itemView.ivLogo)
                holder.itemView.tvName.text = resCompanyHomeInfoVO?.forumName
                holder.itemView.tvTime.text = createTime
                holder.itemView.tvDesc.text = themeTitle
                holder.itemView.showNumber.text = readNumber
                holder.itemView.commentNumber.text = commentsNumber
                if (resCompanyHomeInfoVO?.followStatus == "0") {
                    holder.itemView.tvStatus.text = "+关注"
                    holder.itemView.tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_red)
                } else {
                    holder.itemView.tvStatus.text = "已关注"
                    holder.itemView.tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
                }
                if (isRead == "1") {
                    holder.itemView.lookIcon.setTextColor(mContext.resources.getColor(R.color.red))
                } else {
                    holder.itemView.lookIcon.setTextColor(mContext.resources.getColor(R.color.grayText))
                }

                when (resCompanyHomeInfoVO?.userType) {
                    "1" -> {
                        holder.itemView.tvType.text = "企业主"
                    }
                    "2" -> {
                        holder.itemView.tvType.text = "新媒人"
                    }
                    "3" -> {
                        holder.itemView.tvType.text = "服务商"
                    }
                    "4" -> {
                        holder.itemView.tvType.text = "用户"
                    }
                }

                if (imageUrl != null && imageUrl?.size!! > 0) {
                    when {
                        imageUrl?.size == 1 -> {
                            holder.itemView.imageOne.visibility = View.VISIBLE
                            holder.itemView.imageTwo.visibility = View.GONE
                            holder.itemView.imageThree.visibility = View.GONE
                            GlideUtils.load(context, imageUrl!![0], holder.itemView.imageOne)
                        }

                        imageUrl?.size == 2 -> {
                            holder.itemView.imageOne.visibility = View.VISIBLE
                            holder.itemView.imageTwo.visibility = View.VISIBLE
                            holder.itemView.imageThree.visibility = View.GONE
                            GlideUtils.load(context, imageUrl!![0], holder.itemView.imageOne)
                            GlideUtils.load(context, imageUrl!![1], holder.itemView.imageTwo)
                        }
                        else -> {
                            holder.itemView.imageOne.visibility = View.VISIBLE
                            holder.itemView.imageTwo.visibility = View.VISIBLE
                            holder.itemView.imageThree.visibility = View.VISIBLE
                            GlideUtils.load(context, imageUrl!![0], holder.itemView.imageOne)
                            GlideUtils.load(context, imageUrl!![1], holder.itemView.imageTwo)
                            GlideUtils.load(context, imageUrl!![2], holder.itemView.imageThree)
                        }
                    }

                } else {
                    holder.itemView.imageOne.visibility = View.GONE
                    holder.itemView.imageTwo.visibility = View.GONE
                    holder.itemView.imageThree.visibility = View.GONE
                }
                holder.itemView.detailLL.setOnClickListener {
                    //新闻详情
                    var oldNumber = readNumber?.toInt()
                    var number = (oldNumber ?: 1) + 1
                    readNumber = number.toString()
                    isRead = "1"
                    notifyItemChanged(position)
                    mOnFollowClick?.read(10, position - 1)
                    ShoumeiDetailActivity.start(context, resCompanyHomeInfoVO?.followStatus
                            ?: "", resCompanyHomeInfoVO?.isBlack
                            ?: "", idCompanyHomeTheme
                            ?: "", themePageUrl
                            ?: "", resCompanyHomeInfoVO?.idCompanyHomeBaseInfo ?: "")
                }
                holder.itemView.ivLogo.setOnClickListener {
                    //品牌专属论坛
                    ShouMeiBrandActivity.start(context, resCompanyHomeInfoVO?.idCompanyHomeBaseInfo
                            ?: "")
                }
                holder.itemView.nameLl.setOnClickListener {
                    //品牌专属论坛
                    ShouMeiBrandActivity.start(context, resCompanyHomeInfoVO?.idCompanyHomeBaseInfo
                            ?: "")
                }
                holder.itemView.tvStatus.setOnClickListener {
                    mOnFollowClick?.follow(2, position - 1)
                }
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 1) {
            if (type == -1) {
                return LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_t, parent, false)
            } else {
                return LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_t_s, parent, false)
            }
        }
        return LayoutInflater.from(parent.context).inflate(R.layout.item_shoumei_v, parent, false)
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

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