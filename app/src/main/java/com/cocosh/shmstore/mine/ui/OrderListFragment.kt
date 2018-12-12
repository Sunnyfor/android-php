package com.cocosh.shmstore.mine.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.OrderListAdapter
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.newCertification.ui.PayActivity
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.layout_pull_refresh.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 订单
 * Created by lmg on 2018/4/20.
 */
class OrderListFragment : BaseFragment() {

    var type: String? = null
    var dataList = arrayListOf<Order>()
    var pageindex = "0"
    lateinit var adapter: OrderListAdapter

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.layout_pull_refresh
    override fun initView() {

        EventBus.getDefault().register(this)

        val bundle = arguments//从activity传过来的Bundle
        type = bundle?.getString("TYPE", "全部")

        adapter = OrderListAdapter(getBaseActivity(),dataList)
        getLayoutView().swipeRefreshLayout.recyclerView.adapter = adapter
        getLayoutView().swipeRefreshLayout.recyclerView.layoutManager = LinearLayoutManager(activity)
//        getLayoutView().recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h40).toInt(), resources.getDimension(R.dimen.h40).toInt()))
        getLayoutView().swipeRefreshLayout.onRefreshResult = object :SMSwipeRefreshLayout.OnRefreshResult{
            override fun onUpdate(page: Int) {
                pageindex = "0"
                loadData()
            }

            override fun onLoadMore(page: Int) {
                pageindex = dataList.last().order_sn
                loadData()
            }

        }

        loadData()
    }



    override fun close() {
        EventBus.getDefault().unregister(this)
    }

    override fun onListener(view: View) {

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
            else -> "3"  //交易完成
        }
        if (status != "") {
            params["status"] = status
        }
        if (pageindex != "0"){
            params["order_sn"] = pageindex
        }
        params["num"] = "20"
        ApiManager2.post(getBaseActivity(), params, Constant.ORDER_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Order>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Order>>) {
                data.message?.let {
                    if (pageindex == "0"){
                        dataList.clear()
                        dataList.addAll(it)
                        adapter.notifyDataSetChanged()
                        getLayoutView().swipeRefreshLayout.update(it)
                    }else{
                        getLayoutView().swipeRefreshLayout.loadMore(it)
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
                if (pageindex == "0" ){
                    dataList.clear()
                    adapter.notifyDataSetChanged()
                    getLayoutView().swipeRefreshLayout.update(null)
                }else{
                    getLayoutView().swipeRefreshLayout.loadMore<Order>(arrayListOf())
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<Order>>) {

            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OrderEnvent(order: Order){
        pageindex = "0"
        loadData()
    }
}