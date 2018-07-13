package com.cocosh.shmstore.base

import android.view.View
import com.cocosh.shmstore.utils.LogUtil
import java.text.FieldPosition
import java.util.*

/**
 * 防止双击跳转，出现双页面
 * Created by zhangye on 2018/5/7.
 */
class NoDoubleClickListener {


    private val minClickDelayTime = 1000
    private var lastClickTime: Long = 0
    private var type = 0
    private var fragment: BaseFragment? = null
    private var activity: BaseActivity? = null
    private var onItemClickListener: OnItemClickListener? = null

    constructor(fragment: BaseFragment) {
        type = 0
        this.fragment = fragment
    }

    constructor(activity: BaseActivity) {
        type = 1
        this.activity = activity
    }

    constructor(onItemClickListener: OnItemClickListener?) {
        type = 2
        this.onItemClickListener = onItemClickListener
    }


    fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis

        if (currentTime < lastClickTime){
            lastClickTime = 0
        }

        if (currentTime - lastClickTime > minClickDelayTime) {
            lastClickTime = currentTime
            if (type == 0) {
                fragment?.onListener(v)
            }

            if (type == 1) {
                activity?.onListener(v)
            }
        }
    }

    fun onItemClick(v: View, position: Int) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > minClickDelayTime) {
            lastClickTime = currentTime
            onItemClickListener?.onItemClick(v,position)
        }
    }
}