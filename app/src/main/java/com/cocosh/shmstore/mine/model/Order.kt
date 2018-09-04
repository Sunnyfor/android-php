package com.cocosh.shmstore.mine.model

/**
 * 订单实体类
 * Created by zhangye on 2018/9/4.
 */
class Order(
        var id: String,// 订单ID
        var sn: String,// 订单号
        var status: String, // 状态
        var image: String, // // 图片
        var name: String, //名称
        var price: String, // 单价
        var salenum: String, //数量
        var actual: String, // 总价
        var addtime: String // 订单生成时间
)