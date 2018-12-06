package com.cocosh.shmstore.newhome.model

data class Shop(
        var id: String, // 店铺id
        var name: String, // 店铺名称
        var logo: String, // 店铺logo
        var favs: String, // 收藏人数
        var total: String, // 在售商品总数
        var attention: String, // 是否已关注该企业
        var cates: ArrayList<Gates>?,
        var goods: ArrayList<Goods>?) {
    data class Gates(
            var id: String,
            var name: String)
}