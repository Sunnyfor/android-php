package com.cocosh.shmstore.home.model

/**
 * Banner
 * Created by zhangye on 2018/5/15.
 */
data class Banner(
        var resHomePageBannersVoList: ArrayList<Data>?,
        var timeStamp: String
) {

    data class Data(
            var redPacketOrderId: Int,
            var redPacketImg: String?,
            var redPacketName: String?,
            var advertisementUrl: String?,
            var advertisementBaseType: Int,
            var typeInfo: String?,
            var androidUrl: String?,
            var redPacketStatus: Int,
            var companyLogo:String?,
            var companyName:String?
    )

}