package com.cocosh.shmstore.model

/**
 *
 * Created by zhangye on 2018/5/31.
 */
data class Location(
        var latitude: String,
        var longitude: String,
        var province: String,
        var city: String,
        var address: String,
        var district:String,
        var adcode:String
)