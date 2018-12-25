package com.cocosh.shmstore.mine.adapter

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.cocosh.shmstore.base.BaseRecycleViewHolder
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.mine.ui.OrderDetailActivity
import com.cocosh.shmstore.newCertification.ui.PayActivity
import com.cocosh.shmstore.newhome.Balance
import com.cocosh.shmstore.newhome.GoodsCommentActivity
import com.cocosh.shmstore.newhome.GoodsShoppingActivity
import com.cocosh.shmstore.newhome.adapter.OrderGoodsAdapter
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.item_create_order_shop.view.*
import kotlinx.android.synthetic.main.item_order_list.view.*
import org.greenrobot.eventbus.EventBus

/**
 * 订单
 * Created by lmg on 2018/3/13.
 */
class OrderListAdapter(var baseActivity: BaseActivity, list: ArrayList<Order>, private var isDesc: Boolean = false) : BaseRecycleAdapter<Order>(list) {

    override fun onBindViewHolder(holder: BaseRecycleViewHolder, position: Int) {

        holder.itemView.txt_let.visibility = View.GONE
        holder.itemView.txt_mid.visibility = View.GONE
        holder.itemView.txt_right.visibility = View.GONE

        holder.itemView.tvName.text = getData(position).store_name

        holder.itemView.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.itemView.recyclerView.setHasFixedSize(true)
        holder.itemView.recyclerView.isNestedScrollingEnabled = false
        holder.itemView.recyclerView.adapter = OrderGoodsAdapter(getData(position).list, isDesc,list[position].status).apply {
            order = this@OrderListAdapter.getData(position)
        }

        holder.itemView.txt_money.text = ("¥ " + getData(position).sum)

        holder.itemView.ll_shop.setOnClickListener {
            if (isDesc) {
                GoodsShoppingActivity.start(context, getData(position).store_name, getData(position).store_id)
            } else {
                SmApplication.getApp().setData(DataCode.ORDER, getData(position))
                OrderDetailActivity.start(context, getData(position).order_sn, getData(position).status)
            }
        }


        when (list[position].status) {
            "0" -> { //待付款
                select(holder.itemView.txt_let, position)
                cancel(holder.itemView.txt_mid, position)
                goPay(holder.itemView.txt_right, position)
                holder.itemView.tvStatus.text = "待付款"
            }
            "1" -> { //待发货
                select(holder.itemView.txt_right, position)
                holder.itemView.tvStatus.text = "待发货"
            }
            "2" -> { //待收货
                select(holder.itemView.txt_mid, position)
                confirmReceipt(holder.itemView.txt_right, position)
                holder.itemView.tvStatus.text = "待收货"
            }
            "3" -> {
                holder.itemView.tvStatus.text = "交易完成"
                if (getData(position).list[0].comment == 0){
                    comment(holder.itemView.txt_mid, position)
                }
                select(holder.itemView.txt_right, position)
            }
            "101" -> {
                holder.itemView.tvStatus.text = "交易取消"
                delete(holder.itemView.txt_right, position)
                select(holder.itemView.txt_mid, position)
            }
            else -> {
                select(holder.itemView.txt_right, position)
            }
        }
    }

    override fun setLayout(parent: ViewGroup, viewType: Int): View = LayoutInflater.from(parent.context).inflate(R.layout.item_order_list, parent, false)

    interface OnBtnClickListener {
        fun payBtn(data: String, position: Int)
        fun deleteBtn(data: String, position: Int)
        fun realBtn(data: String, position: Int)
        fun serviceBtn(data: String, position: Int)
        fun lookBtn(data: String, position: Int)
    }

    lateinit var mOnBtnClickListener: OnBtnClickListener

    fun setOnBtnClickListener(mOnBtnClickListener: OnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener
    }

    private fun showGrayBg(textView: TextView) {
        textView.visibility = View.VISIBLE
        textView.setTextColor(ContextCompat.getColor(context, R.color.grayText))
        textView.setBackgroundResource(R.drawable.shape_rectangle_gray_to_white)
    }

    private fun showRedBg(textView: TextView) {
        textView.visibility = View.VISIBLE
        textView.setTextColor(ContextCompat.getColor(context, R.color.white))
        textView.setBackgroundResource(R.drawable.shape_rectangle_round_red)
    }

