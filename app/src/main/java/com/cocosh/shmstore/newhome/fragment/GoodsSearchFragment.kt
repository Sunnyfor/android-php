package com.cocosh.shmstore.newhome.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.adapter.GoodsSearchGoodsAdapter
import com.cocosh.shmstore.newhome.adapter.GoodsSearchShopAdapter
import com.cocosh.shmstore.newhome.model.Goods
import com.cocosh.shmstore.newhome.model.Shop
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_search.view.*

class GoodsSearchFragment : BaseFragment() {

    var keyword = ""
    val goodsList = arrayListOf<Goods>()
    val shopsList = arrayListOf<Shop>()
    var pager = "0"
    var type = false

    private val goodsSearchAdapter: GoodsSearchGoodsAdapter by lazy {
        GoodsSearchGoodsAdapter(goodsList)
    }

    private val shopSearchAdapter: GoodsSearchShopAdapter by lazy {
        GoodsSearchShopAdapter(shopsList)
    }

    override fun setLayout(): Int = R.layout.fragment_search
    override fun initView() {
        getLayoutView().refreshLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        getLayoutView().refreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pager = "0"
                if (type) {
                    searchShop(keyword)
                } else {
                    searchGoods(keyword)
                }
            }

            override fun onLoadMore(page: Int) {
                if (type) {
                    searchShop(keyword)
                    pager = goodsList.last().id
                } else {
                    pager = goodsList.last().id
                    searchGoods(keyword)
                }

            }
        }

        goodsSearchAdapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, index: Int) {
                GoodsDetailActivity.start(context,goodsList[index].name,goodsList[index].id)
            }
        })

    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }

    fun searchShop(keyword: String) {
        this.type = true
        this.keyword = keyword
        val params = hashMapOf<String, String>()
        params["words"] = keyword
        if (pager != "0") {
            params["store_id"] = pager
        }
        params["num"] = "20"

        if (pager == "0") {
            getLayoutView().refreshLayout.recyclerView.adapter = shopSearchAdapter
        }

        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_SEARCH_STORE, object : ApiManager2.OnResult<BaseBean<ArrayList<Shop>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Shop>>) {
                if (pager == "0") {
                    shopsList.clear()
                    shopsList.addAll(data.message ?: arrayListOf())
                    getLayoutView().refreshLayout.recyclerView.adapter.notifyDataSetChanged()
                    getLayoutView().refreshLayout.update(shopsList)
                } else {
                    getLayoutView().refreshLayout.loadMore(shopsList)
                }

            }

            override fun onFailed(code: String, message: String) {
                if (pager == "0") {
                    getLayoutView().refreshLayout.isRefreshing = false
                    getLayoutView().refreshLayout.update(shopsList)
                } else {
                    getLayoutView().refreshLayout.loadMore(shopsList)
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<Shop>>) {
            }

        })
    }


    fun searchGoods(keyword: String) {
        this.type = false
        this.keyword = keyword
        val params = hashMapOf<String, String>()
        params["words"] = keyword
        if (pager != "0") {
            params["goods_id"] = pager
        }
        params["num"] = "20"

        if (pager == "0") {
            getLayoutView().refreshLayout.recyclerView.adapter = goodsSearchAdapter
        }
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