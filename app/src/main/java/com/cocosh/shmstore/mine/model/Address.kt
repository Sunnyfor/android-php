package com.cocosh.shmstore.mine.model

/**
 * 收货地址
 * Created by zhangye on 2018/8/7.
 */
data class Address(
        var id:String,
        var receiver:String, // 收获人姓名
        var phone:String, // 收货人手机号码
        var addr:String, // 详细地址
        var default:String // // 是否为默认地址
)