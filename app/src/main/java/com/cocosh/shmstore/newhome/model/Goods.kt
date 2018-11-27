package com.cocosh.shmstore.newhome.model

data class Goods(
        var id:String, // 商品id
        var name:String, // 商品名称
        var price:String, // 商品价格
        var image:String, // 商品主图
        var sell_out:String, // 销售状态:'0'-在售,'1'-已售罄(即库存为0)
        var comments:String  //总评价数量
)