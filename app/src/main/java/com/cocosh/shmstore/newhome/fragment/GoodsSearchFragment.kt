package com.cocosh.shmstore.newhome.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.adapter.GoodsSearchAdapter
import com.cocosh.shmstore.newhome.model.Goods
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_search.view.*

class GoodsSearchFragment : BaseFragment() {

    var keyword = ""
    val goodsList = arrayListOf<Goods>()
    var pager = "0"
    var type = true

    override fun setLayout(): Int = R.layout.fragment_search
    override fun initView() {
        getLayoutView().refreshLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        getLayoutView().refreshLayout.recyclerView.adapter = GoodsSearchAdapter(goodsList)

        getLayoutView().refreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pager = "0"
                searchGoods(keyword,type)
            }

            override fun onLoadMore(page: Int) {
                pager = goodsList.last().goods_id
                searchGoods(keyword,type)
            }
        }
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }

    fun searchGoods(keyword: String, type:Boolean) {
        this.keyword = keyword
        this.type = type
        val params = hashMapOf<String, String>()
        params["words"] = keyword
        if (pager != "0") {
            params["goods_id"] = pager
        }
        params["num"] = "20"

        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_GOODS_SEARCH, object : ApiManager2.OnResult<BaseBean<ArrayList<Goods>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Goods>>) {
                if (pager == "0") {
                    goodsList.clear()
                    goodsList.addAll(data.message ?: arrayListOf())
                    getLayoutView().refreshLayout.recyclerView.adapter.notifyDataSetChanged()
                    getLayoutView().refreshLayout.update(goodsList)
                } else {
                    getLayoutView().refreshLayout.loadMore(goodsList)
                }

            }

            override fun onFailed(code: String, message: String) {
                if (pager == "0") {
                    getLayoutView().refreshLayout.isRefreshing = false
                    getLayoutView().refreshLayout.update(goodsList)
                } else {
                    getLayoutView().refreshLayout.loadMore(goodsList)
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<Goods>>) {
            }

        })
    }
}