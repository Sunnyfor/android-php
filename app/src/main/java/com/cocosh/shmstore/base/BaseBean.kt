package com.cocosh.shmstore.base

/**
 * 新版公共实体类
 * Created by zhangye on 2018/8/6.
 */
class BaseBean<T> {
    var status = "200"
    var message: T? = null
    var type = javaClass

    override fun toString(): String =
            "BaseModel(status=$status, message=$message, value=$type)"
}