package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.model.MsgModel
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.mine.ui.WebActivity
import com.cocosh.shmstore.mine.ui.mywallet.MoneyWaterActivity
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.OpenType
import kotlinx.android.synthetic.main.item_message_system_text.view.*
import kotlinx.android.synthetic.main.item_message_system_text_and_image.view.*
import kotlinx.android.synthetic.main.item_message_system_withdraw.view.*

/**
 * 系统消息适配器
 * Created by zhangye on 2018/3/13.
 */
class MessageSystemAdapter(var mContext: Context, list: ArrayList<MsgModel>) : BaseRecycleAdapter<MsgModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        //系统消息
        if (getItemViewType(position) == 0) {

            holder.itemView.tvTimeImage.text = getData(position).body?.time
            holder.itemView.tvTitleImage.text = getData(position).body?.title
            holder.itemView.tvDescImage.text = getData(position).body?.content
//
            if (getData(position).body?.image.isNullOrEmpty()) {
                holder.itemView.ivImage.visibility = View.GONE
            } else {
                holder.itemView.ivImage.visibility = View.VISIBLE
                GlideUtils.load(mContext, getData(position).body?.image, holder.itemView.ivImage)
            }
        }

        //提现发起
        if (getItemViewType(position) == 1) {
            holder.itemView.tvTimeDraw.text = getData(position).time
            holder.itemView.tvTitleDraw.text = "提现发起"
            holder.itemView.tvMoney.text = getData(position).body?.amount
            holder.itemView.tvBank.text = getData(position).body?.bankname
            holder.itemView.tvStartTime.text = getData(position).body?.time
            holder.itemView.llEndTime.visibility = View.GONE
        }

        if (getItemViewType(position) == 2) {
            holder.itemView.tvTimeDraw.text = getData(position).time
            holder.itemView.tvTitleDraw.text = "提现到账"
            holder.itemView.tvMoney.text = getData(position).body?.amount
            holder.itemView.tvBank.text = getData(position).body?.bankname
            holder.itemView.tvStartTime.text = getData(position).body?.time
            holder.itemView.tvEndTime.text = getData(position).body?.actual_time
            holder.itemView.llEndTime.visibility = View.VISIBLE
        }

        //核审结果
        if (getItemViewType(position) == 3 || getItemViewType(position) == 4) {
            holder.itemView.tvTime.text = getData(position).time
            holder.itemView.tvDesc.text = getData(position).body?.reason
            if (getItemViewType(position) == 3) {
                holder.itemView.tvTitle.text = "服务商审核结果"
            } else {
                holder.itemView.tvTitle.text = "新媒人审核结果"
            }
        }


        holder.itemView.setOnClickListener {
            if (getItemViewType(position) == 0) {
                //跳转详情页
                if ((getData(position).ext?.url?:"").isNotEmpty()){
                    WebActivity.start(mContext, OpenType.SysMessage.name, getData(position).ext?.url, getData(position).body?.title)
                }
            }
            if (getItemViewType(position) == 1 || getItemViewType(position) == 2) {
                //跳转财富明细
                MoneyWaterActivity.start(mContext, Constant.TYPE_MY)
            }

            if (getItemViewType(position) == 3 || getItemViewType(position) == 4) {
                AuthActivity.start(context)
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 1 || viewType == 2) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_withdraw, parent, false)
        }
        if (viewType == 3 || viewType == 4) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_text, parent, false)
        }

        return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_text_and_image, parent, false)
    }


    override fun getItemViewType(position: Int): Int = list[position].kind?.toInt() ?: 0

}