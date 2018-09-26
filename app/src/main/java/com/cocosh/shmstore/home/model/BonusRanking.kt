package com.cocosh.shmstore.home.model

/**
 * 红包排行榜实体类
 * Created by zhangye on 2018/5/2.
 */
data class BonusRanking(var list: ArrayList<Data>?,
                        var mine: Data?) {


    data class Data(
            var rank: String?,
            var avatar: String?,
            var nickname: String?,
            var amount: String?,
            var smno: String?)
}