package com.cocosh.shmstore.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class CustomViewPager: ViewPager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var isCanScroll = true

    fun setScanScroll(isCanScroll:Boolean){
        this.isCanScroll = isCanScroll
    }


    override fun onInterceptTouchEvent(ev: MotionEvent):Boolean {
        return isCanScroll && super.onInterceptTouchEvent(ev)
    }

}