package com.cocosh.shmstore.base

/**
 * 公共实体类
 * Created by 张野 on 2017/9/14.
 */

class BaseModel<T> {
    var message: String? = "请求错误"
    var success = false
    var code = -1
    var entity: T? = null
    var type = javaClass
    
    override fun toString(): String =
            "BaseModel(message='$message', success=$success, code=$code, entitya=$entity, type=$type)"
}