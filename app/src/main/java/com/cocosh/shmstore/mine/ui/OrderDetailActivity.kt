package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.OrderListAdapter
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.mine.model.OrderDetail
import com.cocosh.shmstore.utils.DataCode
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.item_order_list.*

/**
 * Created by lmg on 2018/4/25.
 * 订单详情
 */
class OrderDetailActivity : BaseActivity() {

    override fun setLayout(): Int = R.layout.activity_order_detail

    override fun initView() {
        titleManager.defaultTitle("订单详情")
        showLogisticsInfo.setOnClickListener(this)

        initData()
    }
    var id = ""
    lateinit var status: String
    private fun initData() {
        status = intent.getStringExtra("status")
        id = intent.getStringExtra("id")

        SmApplication.getApp().getData<Order>(DataCode.ORDER_GOODS,true)?.let {
            val adapter = OrderListAdapter(this,arrayListOf(it),true)
            recyclerView.setHasFixedSize(false)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        var statusStr = ""

        when (status) {
            "0" -> {
                statusStr = "等待买家付款"
                logisticsLL.visibility = View.GONE
            }
            "1" -> {
                statusStr = "等待卖家发货"
                time.visibility = View.GONE
                logisticsLL.visibility = View.GONE

            }
            "2" -> {
                statusStr = "等待确认收货"
                time.visibility = View.GONE
                logisticsLL.visibility = View.VISIBLE

            }
            else -> {
                statusStr = "交易完成"
                time.visibility = View.GONE
                logisticsLL.visibility = View.VISIBLE
            }
        }

        txt_status.text = statusStr

        loadData()
    }

    override fun onListener(view: View) {
        when (view.id) {
            showLogisticsInfo.id -> {
                if (showDesc.visibility == View.VISIBLE) {
                    showLogisticsInfo.text = "展开物流信息"
                    showDesc.visibility = View.GONE
                    return
                }
                showLogisticsInfo.text = "收起∧"
                showDesc.visibility = View.VISIBLE
            }
        }
    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context, id: String, status: String) {
            mContext.startActivity(Intent(mContext, OrderDetailActivity::class.java)
                    .putExtra("status", status)
                    .putExtra("id", id))
        }
    }


    fun loadData() {
        val params = HashMap<String, String>()
        params["order_sn"] = id
        ApiManager2.post(1, this, params, Constant.ORDER_DETAILS, object : ApiManager2.OnResult<BaseBean<OrderDetail>>() {
            override fun onSuccess(data: BaseBean<OrderDetail>) {
                data.message?.let {
                    text_user_name.text = ("${it.recive.name}  ${it.recive.phone}")
                    address.text = (it.recive.province + it.recive.city + it.recive.town+ it.recive.more)

                    txt_money.text = ("￥${it.body.price}")
                    showStartTime.text = it.order.pay_time
//                    logisticsWay.text = it.express?.company
//                    logisticsName.text = it.express?.com
//                    logisticsNumber.text = it.express?.no

//                    it.express?.list?.let {
//                        logisticsRecyclerView.adapter = LogisticsAdapter(this@OrderDetailActivity, it)
//                        logisticsRecyclerView.layoutManager = LinearLayoutManager(this@OrderDetailActivity)
//                        logisticsRecyclerView.setHasFixedSize(true)
//                    }



                    showNumber.text = id
                    showWaterNumber.text = it.order.flowno //流水号
                    showPayWay.text = (it.body.price + "元")

                    text_deduction.text = when(it.body.discount_type){
                        "0" -> "无抵扣"
                        else -> "红包抵扣"
                    }

                    text_deduction_money.text = (it.body.discount + "元")

                    text_pay_money.text = (it.body.actual + "元")
                    txt_time.text = it.order.time
                    showStartTime.text = it.order.delivery_time

                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<OrderDetail>) {

            }
        })

    }

}