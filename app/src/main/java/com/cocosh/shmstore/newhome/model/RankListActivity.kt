package com.cocosh.shmstore.newhome.model

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.adapter.GoodsListAdapter
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_pull_refresh.*


class RankListActivity : BaseActivity() {

    var goodsList = arrayListOf<Goods>()
    var kind = "1"

    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {

        kind = intent.getStringExtra("kind") ?: "1"

        titleManager.defaultTitle(
                when (kind) {
                    "1" -> "热销·榜"
                    "2" -> "热搜·榜"
                    "3" -> "好评·榜"
                    "4" -> "必买·榜"
                    "5" -> "回购·榜"
                    else -> ""
                }
        )


        swipeRefreshLayout.recyclerView.layoutManager = GridLayoutManager(this, 2)
        val goodsAdapter = GoodsListAdapter(goodsList)

        goodsAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                GoodsDetailActivity.start(this@RankListActivity, goodsList[index].name, goodsList[index].id)
            }
        })

        swipeRefreshLayout.recyclerView.adapter = goodsAdapter
        swipeRefreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                loadData()
            }

            override fun onLoadMore(page: Int) {
            }
        }

        loadData()
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }


    fun loadData() {
        val params = hashMapOf<String, String>()
        params["kind"] = kind

        ApiManager2.post(this, params, Constant.ESHOP_RANK_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Goods>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Goods>>) {
                swipeRefreshLayout.isRefreshing = false
                data.message?.let {
                    goodsList.clear()
                    goodsList.addAll(it)
                    swipeRefreshLayout.update(it)
                    swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                }

            }

            override fun onFailed(code: String, message: String) {
                goodsList.clear()
                swipeRefreshLayout.isRefreshing = false
                swipeRefreshLayout.update(arrayListOf<Goods>())
                swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()

            }

            override fun onCatch(data: BaseBean<ArrayList<Goods>>) {
            }
        })
    }
}