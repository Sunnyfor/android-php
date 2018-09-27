package com.cocosh.shmstore.home.model

import com.google.gson.internal.LinkedTreeMap

/**
 * 红包详情页数据
 * Created by zhangye on 2018/9/26.
 */
data class RedPackage(
        var result:String?,
        var hold:String?,
        var tip:String?,
        var name:String?,
        var avatar:String?,
        var base:Base?,
        var h5url:String?,
        var attrs:ArrayList<LinkedTreeMap<String,Any>>?

){
    data class Base(
            var no:String?,
            var type:String?,
            var time:String?,
            var name:String?,
            var pubtime:String?,
            var amount:String?,
            var svrtime:String?
    )


    data class Attrs(
            var kind:String?,
            var item:String?,
            var `val`:String?
    )

}