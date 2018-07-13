package com.cocosh.shmstore.mine.adapter

import android.content.Intent
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.home.BonusWebActivity
import com.cocosh.shmstore.home.SendBonusActivity
import com.cocosh.shmstore.home.model.SendBonus
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.ui.authentication.PackagePushInfoActivity
import com.cocosh.shmstore.newCertification.ui.PayActivity
import kotlinx.android.synthetic.main.item_red_package.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 适配器
 * Created by zhangye on 2018/3/13.
 */
class SendRedPackageListAdapter(list: ArrayList<SendBonus.Bonus>) : BaseRecycleAdapter<SendBonus.Bonus>(list) {
    var array = SparseArray<BaseRecycleViewHolder>()
    var onCancleReleaseListener: OnCancleReleaseListener? = null
    var format = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

    //10 待付款 11 交易关闭 20审核中 30投放中 21被驳回 40 红包已抢完 50结束 51退款

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {
        array.put(position, holder)

        holder.itemView.orderStatus.text = when (getData(position).orderStatus) {   //付款类型
            "10" -> "待付款 "
            "11" -> "交易关闭"
            "20" -> "审核中"
            "30", "40", "50", "51" -> "已投放"
            "21" -> "被驳回"
            else -> {
                "未知"
            }
        }

        if (getData(position).orderStatus == "10" || getData(position).orderStatus == "11") {
            holder.itemView.cancel.visibility = View.VISIBLE
            holder.itemView.pay.visibility = View.VISIBLE
            holder.itemView.timeLeave.visibility = View.VISIBLE
            holder.itemView.timeLeave.text = timeParse(getData(position).endTimeStamp)
//            holder.itemView.timeLeave.text =  format.format(getData(position).endTimeStamp)

            holder.itemView.createNumberLL.visibility = View.GONE
            holder.itemView.payWeyLL.visibility = View.GONE
            holder.itemView.payTimeLL.visibility = View.GONE

            holder.itemView.pay.setOnClickListener {
                val intent = Intent(context, PayActivity::class.java)
                intent.putExtra("amount", getData(position).redPacketMoney)
                intent.putExtra("runningNumber", getData(position).orderNumber)
                intent.putExtra("payOperatStatus", AuthenStatus.SEND_RED_PACKET.type)
                context.startActivity(intent)
            }

            holder.itemView.cancel.setOnClickListener {
                onCancleReleaseListener?.onCancleRelease(getData(position))
            }

            if (getData(position).endTimeStamp <= 0 || getData(position).orderStatus == "11") {
                holder.itemView.timeLeave.visibility = View.GONE
                holder.itemView.cancel.visibility = View.GONE
                holder.itemView.pay.visibility = View.GONE
            }
        } else {
            holder.itemView.timeLeave.visibility = View.GONE
            holder.itemView.cancel.visibility = View.GONE
            holder.itemView.pay.visibility = View.GONE
            holder.itemView.createNumberLL.visibility = View.VISIBLE
            holder.itemView.payWeyLL.visibility = View.VISIBLE
            holder.itemView.payTimeLL.visibility = View.VISIBLE
            holder.itemView.createNumber.text = getData(position).redPacketOrderRunningNum
            holder.itemView.payWey.text = when (getData(position).redPacketOrderPayType) {   //付款类型
                20 -> "首媒支付 "
                50 -> "支付宝支付"
                40 -> "微信支付"
                else -> {
                    "未知"
                }
            }
        }

        if (getData(position).orderStatus == "30" || getData(position).orderStatus == "40" || getData(position).orderStatus == "50" || getData(position).orderStatus == "51") {
            holder.itemView.btnData.visibility = View.VISIBLE
            holder.itemView.btnData.setOnClickListener {
                val intent = Intent(context, PackagePushInfoActivity::class.java)
                intent.putExtra("redPacketOrderId", getData(position).redPacketOrderId)
                context.startActivity(intent)
            }

            if (getData(position).orderStatus == "40" || getData(position).orderStatus == "50" || getData(position).orderStatus == "51" ) {
                holder.itemView.viewNone.visibility = View.VISIBLE
                if (getData(position).orderStatus == "40") {
                    holder.itemView.viewNone.setBackgroundResource(R.mipmap.ic_bonus_none)
                } else if (getData(position).orderStatus == "50") {
                    holder.itemView.viewNone.setBackgroundResource(R.drawable.ic_bonus_over)
                } else if (getData(position).orderStatus == "51") {
                    holder.itemView.viewNone.setBackgroundResource(R.drawable.ic_bonus_back)
                }
            } else {
                holder.itemView.viewNone.visibility = View.GONE
            }

        } else {
            holder.itemView.btnData.visibility = View.GONE
            holder.itemView.viewNone.visibility = View.GONE
        }


        if (getData(position).orderStatus == "21") {
            holder.itemView.btnMotify.visibility = View.VISIBLE
            holder.itemView.rejectTimeLL.visibility = View.VISIBLE
            holder.itemView.rejectTime.text = getData(position).rejectTime
            holder.itemView.turnTime.text = getData(position).rejectTime //驳回时间
//            holder.itemView.turnReson.text = getData(position).reje  //驳回原因
            holder.itemView.turnTimeLL.visibility = View.VISIBLE
            holder.itemView.llayout_1.visibility = View.GONE

            holder.itemView.btnMotify.setOnClickListener {
                val intent = Intent(context, SendBonusActivity::class.java)
                context.startActivity(intent)
            }

        } else {
            holder.itemView.llayout_1.visibility = View.VISIBLE
            holder.itemView.turnTimeLL.visibility = View.GONE
            holder.itemView.btnMotify.visibility = View.GONE
            holder.itemView.rejectTimeLL.visibility = View.GONE
        }

        holder.itemView.createTime.text = getData(position).createTime



        holder.itemView.payTime.text = getData(position).redPayTime //付款时间
        holder.itemView.number.text = getData(position).orderNumber //订单编号
        holder.itemView.shopName.text = getData(position).redPacketName
        holder.itemView.pushTime.text = getData(position).redPacketStartTime //投放时间
        holder.itemView.pushLocation.text = getData(position).regionName ?: "全国" //投放位置
        holder.itemView.pushMoney.text = (getData(position).redPacketMoney + "元") //投放金额
        holder.itemView.pushTotal.text = (getData(position).redPacketCount.toString() + "个")
        holder.itemView.pushSingle.text = (getData(position).singleRedPacketAmount + "元")

        if (getData(position).isExtend) {
            holder.itemView.pushLocationLL.visibility = View.VISIBLE
            holder.itemView.btnShow.text = "收起"
            if (getData(position).orderStatus == "21") {
                holder.itemView.nomal_minLL.visibility = View.GONE
            }
        } else {
            holder.itemView.pushLocationLL.visibility = View.GONE
            holder.itemView.btnShow.text = "展开更多"
            if (getData(position).orderStatus == "21") {
                holder.itemView.nomal_minLL.visibility = View.VISIBLE
            }
        }


        holder.itemView.btnShow.setOnClickListener({
            getData(position).isExtend = !getData(position).isExtend
            if (getData(position).isExtend) {
                holder.itemView.btnShow.text = "收起"
                holder.itemView.pushLocationLL.visibility = View.VISIBLE
            } else {
                holder.itemView.btnShow.text = "展开更多"
                holder.itemView.pushLocationLL.visibility = View.GONE
            }
        })

        holder.itemView.look.setOnClickListener {

            val intent = Intent(context, BonusWebActivity::class.java)
            intent.putExtra("title", getData(position).redPacketName)
            intent.putExtra("typeInfo", "1")
            intent.putExtra("htmUrl", getData(position).htmlUrl)
            intent.putExtra("state", "PREVIEW")
            context.startActivity(intent)
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_red_package, parent, false)


    interface OnCancleReleaseListener {
        fun onCancleRelease(bonus: SendBonus.Bonus)
    }


    fun notifyTimeLeave(position: Int) {
//        array[position]?.itemView?.timeLeave?.text = format.format(getData(position).endTimeStamp)
        array[position]?.itemView?.timeLeave?.text = timeParse(getData(position).endTimeStamp)
    }

    private fun timeParse(time: Long): String {
        time.let {
            val hours = it % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
            val minutes = it % (1000 * 60 * 60) / (1000 * 60)
            val seconds = it % (1000 * 60) / 1000
            if (seconds < 10) {
                return "$hours:$minutes:0$seconds"
            }
            return "$hours:$minutes:$seconds"
        }
    }
}