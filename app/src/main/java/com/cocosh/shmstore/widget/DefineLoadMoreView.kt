package com.cocosh.shmstore.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.cocosh.shmstore.R
import com.yanzhenjie.loading.LoadingView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView


/**
 * Created by lmg on 2018/5/11.
 */
class DefineLoadMoreView : LinearLayout, SwipeMenuRecyclerView.LoadMoreView, View.OnClickListener {
    private var mLoadingView: LoadingView
    private var mTvMessage: TextView

    private var mLoadMoreListener: SwipeMenuRecyclerView.LoadMoreListener? = null

    constructor(context: Context) : super(context) {
        layoutParams = ViewGroup.LayoutParams(-1, -2)
        gravity = Gravity.CENTER
        visibility = View.GONE

        val displayMetrics = resources.displayMetrics

        val minHeight = (displayMetrics.density * 60 + 0.5).toInt()
        minimumHeight = minHeight

        View.inflate(context, R.layout.layout_fotter_loadmore, this)
        mLoadingView = findViewById(R.id.loading_view) as LoadingView
        mTvMessage = findViewById(R.id.tv_message) as TextView

        val color1 = ContextCompat.getColor(getContext(), R.color.colorPrimary)
        val color2 = ContextCompat.getColor(getContext(), R.color.colorPrimaryDarkS)
        val color3 = ContextCompat.getColor(getContext(), R.color.colorAccent)

        mLoadingView.setCircleColors(color1, color2, color3)
        setOnClickListener(this)
    }

    /**
     * 马上开始回调加载更多了，这里应该显示进度条。
     */
    override fun onLoading() {
        visibility = View.VISIBLE
        mLoadingView.visibility = View.VISIBLE
        mTvMessage.visibility = View.VISIBLE
        mTvMessage.text = "正在努力加载，请稍后"
    }

    /**
     * 加载更多完成了。
     *
     * @param dataEmpty 是否请求到空数据。
     * @param hasMore   是否还有更多数据等待请求。
     */
    override fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean) {
        if (!hasMore) {
            visibility = View.VISIBLE

            if (dataEmpty) {
                mLoadingView.visibility = View.GONE
                mTvMessage.visibility = View.VISIBLE
                mTvMessage.text = "没有更多数据啦"
            } else {
                mLoadingView.visibility = View.GONE
                mTvMessage.visibility = View.VISIBLE
                mTvMessage.text = "没有更多数据啦"
            }
        } else {
            visibility = View.INVISIBLE
        }
    }

    /**
     * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，这个方法会被调用，并传入加载更多的listener。
     */
    override fun onWaitToLoadMore(loadMoreListener: SwipeMenuRecyclerView.LoadMoreListener) {
        this.mLoadMoreListener = loadMoreListener

        visibility = View.VISIBLE
        mLoadingView.visibility = View.GONE
        mTvMessage.visibility = View.VISIBLE
        mTvMessage.text = "点我加载更多"
    }

    /**
     * 加载出错啦，下面的错误码和错误信息二选一。
     *
     * @param errorCode    错误码。
     * @param errorMessage 错误信息。
     */
    override fun onLoadError(errorCode: Int, errorMessage: String) {
        visibility = View.VISIBLE
        mLoadingView.visibility = View.GONE
        mTvMessage.visibility = View.VISIBLE

        // 这里要不直接设置错误信息，要不根据errorCode动态设置错误数据。
        mTvMessage.text = errorMessage
    }

    /**
     * 非自动加载更多时mLoadMoreListener才不为空。
     */
    override fun onClick(v: View) {
        if (mLoadMoreListener != null) mLoadMoreListener!!.onLoadMore()
    }
}