package com.cocosh.shmstore.mine.model

/**
 *
 * Created by zhangye on 2018/9/14.
 */
data class OrderDetail(
        var sn: String, // 订单号
        var status: String, // 状态
        var image: String, // 图片
        var name: String, // 名称
        var price: String, // 单价
        var salenum: String, //数量
        var actual: String, // 总价
        var addtime: String,// 订单生成时间
        var receiver: String, // 收货人-姓名
        var receiver_phone: String, // 收货人-手机号码
        var receiver_province: String, // 收货人-省份
        var receiver_city: String, // 收货人-城市
        var receiver_town: String,  // 收货人-区县
        var receiver_more: String, // 收货人-详细地址
        var pay_type: String?, // 支付方式:'0'-首媒余额,'1'-支付宝,'2'-微信
        var pay_time: String?, // 付款时间
        var shipping_time: String?, // 发货时间
        var shipping_code: String?, // 物流单号
        var express: Express? // 只有订单 再发货 的时候有这个字段
) {
    data class Express(
            var company: String, //快递公司名字
            var com: String,
            var no: String,  // 快递单号
            var status: String,
            var list: ArrayList<Data>

    ) {
        data class Data(
                var datetime: String, // 物流事件发生的时间
                var remark: String,  // 物流事件的描述
                var zone: String  // 快件当时所在区域，由于快递公司升级，现大多数快递不提供此信息
        )

    }
}