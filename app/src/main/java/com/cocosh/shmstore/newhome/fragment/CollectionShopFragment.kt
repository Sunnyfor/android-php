package com.cocosh.shmstore.newhome.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsShoppingActivity
import com.cocosh.shmstore.newhome.adapter.CollectionShopAdapter
import com.cocosh.shmstore.newhome.model.CollectionShop
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_pull_refresh.*

class CollectionShopFragment: BaseFragment() {

    val pageCount = 20
    var pageNumber = "0"

    private var collectList = arrayListOf<CollectionShop>()

    private val collectionShopAdapter: CollectionShopAdapter by lazy {
        CollectionShopAdapter(collectList){
            cancel(it)
        }.apply {
            setOnItemClickListener(object :OnItemClickListener{
                override fun onItemClick(v: View, index: Int) {
                    GoodsShoppingActivity.start(context,collectList[index].name,collectList[index].id)
                }

            })
        }
    }

    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {

        swipeRefreshLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        swipeRefreshLayout.recyclerView.adapter = collectionShopAdapter

        swipeRefreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pageNumber = "0"
                loadData()
            }

            override fun onLoadMore(page: Int) {
                pageNumber = collectList.last().id
                loadData()
            }
        }

        loadData()
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }


    fun loadData() {
        val params = hashMapOf<String, String>()
        params["num"] = pageCount.toString()
        params["store_id"] = pageNumber
        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_FAVLIST_STORE, object : ApiManager2.OnResult<BaseBean<ArrayList<CollectionShop>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<CollectionShop>>) {
                swipeRefreshLayout.isRefreshing = false
                data.message?.let {
                    if (pageNumber == "0") {
                        collectList.clear()
                        swipeRefreshLayout.update(it)
                        collectList.addAll(it)
                        collectionShopAdapter.notifyDataSetChanged()
                    } else {
                        swipeRefreshLayout.loadMore(it)
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
                if (pageNumber == "0") {
                    swipeRefreshLayout.isRefreshing = false
                    collectList.clear()
                    collectionShopAdapter.notifyDataSetChanged()
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<CollectionShop>>) {

            }

        })
    }

    fun cancel(index: Int) {
        val params = hashMapOf<String, String>()
        params["store_id"] = collectList[index].id
        params["op"] = "2"
        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_FAV_STORE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                collectList.removeAt(index)
                collectionShopAdapter.notifyDataSetChanged()
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}