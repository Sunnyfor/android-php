package com.cocosh.shmstore.login.model

/**
 * 第三方回执
 * Created by zhangye on 2018/1/26.
 */
data class OtherLogin(
        var type: String,
        var openId: String
) {
    var token:String? = null
    var phone:String? = null
}