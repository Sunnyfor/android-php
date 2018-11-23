package com.cocosh.shmstore.newhome.model

class GoodsNav(
        var data: ArrayList<Data>,
        var st: String) {

    data class Data(
            var id: String, // 分类id
            var parent: String, // 父级分类id
            var name: String, // 商品分类名称
            var deep: String,
            var data: ArrayList<Data>
    )

}