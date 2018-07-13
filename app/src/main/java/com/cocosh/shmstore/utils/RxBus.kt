package com.cocosh.shmstore.utils

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor


/**
 * Created by lmg on 2018/1/4.
 */
class RxBus {
    private var mBus: FlowableProcessor<Any>? = null

    init {
        mBus = PublishProcessor.create<Any>().toSerialized()
    }

    companion object {
        private object Holder {
            val instance = RxBus()
        }

        fun getInstance(): RxBus {
            return Holder.instance
        }
    }


    fun post(obj: Any) {
        mBus?.onNext(obj)
    }

    fun <T> register(clz: Class<T>): Flowable<T> {
        return mBus?.ofType(clz)!!
    }

    fun unregisterAll() {
        //解除注册
        mBus?.onComplete()
    }

    fun hasSubscribers(): Boolean {
        return mBus?.hasSubscribers()!!
    }
}