package com.cocosh.shmstore.newhome.fragment

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.adapter.GoodsListAdapter
import com.cocosh.shmstore.newhome.model.Goods
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_pull_refresh.view.*

class GoodsListFragment : BaseFragment() {

    var goodsList = arrayListOf<Goods>()
    var pager = "0"
    var nodeId = "0"
    var cateId = "0"
    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {
        getLayoutView().swipeRefreshLayout.recyclerView.layoutManager = GridLayoutManager(context, 2)
        val goodsAdapter = GoodsListAdapter(goodsList)

        goodsAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                startActivity(Intent(context, GoodsDetailActivity::class.java).putExtra("goods_id", goodsList[index].id))
            }
        })

        getLayoutView().swipeRefreshLayout.recyclerView.adapter = goodsAdapter
        getLayoutView().swipeRefreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pager = "0"
                loadData(nodeId,cateId)
            }

            override fun onLoadMore(page: Int) {
                pager = goodsList.last().id
            }
        }

    }

    override fun reTryGetData() {

    }

    override fun onListener(view: View) {

    }

    override fun close() {
    }

    fun loadData(node_id: String, cate_id: String) {
        nodeId = node_id
        cateId = cate_id
        val params = hashMapOf<String, String>()
        params["node_id"] = node_id
        params["cate_id"] = cate_id
        params["goods_id"] = pager
        params["num"] = "20"


        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_GOODS_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Goods>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Goods>>) {
                getLayoutView().swipeRefreshLayout.isRefreshing = false
                data.message?.let {
                    if (pager == "0") {
                        goodsList.clear()
                        goodsList.addAll(it)
                        getLayoutView().swipeRefreshLayout.update(it)
                        getLayoutView().swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                    } else {
                        getLayoutView().swipeRefreshLayout.loadMore(it)
                    }
                }

            }

            override fun onFailed(code: String, message: String) {
                if (pager == "0") {
                    goodsList.clear()
                    getLayoutView().swipeRefreshLayout.isRefreshing = false
                    getLayoutView().swipeRefreshLayout.update(arrayListOf<Goods>())
                    getLayoutView().swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                } else {
                    getLayoutView().swipeRefreshLayout.loadMore(arrayListOf<Goods>())
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<Goods>>) {
            }

        })
    }
}