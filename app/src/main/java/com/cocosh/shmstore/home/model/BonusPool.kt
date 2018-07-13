package com.cocosh.shmstore.home.model

/**
 *
 * Created by zhangye on 2018/5/16.
 */
data class BonusPool(var redPacketPicture: String?,
                     var resPopularRedEnvelopesList:ArrayList<Data>,
                     var redPacketAllAmount: Float) {

    data class Data(var popularRedEnvelopes: Double,
                    var percentPopularRedEnvelopes: String?,
                    var popularRedEnvelopesHeadPicture: String?,
                    var popularRedEnvelopesFiles: String?,
                    var redType: Int)
}