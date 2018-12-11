package com.cocosh.shmstore.mine.model

/**
 *
 * Created by zhangye on 2018/9/14.
 */
data class OrderDetail(
        var recive: Recive,
        var body: Body,
        var order: Order
) {
    data class Recive(
            var name: String,
            var phone: String,
            var province: String,
            var city: String,
            var town: String,
            var more:String
    )

    data class Body(
            var price: String,
            var postage: String,
            var discount_type: String,
            var discount: String,
            var actual: String)

    data class Order(
            var flowno: String,
            var time: String,
            var pay_time: String,
            var remain: Long,
            var delivery_time: String,
            var trans_time: String

    )
}