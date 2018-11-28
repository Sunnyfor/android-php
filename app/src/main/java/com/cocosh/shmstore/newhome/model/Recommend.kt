package com.cocosh.shmstore.newhome.model

data class Recommend(
        var rank: ArrayList<String>?, // 排行榜: 前2笔热销榜数据,1笔热搜榜数据,1笔好评榜数据,1笔必买榜数据,1笔回购榜数据
        var market: ArrayList<Market>?,
        var recommend: ArrayList<Goods>?
) {

    data class Market(
            var id: String, // 活动id
            var name: String, // 活动名称
            var desc: String, // 活动描述
            var program: ArrayList<Program>?) {
        data class Program(
                var id: String, // 节目id
                var name: String, // 节目名称
                var desc: String, // 节目描述
                var image: String // 隶属于节目的随机商品图片
        )
    }

}