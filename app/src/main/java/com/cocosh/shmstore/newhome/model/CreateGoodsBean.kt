package com.cocosh.shmstore.newhome.model

data class CreateGoodsBean(
        var store_id: String,
        var store_name: String,
        var goods_id: String,
        var goods_name: String,
        var goods_img:String,
        var sku_id: String,
        var sku_str:String,
        var price: String,
        var num: String,
        var goodsList: ArrayList<CreateGoodsBean>?
)