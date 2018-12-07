package com.cocosh.shmstore.newhome.fragment

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

class ShopGoodsListFragment : BaseFragment() {

    var goodsList = arrayListOf<Goods>()

    var cateId = "0"
    var storeId = "0"
    var pager = "0"

    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {
        getLayoutView().swipeRefreshLayout.recyclerView.layoutManager = GridLayoutManager(context, 2)
        val goodsAdapter = GoodsListAdapter(goodsList)

        goodsAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                GoodsDetailActivity.start(context, goodsList[index].name, goodsList[index].id)
            }
        })

        getLayoutView().swipeRefreshLayout.recyclerView.adapter = goodsAdapter
        getLayoutView().swipeRefreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pager = "0"
                loadData(storeId, cateId)
            }

            override fun onLoadMore(page: Int) {
                pager = goodsList.last().id
                loadData(storeId, cateId)
            }
        }
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }


    fun loadData(storeId: String, cate_id: String) {
        this.storeId = storeId
        if (cate_id != this.cateId) {
            pager = "0"
        }
        cateId = cate_id
        val params = hashMapOf<String, String>()
        params["store_id"] = storeId
        params["cate_id"] = cate_id
        params["goods_id"] = pager
        params["num"] = "20"


        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_STORE_GOODS, object : ApiManager2.OnResult<BaseBean<ArrayList<Goods>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Goods>>) {
                getLayoutView().swipeRefreshLayout.isRefreshing = false
                if (pager == "0") {
                    goodsList.clear()
                    goodsList.addAll(data.message ?: arrayListOf())
                    getLayoutView().swipeRefreshLayout.update(data.message)
                    getLayoutView().swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                } else {
                    getLayoutView().swipeRefreshLayout.loadMore(data.message ?: arrayListOf())
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