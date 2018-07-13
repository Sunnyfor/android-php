package com.cocosh.shmstore.widget

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;


/**
 * Created by lmg on 2018/5/31.
 * web+list
 */
class SMediaScrollView : ScrollView {
    private var recyclerView: RecyclerView? = null
    var downY: Int = 0
    var moveY = 0
    private var downEvent: MotionEvent? = null

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //如果RecyclerView
        if (recyclerView != null) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    downEvent = ev
                    downY = ev.x.toInt()
                    moveY = 0
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveY = ev.y.toInt() - downY
                    if (Math.abs(moveY) > 20) {
                        //表示滑动
                        if (moveY > 0) {
                            //向下滑动,如果recyclerView还没有显示则拦截事件
                            if (scrollY < recyclerView!!.top) {
                                return true
                            }
                        } else {
                            //向上滑动
                            if (scrollY == recyclerView!!.top) {
                                //如果第一个item已经显现才拦截事件
                                val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
                                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                                    return true
                                }
                            }
                        }
                    }
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }
}