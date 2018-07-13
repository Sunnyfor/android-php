package com.cocosh.shmstore.home.model

/**
 * 红包排行榜实体类
 * Created by zhangye on 2018/5/2.
 */
data class BonusRanking(var rankingListVOS: ArrayList<Data>?,
                        var resMyRankingVO: ResMyRankingVO?) {


    data class Data(
            var rank: String?,
            var headPic: String?,
            var userName: String?,
            var totalAmount: String?,
            var idUserInfoBase: String?)

    data class ResMyRankingVO(var myRank: String?,
                              var myHeadPic: String?,
                              var myUserName: String?,
                              var myTotalAmount: String?)

}