package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.adapter.LogisticsAdapter
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
        logisticsRecyclerView.layoutManager = LinearLayoutManager(this)
        logisticsRecyclerView.adapter = LogisticsAdapter(this, arrayListOf<String>("1", "2", "3", "4", "5"))
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
    }

    override fun onListener(view: View) {
        when (view.id) {
            showLogisticsInfo.id -> {
                if (showDesc.visibility == View.VISIBLE) {
                    showDesc.visibility = View.GONE
                    return
                }
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
                        PayActivity.start(this, "343", "100","")
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
}