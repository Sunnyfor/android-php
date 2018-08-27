package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.ShoumeiDetailActivity
import com.cocosh.shmstore.home.model.MessageType
import com.cocosh.shmstore.home.model.MsgModel
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.WebActivity
import com.cocosh.shmstore.mine.ui.mywallet.MoneyWaterActivity
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.OpenType
import kotlinx.android.synthetic.main.item_message_system_withdraw.view.*
import kotlinx.android.synthetic.main.item_message_system_text.view.*
import kotlinx.android.synthetic.main.item_message_system_text_and_image.view.*

/**
 * 系统消息适配器
 * Created by zhangye on 2018/3/13.
 */
class MessageSystemAdapter(var mContext: Context, list: ArrayList<MsgModel>) : BaseRecycleAdapter<MsgModel>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        if (getItemViewType(position) == 1) {
            holder.itemView.tvTime.text = getData(position).time
            holder.itemView.tvDesc.text = getData(position).body?.title
//            holder.itemView.tvTitle.text = getData(position).title
        }
        if (getItemViewType(position) == 2) {
            holder.itemView.tvTimeImage.text = getData(position).time
//            holder.itemView.tvTitleImage.text = getData(position).title
            holder.itemView.tvDescImage.text = getData(position).body?.title
//            GlideUtils.load(mContext, getData(position).resourcesUrl, holder.itemView.ivImage)
        }
        if (getItemViewType(position) == 3) {
            holder.itemView.tvTimeDraw.text = getData(position).time
//            holder.itemView.tvTitleDraw.text = getData(position).title
//            holder.itemView.tvMoney.text = getData(position).putForwardAmount
//            holder.itemView.tvBank.text = getData(position).putForwardBankName
//            holder.itemView.tvStartTime.text = getData(position).putForwardCreateDate
            holder.itemView.llEndTime.visibility = View.GONE
        }
        if (getItemViewType(position) == 4) {
            holder.itemView.tvTimeDraw.text = getData(position).time
//            holder.itemView.tvTitleDraw.text = getData(position).title
//            holder.itemView.tvMoney.text = getData(position).putForwardAmount
//            holder.itemView.tvBank.text = getData(position).putForwardBankName
//            holder.itemView.tvStartTime.text = getData(position).putForwardCreateDate
//            holder.itemView.tvEndTime.text = getData(position).putForwardSuccessDate
            holder.itemView.llEndTime.visibility = View.VISIBLE
        }
        if (getItemViewType(position) == 5) {
            holder.itemView.tvTime.text = getData(position).time
            holder.itemView.tvDesc.text = getData(position).body?.title
//            holder.itemView.tvTitle.text = getData(position).title
        }

        holder.itemView.setOnClickListener {
            if (getItemViewType(position) == 2) {
                //跳转详情页
//                WebActivity.start(mContext, OpenType.SysMessage.name, getData(position).associatedUrl,  getData(position).title)
            }
            if (getItemViewType(position) == 3 || getItemViewType(position) == 4) {
                //跳转财富明细
//                MoneyWaterActivity.start(mContext, Constant.TYPE_MY)
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View {
        if (viewType == 1) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_text, parent, false)
        }
        if (viewType == 2) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_text_and_image, parent, false)
        }
        if (viewType == 3) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_withdraw, parent, false)
        }
        if (viewType == 4) {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_withdraw, parent, false)
        }
        return LayoutInflater.from(parent.context).inflate(R.layout.item_message_system_text, parent, false)
    }


    override fun getItemViewType(position: Int): Int {
        if (list[position].kind == MessageType.TEXT_MESSAGE.type) {
            return 1
        }
        if (list[position].kind == MessageType.GRAPHIC_MESSAGE.type) {
            return 2
        }
        if (list[position].kind == MessageType.PUT_FORWARD_LAUNCH_MESSAGE.type) {
            return 3
        }
        if (list[position].kind == MessageType.PUT_FORWARD_TO_ACCOUNT_MESSAGE.type) {
            return 4
        }
        return 5
    }

}