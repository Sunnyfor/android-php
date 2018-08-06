package com.cocosh.shmstore.model

/**
 * 地区
 * Created by zhangye on 2018/8/6.
 */
data class District(
        var districtID: Int,
        var cityID: Int,
        var divisionCode: String,
        var districtName: String
)