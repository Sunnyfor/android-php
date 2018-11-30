package com.cocosh.shmstore.utils

import com.cocosh.shmstore.newhome.GoodsDetailActivity
import java.util.*

object GoodsDetailActivityManager {
    private val activityStack = Stack<GoodsDetailActivity>()

    fun addActivity(activity: GoodsDetailActivity) {
        activityStack.add(activity)
    }

    fun removeFirstActivity() {
        if (activityStack.size >2) {
            activityStack.removeAt(0).finish()
        }
    }

    fun removeActivity(activity: GoodsDetailActivity){
        activityStack.remove(activity)
    }
}