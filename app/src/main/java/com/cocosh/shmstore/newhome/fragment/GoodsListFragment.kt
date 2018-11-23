package com.cocosh.shmstore.newhome.fragment

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.adapter.GoodsListAdapter
import com.cocosh.shmstore.newhome.model.Goods
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_pull_refresh.view.*

class GoodsListFragment : BaseFragment() {

    var goodsList = arrayListOf<Goods>()
    var pager = "0"
    var cateId = "0"
    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {
        getLayoutView().swipeRefreshLayout.recyclerView.layoutManager = GridLayoutManager(context, 2)
        val goodsAdapter = GoodsListAdapter(goodsList)
        getLayoutView().swipeRefreshLayout.recyclerView.adapter = goodsAdapter
        getLayoutView().swipeRefreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pager = "0"
                loadData(cateId)
            }

            override fun onLoadMore(page: Int) {
                pager = goodsList.last().goods_id
            }
        }

    }

    override fun reTryGetData() {

    }

    override fun onListener(view: View) {

    }

    override fun close() {
    }

    fun loadData(cate_id: String) {
        cateId = cate_id
        val params = hashMapOf<String, String>()
        params["cate_id"] = cate_id
        params["goods_id"] = pager
        params["num"] = "20"
        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_GOODS_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Goods>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Goods>>) {
                getLayoutView().swipeRefreshLayout.isRefreshing = false
                data.message?.let {
                    if (pager == "0") {
                        goodsList.clear()
                    }
                    goodsList.addAll(it)
                    getLayoutView().swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                }

            }

            override fun onFailed(code: String, message: String) {
                getLayoutView().swipeRefreshLayout.isRefreshing = false
            }

            override fun onCatch(data: BaseBean<ArrayList<Goods>>) {
            }

        })
    }
}