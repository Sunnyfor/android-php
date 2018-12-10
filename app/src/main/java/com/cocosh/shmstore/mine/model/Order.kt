package com.cocosh.shmstore.mine.model



/**
 * 订单实体类
 * Created by zhangye on 2018/9/4.
 */
class Order(
        var order_sn: String,// 订单ID
        var status: String, // 状态
        var store_id: String, //商铺ID
        var store_name: String, //店铺名字
        var num: String, // 订单商品个数
        var sum: String,// 订单小计
        var postage: String,// // 运费
        var list: ArrayList<Goods>//商品
) {
    data class Goods(
            var goods_id: String, // 商品id
            var goods_name: String, // 商品名称
            var image: String, // sku图片
            var price: String, // sku单价
            var attr: LinkedHashMap<String, String>, // sku属性
            var salenum: String  // 购买数量
    )

}