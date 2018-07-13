package com.cocosh.shmstore.utils


/**
 * Created by lmg on 2018/1/4.
 */
class RxMessageEvent<T> {
    var data: T? = null
    var mMsg: String? = null
    var type: Int? = null

    constructor(data: T) {
        this.data = data
    }

    constructor(type: Int, msg: String) {
        this.type = type
        this.mMsg = msg
    }
}