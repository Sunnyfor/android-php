package com.cocosh.shmstore.vouchers.model

data class Vouchers(
        var id:String, //红包礼券领取id
        var kind_id:String, //红包礼券类型id
        var name:String, //红包礼券名称
        var limit:String, //红包礼券满limit可用
        var stime:String, //券的固定生效时间
        var etime:String, //券的固定过期时间
        var face_value:String
)