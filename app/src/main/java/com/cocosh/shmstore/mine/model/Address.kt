package com.cocosh.shmstore.mine.model

import java.io.Serializable

/**
 * 收货地址
 * Created by zhangye on 2018/8/7.
 */
data class Address(
        var id: String,
        var receiver: String, // 收获人姓名
        var phone: String, // 收货人手机号码
        var province: String,
        var city: String,
        var town: String,
        var addr: String, // 详细地址
        var default: String, // // 是否为默认地址
        var district: String //选择地址字符串
):Serializable