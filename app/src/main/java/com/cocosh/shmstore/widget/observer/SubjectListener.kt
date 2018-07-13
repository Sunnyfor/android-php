package com.cocosh.shmstore.widget.observer

/**
 * Created by lmg on 2018/7/3.
 */
interface SubjectListener {
    fun add(observerListener: ObserverListener)
    fun notifyObserver(type: Int, data: Any, content: Any, dataExtra: Any)
    fun remove(observerListener: ObserverListener)
}