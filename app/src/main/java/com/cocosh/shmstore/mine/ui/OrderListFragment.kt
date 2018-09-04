package com.cocosh.shmstore.mine.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.OrderListAdapter
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.newCertification.ui.PayActivity
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.fragment_order_list.view.*


/**
 * 订单
 * Created by lmg on 2018/4/20.
 */
class OrderListFragment : BaseFragment() {

    var type: String? = null
    var dataList = arrayListOf<Order>()
    lateinit var adapter: OrderListAdapter

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.fragment_order_list
    override fun initView() {
        val bundle = arguments//从activity传过来的Bundle
        type = bundle?.getString("TYPE", "全部")

        adapter = OrderListAdapter(dataList)
        getLayoutView().recyclerView.adapter = adapter
        getLayoutView().recyclerView.layoutManager = LinearLayoutManager(activity)
//        getLayoutView().recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h40).toInt(), resources.getDimension(R.dimen.h40).toInt()))
        initListener()
        loadData()
    }

    private fun initListener() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                var type = ""
                when (dataList[index].status) {
                    "0" -> {
                        type = CommonType.ORDER_NOPAY.type
                    }
                    "1" -> {
                        type = CommonType.ORDER_NOSEND.type
                    }
                    "2" -> {
                        type = CommonType.ORDER_GET.type
                    }
                    "5" -> {
                        type = CommonType.ORDER_FINISH.type
                    }
                    else -> {
                    }
                }
                OrderDetailActivity.start(activity, type)
            }
        })

        adapter.setOnBtnClickListener(object : OrderListAdapter.OnBtnClickListener {
            override fun payBtn(data: String, position: Int) {
                PayActivity.start(activity, "222", "120", "")
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

    fun loadData() {
        val params = hashMapOf<String, String>()
        val status = when (type) {
            "全部" -> {
                ""
            }
            "待付款" -> {
                "0"
            }
            "待发货" -> {
                "1"
            }
            "待收货" -> {
                "2"
            }
            else -> "5"  //交易完成
        }
        if (status != "") {
            params["status"] = status
        }
        ApiManager2.post(getBaseActivity(), params, Constant.ORDER_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Order>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Order>>) {
                data.message?.let {
                    dataList.clear()
                    dataList.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ArrayList<Order>>) {

            }

        })
    }
}