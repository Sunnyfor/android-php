package com.cocosh.shmstore.widget.observer

/**
 * Created by lmg on 2018/7/3.
 */
interface ObserverListener {
    /**
     * type 区分类型 1 评论数 2详情
     *
     *  数据
     */
    fun observerUpData(type: Int, data: Any, content: Any, dataExtra: Any) //刷新操作


}