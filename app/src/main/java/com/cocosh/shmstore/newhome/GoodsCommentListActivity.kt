package com.cocosh.shmstore.newhome

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.adapter.GoodsCommentListAdapter
import com.cocosh.shmstore.newhome.model.CommentBean
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_pull_refresh.*

class GoodsCommentListActivity: BaseActivity() {

    val pageCount = 20
    var pageNumber = "0"
    var list = arrayListOf<CommentBean>()

    var goods_id = "0"

    override fun setLayout(): Int = R.layout.layout_pull_refresh

    override fun initView() {
        titleManager.defaultTitle("评论")
        goods_id = intent.getStringExtra("goods_id")
        swipeRefreshLayout.recyclerView.layoutManager = LinearLayoutManager(this)
        swipeRefreshLayout.recyclerView.adapter = GoodsCommentListAdapter(list)

        swipeRefreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                pageNumber = "0"
                loadData()
            }

            override fun onLoadMore(page: Int) {
                pageNumber = list.last().id
                loadData()
            }
        }

        loadData()
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    fun loadData(){
        val params = HashMap<String,String>()
        params["goods_id"] = goods_id
        if (pageNumber != "0"){
            params["comment_id"] = pageNumber
        }
        ApiManager2.post(this,params, Constant.ESHOP_COMMENTS,object : ApiManager2.OnResult<BaseBean<ArrayList<CommentBean>>>(){
            override fun onSuccess(data: BaseBean<ArrayList<CommentBean>>) {
                swipeRefreshLayout.isRefreshing = false
                data.message?.let {
                    if (pageNumber == "0") {
                        list.clear()
                        swipeRefreshLayout.update(it)
                        list.addAll(it)
                        swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                    } else {
                        swipeRefreshLayout.loadMore(it)
                    }
                }

            }

            override fun onFailed(code: String, message: String) {
                if (pageNumber == "0") {
                    swipeRefreshLayout.isRefreshing = false
                    list.clear()
                    swipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<CommentBean>>) {

            }

        })
    }

    companion object {
        fun start(context:Context,goods_id:String){
            context.startActivity(Intent(context,GoodsCommentListActivity::class.java)
                    .putExtra("goods_id",goods_id))
        }
    }
}