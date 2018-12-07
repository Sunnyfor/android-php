package com.cocosh.shmstore.newhome.model

data class CollectionGoods(
        var id: String, // 商品id
        var name: String, // 商品名称
        var price: String, // 商品价格
        var url: String, // 商品图片
        var sale: String, // 上下架状态:'0'-未上架,'1'-已上架
        var favs: String) // 被收藏数量