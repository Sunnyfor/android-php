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
            var detail_id:String, //订单详情ID
            var goods_name: String, // 商品名称
            var image: String, // sku图片
            var price: String, // sku单价
            var attr: LinkedHashMap<String, String>, // sku属性
            var salenum: String,  // 购买数量
            var off:String, // 是否已下架:'0'-正常售卖,'1'-已下架
            var style:Int,// 退款状态：0：正常交易；1：正在退款；2：正在退货并退款；3：退款/货成功；4：退款/货关闭; 5：同意退款/货
            var sku_id:String,
            var comment:Int // 是否已评论:'1'-已评
    ){
        var ratingNum = 0
        var commentStr:String? = "" //评价内容
    }

}