package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.LogisticsAdapter
import com.cocosh.shmstore.mine.model.OrderDetail
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.newCertification.ui.PayActivity
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_order_detail.*

/**
 * Created by lmg on 2018/4/25.
 * 订单详情
 */
class OrderDetailActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_order_detail

    override fun initView() {
        titleManager.defaultTitle("订单详情")
        showLogisticsInfo.setOnClickListener(this)
        cancelOrder.setOnClickListener(this)
        pay.setOnClickListener(this)
        initData()
    }

    lateinit var type: String
    private fun initData() {
        type = intent.getStringExtra("ORDER_TYPE")
        when (type) {
            CommonType.ORDER_NOPAY.type -> {
                cancelOrder.visibility = View.VISIBLE
                pay.visibility = View.VISIBLE
                orderInfoLl.visibility = View.VISIBLE
                hint.visibility = View.GONE
                orderInfoShowLl.visibility = View.GONE
                logisticsLL.visibility = View.GONE
            }
            CommonType.ORDER_NOSEND.type -> {
                cancelOrder.visibility = View.VISIBLE
                pay.visibility = View.GONE
                orderInfoLl.visibility = View.GONE
                hint.visibility = View.VISIBLE
                orderInfoShowLl.visibility = View.VISIBLE
                logisticsLL.visibility = View.GONE
                cancelOrder.text = "联系客服"
            }
            CommonType.ORDER_GET.type -> {
                cancelOrder.visibility = View.VISIBLE
                pay.visibility = View.VISIBLE
                orderInfoLl.visibility = View.GONE
                hint.visibility = View.VISIBLE
                orderInfoShowLl.visibility = View.VISIBLE
                logisticsLL.visibility = View.VISIBLE
                cancelOrder.text = "联系客服"
                pay.text = "确认收货"
            }
            CommonType.ORDER_FINISH.type -> {
                time.visibility = View.GONE
                cancelOrder.visibility = View.VISIBLE
                pay.visibility = View.GONE
                orderInfoLl.visibility = View.GONE
                hint.visibility = View.VISIBLE
                orderInfoShowLl.visibility = View.VISIBLE
                logisticsLL.visibility = View.VISIBLE
                cancelOrder.text = "删除订单"
            }
            else -> {
            }
        }

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
            cancelOrder.id -> {
                when (type) {
                    CommonType.ORDER_NOPAY.type -> {
                        //取消订单
                        showRealDialog("您确定要取消订单吗？")
                    }
                    CommonType.ORDER_NOSEND.type -> {
                        // 联系客服
                        ContactServiceActivity.start(this)
                    }
                    CommonType.ORDER_GET.type -> {
                        // 联系客服
                        ContactServiceActivity.start(this)
                    }
                    CommonType.ORDER_FINISH.type -> {
                        //删除订单
                        showRealDialog("您确定删除订单吗？")
                    }
                    else -> {
                    }
                }
            }
            pay.id -> {
                when (type) {
                    CommonType.ORDER_NOPAY.type -> {
                        //付款
                        PayActivity.start(this, "343", "100", "")
                    }

                    CommonType.ORDER_GET.type -> {
                        // 确认收货
                        showRealDialog("您确定已收到商品？")
                    }
                    else -> {
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context, type: String) {
            mContext.startActivity(Intent(mContext, OrderDetailActivity::class.java).putExtra("ORDER_TYPE", type))
        }
    }

    fun showRealDialog(content: String) {
        val dialog = SmediaDialog(this)
        dialog.setTitle(content)
        dialog.show()
    }


    fun loadData() {
        val patams = HashMap<String, String>()
        patams["order_id"] = "10"
        ApiManager2.get(1, this, patams, Constant.ORDER_DETAILS, object : ApiManager2.OnResult<BaseBean<OrderDetail>>() {
            override fun onSuccess(data: BaseBean<OrderDetail>) {
                data.message?.let {
                    orderNumber.text = it.sn
                    orderPlaceTime.text = it.addtime
                    buyer.text = ("${it.receiver}  ${it.receiver_phone}")
                    shopName.text = it.name
                    goodsPrice.text = ("￥${it.price}")
                    Glide.with(this@OrderDetailActivity).load(it.image).placeholder(R.drawable.default_content).into(goodsPic)

                    logisticsWay.text = it.express?.company
                    logisticsName.text = it.express?.com
                    logisticsNumber.text = it.express?.no

                    it.express?.list?.let {
                        logisticsRecyclerView.adapter = LogisticsAdapter(this@OrderDetailActivity,it)
                        logisticsRecyclerView.layoutManager = LinearLayoutManager(this@OrderDetailActivity)
                        logisticsRecyclerView.setHasFixedSize(true)
                    }

                    showNumber.text = it.sn
                    showWaterNumber.text = it.sn //流水号
                    showPayWay.text = when (it.pay_type) {
                        "1" -> "支付宝"
                        "2" -> "微信"
                        else -> "首媒支付"
                    }
                    showPlaceTime.text = it.addtime
                    showPayTime.text = it.pay_time
                    showStartTime.text = it.shipping_time

                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<OrderDetail>) {

            }
        })

    }

    }