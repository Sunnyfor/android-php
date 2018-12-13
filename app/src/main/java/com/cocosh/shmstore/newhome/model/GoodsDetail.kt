package com.cocosh.shmstore.newhome.model


data class GoodsDetail(
        var goods: Goods?,
        var addr: Addr, // --- 此域待定
        var comment: ArrayList<Comment>,//评论
        var store: Shop,
        var detail: String, //商品详情
        var exts:String,
        var service:String
) {
    data class Goods(
            var id: String, // 商品id
            var name: String, // 商品名称
            var price: String, // 商品价格
            var storage: String, // 商品总库存, 注意:当总库存量 = 0时,客户端显示已售罄,不能sku选购
            var image: ArrayList<String>, // 商品主图
            var sku: Sku?,  // 仅限总库存量 > 0时,显示此域
            var params:ArrayList<Params>?, //参数
            var fav:String
    ) {
        data class Sku(
                var attrs: ArrayList<Attrs>?, //属性
                var desc: ArrayList<Desc>? //属性

        ) {
            data class Attrs(
                    var name: String,  // 属性名1
                    var vals: String
            )

            data class Desc(
                    var id: String, // sku的记录id
                    var ele: LinkedHashMap<String,String>, // sku的属性组合
                    var price: String, // sku的价格
                    var num: String // sku的库存量, 注意: 库存为0时,客户端需置灰,不可选择

            )
        }

        data class Params(
                var name:String,
                var value:String)
    }

    data class Addr(
            var prov: String, // 省份(或直辖市)
            var city: String, // 城市(直辖市忽略)
            var town: String, // 区县
            var more: String // 详细地址
    )

    data class Comment(
            var id: String, //评论id
            var smno: String, // 首媒号
            var avatar: String, // 头像
            var nick: String, // 昵称
            var words: String // 买家留言
    )
}