package com.cocosh.shmstore.home.model

/**
 *
 * Created by zhangye on 2018/5/14.
 */
data class LocationCityModel(
        var citys: ArrayList<NormalCityModel>,
        var firstLetter: String
) {
    data class NormalCityModel(var name: String,var type:String)
}