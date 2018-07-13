package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
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
    private var listDatas = arrayListOf<FollowListModel.Follow>()
    private val pageCount = 12
    private var pageNumber = 1
    private var timeStamp = ""
    private var mPresenter = FollowPresenter(this, this)
    private lateinit var adapter: ConcernAdapter

    override fun follow(result: BaseModel<FollowListModel>) {
        if (result.success && result.code == 200) {
            sfSwiperefresh.isRefreshing = false
            if (result.entity?.list != null) {
                if (result.entity?.list?.size == 0) {
                    recyclerView.loadMoreFinish(true, false);
                    return
                }
                listDatas.addAll(result.entity?.list!!)
                timeStamp = result?.entity?.timeStamp ?: ""
                recyclerView.adapter.notifyDataSetChanged()
                recyclerView.loadMoreFinish(false, true);
                pageNumber++
            }

        } else {
            ToastUtil.show(result.message)
            sfSwiperefresh.isRefreshing = false
        }
    }

    override fun cancelFollow(result: BaseModel<Boolean>) {

    }

    override fun setLayout(): Int = R.layout.activity_concern

    override fun initView() {
        titleManager.defaultTitle("关注")
        mPresenter.requestFollowData(1, pageNumber.toString(), pageCount.toString(), timeStamp)

        /**
         * 加载更多。
         */
        val mLoadMoreListener = SwipeMenuRecyclerView.LoadMoreListener {
            recyclerView.postDelayed(Runnable {
                mPresenter.requestFollowData(1, pageNumber.toString(), pageCount.toString(), timeStamp)
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
            listDatas.clear()
            pageNumber = 1
            timeStamp = ""
            recyclerView.adapter.notifyDataSetChanged()
            sfSwiperefresh.isRefreshing = true
            mPresenter.requestFollowData(1, pageNumber.toString(), pageCount.toString(), timeStamp)
        }

        adapter.setOnItemClickListener(object : ConcernAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                cancelOrConfirm(listDatas[position].idCompanyInfoBase ?: "", position)
            }
        })
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {
        mPresenter.requestFollowData(1, pageNumber.toString(), pageCount.toString(), timeStamp)
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, FollowActivity::class.java))
        }
    }

    private fun cancelOrConfirm(idCompanyHomeBaseInfo: String, position: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["idCompanyHomeBaseInfo"] = idCompanyHomeBaseInfo
        params["isFollow"] = "0"
        ApiManager.post(this, params, Constant.SM_FOLLOW_OR_CANCEL, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                isShowLoading = false
                if (data.success && data.code == 200) {
                    listDatas.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    if (position != listDatas.size) {
                        adapter.notifyItemRangeChanged(position, listDatas.size - position);
                    }
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }
}