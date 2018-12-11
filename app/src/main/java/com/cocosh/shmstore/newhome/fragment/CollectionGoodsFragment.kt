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
import com.cocosh.shmstore.newhome.adapter.CollectionGoodsAdapter
import com.cocosh.shmstore.newhome.model.CollectionGoods
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_pull_refresh.*

class CollectionGoodsFragment : BaseFragment() {

    val pageCount = 20
    var pageNumber = "0"

    private var collectList = arrayListOf<CollectionGoods>()

    val collectionGoodsAdapter: CollectionGoodsAdapter by lazy {
        CollectionGoodsAdapter(collectList) {
            cancel(it) //取消收藏
        }.apply {
            setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(v: View, index: Int) {
                    if (collectList[index].sale == "1") {
                        GoodsDetailActivity.start(context, collectList[index].name, collectList[index].id)
                    }
                }
            })
        }
    }

    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {

        swipeRefreshLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        swipeRefreshLayout.recyclerView.adapter = collectionGoodsAdapter

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
        params["goods_id"] = pageNumber
        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_FAVLIST_GOODS, object : ApiManager2.OnResult<BaseBean<ArrayList<CollectionGoods>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<CollectionGoods>>) {
                swipeRefreshLayout.isRefreshing = false
                data.message?.let {
                    if (pageNumber == "0") {
                        collectList.clear()
                        swipeRefreshLayout.update(it)
                        collectList.addAll(it)
                        collectionGoodsAdapter.notifyDataSetChanged()
                    } else {
                        swipeRefreshLayout.loadMore(it)
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
                if (pageNumber == "0") {
                    swipeRefreshLayout.isRefreshing = false
                    collectList.clear()
                    collectionGoodsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<CollectionGoods>>) {

            }

        })
    }

    fun cancel(index: Int) {
        val params = hashMapOf<String, String>()
        params["goods_id"] = collectList[index].id
        params["op"] = "2"
        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_FAV_GOODS, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                collectList.removeAt(index)
                collectionGoodsAdapter.notifyDataSetChanged()
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}