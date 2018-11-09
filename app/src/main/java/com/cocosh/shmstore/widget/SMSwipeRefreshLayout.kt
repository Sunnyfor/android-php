package com.cocosh.shmstore.widget

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import com.cocosh.shmstore.base.BaseRecycleAdapter
import com.yanzhenjie.recyclerview.swipe.SwipeAdapterWrapper
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView

/**
 * 可刷新组件
 * Created by zhangye on 2018/5/18.
 */
class SMSwipeRefreshLayout : SwipeRefreshLayout {

    private var page = 1
    var pageCount = 20

    var onRefreshResult: OnRefreshResult? = null

    lateinit var recyclerView: SwipeMenuRecyclerView

    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    fun initView(context: Context) {

        recyclerView = SwipeMenuRecyclerView(context)

        recyclerView.useDefaultLoadMore()

        recyclerView.setLoadMoreListener {
            // 加载更多的监听。
            page += 1
            onRefreshResult?.onLoadMore(page)
        }

        setOnRefreshListener {
            page = 1
            onRefreshResult?.onUpdate(page)
        }

        addView(recyclerView)
    }

    fun update(list: ArrayList<*>?) {
        if (list == null || list.isEmpty()) {
            recyclerView.loadMoreFinish(true, false)
        } else {
            if (list.size < pageCount) {
                recyclerView.loadMoreFinish(false, false)
            } else {
                recyclerView.loadMoreFinish(false, true)
            }
        }
        isRefreshing = false
    }


    @Suppress("UNCHECKED_CAST")
    fun <T> loadMore(list: ArrayList<T>?) {
        if (list == null || list.isEmpty()) {
            recyclerView.loadMoreFinish(false, false)
        } else {

            if (list.size < pageCount) {
                recyclerView.loadMoreFinish(false, false)
            } else {
                recyclerView.loadMoreFinish(false, true)
            }
            ((recyclerView.adapter as SwipeAdapterWrapper)
                    .originAdapter as BaseRecycleAdapter<T>)
                    .notifyDataSetChanged(list)
        }
    }


    interface OnRefreshResult {
        fun onUpdate(page: Int)
        fun onLoadMore(page: Int)
    }
}