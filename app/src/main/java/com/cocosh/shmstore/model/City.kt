package com.cocosh.shmstore.model

/**
 * 城市
 * Created by zhangye on 2018/8/6.
 */
data class City(
        var cityID:Int,
        var provinceID:Int,
        var divisionCode:String,
        var cityName:String
)