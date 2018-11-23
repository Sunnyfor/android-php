package com.cocosh.shmstore.newhome

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.adapter.GoodsSearchAdapter
import com.cocosh.shmstore.newhome.model.Goods
import com.cocosh.shmstore.title.SearchTitleFragment
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_goods_search.*

class GoodsSearchActivity : BaseActivity() {

    var keyword = ""
    val goodsList = arrayListOf<Goods>()
    var pager = "0"
    private val searchTitleFragment:SearchTitleFragment by lazy {
        SearchTitleFragment()
    }

    override fun setLayout(): Int = R.layout.activity_goods_search

    override fun initView() {
        searchTitleFragment.onKeyWord = {

            if (it.isEmpty()){
                ToastUtil.show("请输入关键字")
            }else{
                pager = "0"
                searchGoods(it)
            }
        }
        titleManager.addTitleFragment(searchTitleFragment)

        refreshLayout.recyclerView.layoutManager = LinearLayoutManager(this)
        refreshLayout.recyclerView.adapter = GoodsSearchAdapter(goodsList)

        refreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pager = "0"
                searchGoods(keyword)
            }

            override fun onLoadMore(page: Int) {
                pager = goodsList.last().goods_id
                searchGoods(keyword)
            }
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    fun searchGoods(keyword: String) {
        this.keyword = keyword
        val params = hashMapOf<String, String>()
        params["words"] = keyword
        if (pager != "0") {
            params["goods_id"] = pager
        }
        params["num"] = "20"

        ApiManager2.post(this, params, Constant.ESHOP_GOODS_SEARCH, object : ApiManager2.OnResult<BaseBean<ArrayList<Goods>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Goods>>) {
                if (pager == "0") {
                    goodsList.clear()
                    goodsList.addAll(data.message?: arrayListOf())
                    refreshLayout.recyclerView.adapter.notifyDataSetChanged()
                    refreshLayout.update(goodsList)
                }else{
                    refreshLayout.loadMore(goodsList)
                }

            }

            override fun onFailed(code: String, message: String) {
                if (pager == "0") {
                    refreshLayout.isRefreshing = false
                    refreshLayout.update(goodsList)
                }else{
                    refreshLayout.loadMore(goodsList)
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<Goods>>) {
            }

        })
    }
}