package com.cocosh.shmstore.newhome.model

data class CreateOrder(
        var flowno:String,
        var order_sn:ArrayList<String>,
        var actual:String,
        var pay_status:String) // 是否全部抵扣完 0没有抵扣完 1全部抵扣完