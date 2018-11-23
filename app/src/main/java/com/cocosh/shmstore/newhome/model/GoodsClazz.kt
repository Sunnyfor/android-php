package com.cocosh.shmstore.newhome.model

data class GoodsClazz(
        var data: ArrayList<Bean>,
        var ts: String

) {
    data class Bean(
            var id: String, // 分类id
            var parent: String, // 父级分类id
            var name: String, // 商品分类名称
            var logo: String, // 商品分类logo
            var order: String,// 排序序号
            var deep: String,
            var data: ArrayList<Bean>?
    )

}
