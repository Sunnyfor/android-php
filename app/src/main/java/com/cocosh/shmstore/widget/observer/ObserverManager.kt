package com.cocosh.shmstore.widget.observer

/**
 * Created by lmg on 2018/7/3.
 */
class ObserverManager : SubjectListener {

    //观察者接口集合
    private val list = arrayListOf<ObserverListener>()

    companion object {
        private var observerManager: ObserverManager? = null
        fun getInstance(): ObserverManager {
            if (null == observerManager) {
                synchronized(ObserverManager::class.java) {
                    if (null == observerManager) {
                        observerManager = ObserverManager()
                    }
                }
            }
            return observerManager!!
        }
    }

    override fun add(observerListener: ObserverListener) {
        list.add(observerListener)
    }

    override fun notifyObserver(type: Int, data: Any, content: Any, dataExtra: Any) {
        for (observerListener in list) {
            observerListener.observerUpData(type, data, content, dataExtra)
        }
    }

    override fun remove(observerListener: ObserverListener) {
        if (list.contains(observerListener)) {
            list.remove(observerListener)
        }
    }
}