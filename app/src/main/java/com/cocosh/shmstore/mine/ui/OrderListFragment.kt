package com.cocosh.shmstore.mine.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.mine.adapter.OrderListAdapter
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.newCertification.ui.PayActivity
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.fragment_order_list.view.*


/**
 * 订单
 * Created by lmg on 2018/4/20.
 */
class OrderListFragment : BaseFragment() {
    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.fragment_order_list
    override fun initView() {
        val bundle = arguments//从activity传过来的Bundle
        val type = bundle?.getString("TYPE", "全部")
        when (type) {
            "全部" -> {
                getLayoutView().recyclerView.adapter = OrderListAdapter((activity as OrderListActivity).list)
            }
            "待付款" -> {
                val datas = arrayListOf<String>()
                (activity as OrderListActivity).list.forEach {
                    if (it == "待付款") {
                        datas.add(it)
                    }
                }
                getLayoutView().recyclerView.adapter = OrderListAdapter(datas)
                getLayoutView().recyclerView.adapter.notifyDataSetChanged()
            }
            "待发货" -> {
                val datas = arrayListOf<String>()
                (activity as OrderListActivity).list.forEach {
                    if (it == "待发货") {
                        datas.add(it)
                    }
                }
                getLayoutView().recyclerView.adapter = OrderListAdapter(datas)
                getLayoutView().recyclerView.adapter.notifyDataSetChanged()
            }
            "待收货" -> {
                val datas = arrayListOf<String>()
                (activity as OrderListActivity).list.forEach {
                    if (it == "待收货") {
                        datas.add(it)
                    }
                }
                getLayoutView().recyclerView.adapter = OrderListAdapter(datas)
                getLayoutView().recyclerView.adapter.notifyDataSetChanged()
            }
            "交易完成" -> {
                val datas = arrayListOf<String>()
                (activity as OrderListActivity).list.forEach {
                    if (it == "交易完成") {
                        datas.add(it)
                    }
                }
                getLayoutView().recyclerView.adapter = OrderListAdapter(datas)
                getLayoutView().recyclerView.adapter.notifyDataSetChanged()
            }
            else -> {
            }
        }

        getLayoutView().recyclerView.layoutManager = LinearLayoutManager(activity)
//        getLayoutView().recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h40).toInt(), resources.getDimension(R.dimen.h40).toInt()))
        initListener()
    }

    private fun initListener() {
        (getLayoutView().recyclerView.adapter as OrderListAdapter).setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                var type = ""
                when ((getLayoutView().recyclerView.adapter as OrderListAdapter).getData(index)) {
                    "待付款" -> {
                        type = CommonType.ORDER_NOPAY.type
                    }
                    "待发货" -> {
                        type = CommonType.ORDER_NOSEND.type
                    }
                    "待收货" -> {
                        type = CommonType.ORDER_GET.type
                    }
                    "交易完成" -> {
                        type = CommonType.ORDER_FINISH.type
                    }
                    else -> {
                    }
                }
                OrderDetailActivity.start(activity, type)
            }
        })
        (getLayoutView().recyclerView.adapter as OrderListAdapter).setOnBtnClickListener(object : OrderListAdapter.OnBtnClickListener {
            override fun payBtn(data: String, position: Int) {
                PayActivity.start(activity, "222", "120","")
            }

            override fun deleteBtn(data: String, position: Int) {
                showRealDialog("您确定删除订单吗？")
            }

            override fun realBtn(data: String, position: Int) {
                showRealDialog("您确定已收到商品？")
            }

            override fun serviceBtn(data: String, position: Int) {
                ContactServiceActivity.start(activity)
            }

            override fun lookBtn(data: String, position: Int) {
                OrderDetailActivity.start(activity, CommonType.ORDER_GET.type)
            }
        })
    }

    override fun close() {

    }

    override fun onListener(view: View) {

    }

    fun showRealDialog(content: String) {
        val dialog = SmediaDialog(activity)
        dialog.setTitle(content)
        dialog.show()
    }
}