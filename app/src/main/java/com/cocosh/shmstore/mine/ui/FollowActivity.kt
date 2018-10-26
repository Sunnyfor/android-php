package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.ConcernAdapter
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.FollowListModel
import com.cocosh.shmstore.mine.presenter.FollowPresenter
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.DefineLoadMoreView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import kotlinx.android.synthetic.main.activity_concern.*

/**
 * Created by lmg on 2018/4/25.
 * 关注
 */
class FollowActivity : BaseActivity(), MineContrat.IFollowView {
    private var listDatas = arrayListOf<SMCompanyThemeData.BBS>()
    private val pageCount = 20
    private var pageNumber = "1"
    private var mPresenter = FollowPresenter(this, this)
    private lateinit var adapter: ConcernAdapter

    override fun follow(result: BaseBean<ArrayList<SMCompanyThemeData.BBS>>) {
            sfSwiperefresh.isRefreshing = false

            if (result.message != null) {
                if (result.message?.size == 0) {
                    recyclerView.loadMoreFinish(true, false);
                    return
                }

                if (pageNumber == "1"){
                    listDatas.clear()
                }
                listDatas.addAll(result.message?: arrayListOf())
                recyclerView.adapter.notifyDataSetChanged()
                recyclerView.loadMoreFinish(false, true)
                pageNumber = listDatas.last().eid?:""
            }else{
                showReTryLayout("暂无数据")
            }
    }

    override fun cancelFollow(result: BaseBean<Boolean>) {

    }

    override fun setLayout(): Int = R.layout.activity_concern

    override fun initView() {
        titleManager.defaultTitle("关注")
        mPresenter.requestFollowData(1, pageNumber, pageCount.toString())

        /**
         * 加载更多。
         */
        val mLoadMoreListener = SwipeMenuRecyclerView.LoadMoreListener {
            recyclerView.postDelayed({
                mPresenter.requestFollowData(1, pageNumber, pageCount.toString())
            }, 300)
        }

        // 自定义的核心就是DefineLoadMoreView类。
        val loadMoreView = DefineLoadMoreView(this)
        recyclerView.addFooterView(loadMoreView) // 添加为Footer。
        recyclerView.setLoadMoreView(loadMoreView) // 设置LoadMoreView更新监听。
        recyclerView.setLoadMoreListener(mLoadMoreListener) // 加载更多的监听。

        sfSwiperefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h45).toInt(), resources.getDimension(R.dimen.h45).toInt()))
        adapter = ConcernAdapter(this, listDatas)
        recyclerView.adapter = adapter

        //下拉刷新
        sfSwiperefresh.setOnRefreshListener {
            //拉取默认数据
            pageNumber = "1"
            mPresenter.requestFollowData(1, pageNumber, pageCount.toString())
        }

        adapter.setOnItemClickListener(object : ConcernAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                cancelOrConfirm(listDatas[position].eid?:"", position)
            }
        })
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {
        pageNumber = "1"
        mPresenter.requestFollowData(1, pageNumber, pageCount.toString())
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, FollowActivity::class.java))
        }
    }

    private fun cancelOrConfirm(idCompanyHomeBaseInfo: String, position: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["eid"] = idCompanyHomeBaseInfo
        params["op"] = "cancel"
        ApiManager2.post(this, params, Constant.EHOME_FOLLOW_OPERATE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<String>) {
                isShowLoading = false
                    listDatas.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    if (position != listDatas.size) {
                        adapter.notifyItemRangeChanged(position, listDatas.size - position)
                    }

                if (listDatas.isEmpty()){
                    pageNumber = "1"
                    mPresenter.requestFollowData(1, pageNumber, pageCount.toString())
                }

            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }
}