    private fun goPay(textView: TextView, position: Int) {
        textView.apply {
            text = "付款"
            showRedBg(this)
            setOnClickListener {
                pay(position)
            }
        }

    }

    private fun delete(textView: TextView, position: Int) {
        textView.apply {
            text = "删除订单"
            showGrayBg(this)
            setOnClickListener { _ ->
                showRealDialog("您确定删除订单吗？")
                        .OnClickListener = View.OnClickListener {
                    option("3", getData(position).order_sn)
                }
            }
        }
    }


    private fun comment(textView: TextView, position: Int) {
        textView.apply {
            text = "评价"
            showGrayBg(this)
            setOnClickListener { _ ->
                SmApplication.getApp().setData(DataCode.ORDER_GOODS, getData(position).list)
                GoodsCommentActivity.start(context,getData(position).order_sn)
            }
        }
    }


    private fun cancel(textView: TextView, position: Int) {
        textView.apply {
            text = "取消订单"
            showGrayBg(this)
            setOnClickListener { _ ->
                showRealDialog("您确定取消订单吗？")
                        .OnClickListener = View.OnClickListener {
                    option("2", getData(position).order_sn)
                }
            }
        }
    }

    private fun select(textView: TextView, position: Int) {

        if (isDesc) {
            return
        }
        textView.apply {
            text = "查看订单"
            showGrayBg(this)
            setOnClickListener {
                SmApplication.getApp().setData(DataCode.ORDER, getData(position))
                OrderDetailActivity.start(context, getData(position).order_sn, getData(position).status)
            }
        }
    }


    private fun confirmReceipt(textView: TextView, position: Int) {
        textView.apply {
            text = "确认收货"
            showRedBg(this)
            setOnClickListener { _ ->
                showRealDialog("您确定已收到商品？").OnClickListener = View.OnClickListener {
                    option("1", getData(position).order_sn)
                }
            }
        }
    }

    private fun showRealDialog(content: String): SmediaDialog {
        val dialog = SmediaDialog(context)
        dialog.setTitle(content)
        dialog.show()
        return dialog
    }


    //订单操作
    private fun option(type: String, order_sn: String) {
        val params = HashMap<String, String>()
        params["type"] = type
        params["order_sn"] = order_sn
        ApiManager2.post(baseActivity, params, Constant.ESHOP_ORDER_UPDA, object : ApiManager2.OnResult<BaseBean<String>>() {

            override fun onSuccess(data: BaseBean<String>) {
                EventBus.getDefault().post(Order("", "", "", "", "", "", "", arrayListOf()))
                if (isDesc) {
                    baseActivity.finish()
                }
                EventBus.getDefault().post(Order("", "", "", "", "", "", "", arrayListOf()))
                ToastUtil.show("操作成功！")
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }


    private fun pay(position: Int) {
        val params = HashMap<String, String>()
        params["order_sn"] = getData(position).order_sn
        ApiManager2.get(baseActivity, params, Constant.ESHOP_RP_BALANCE, object : ApiManager2.OnResult<BaseBean<Balance>>() {
            override fun onSuccess(data: BaseBean<Balance>) {
                if (data.message?.is_rp == "0") {
                    val smediaDialog = SmediaDialog(context)
                    val actual = (data.message?.actual ?: "0").toFloat()
                    val discount = (data.message?.discount ?: "0").toFloat()
                    val rp = (data.message?.rp ?: "0").toFloat()
                    smediaDialog.setTitle("红包金额不足，需支付${actual + discount - rp}元")
                    smediaDialog.OnClickListener = View.OnClickListener {
                        update(position, (actual + discount - rp).toString())
                    }
                    smediaDialog.show()
                } else {
                    PayActivity.start(context, getData(position).order_sn, getData(position).sum, "6")
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<Balance>) {

            }

        })
    }

    //更新红包订单
    private fun update(position: Int, money: String) {
        baseActivity.showLoading()
        val params = HashMap<String, String>()
        params["order_sn"] = getData(position).order_sn
        ApiManager2.post(baseActivity, params, Constant.ESHOP_RP_UPDA, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                baseActivity.hideLoading()
                if (data.status == "200") {
                    getData(position).sum = money
                    notifyDataSetChanged()
                    PayActivity.start(context, getData(position).order_sn, money, "6")
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(code: String, message: String) {
                baseActivity.hideLoading()
            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}