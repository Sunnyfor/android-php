package com.cocosh.shmstore.home.model

/**
 * 红包
 * Created by zhangye on 2018/5/7.
 */
data class BonusModel(var timeStamp: Long,
                      var list: ArrayList<Data>) {

    data class Data(var redPacketId: Int, //红包ID
                    var advertisementBanner: String?, //广告banner
                    var redPacketName: String?, //红包名称
                    var redPacketStatus: Int, //红包状态(1已抢，0未抢)
                    var redPacketOrderId: Int, //红包订单ID
                    var advertisementBaseType: Int, //广告类型（1.app，2.可购买，3.不可购买
                    var redPacketMoneyStatus: Int,//红包状态(1-未拆开 2-已打开)
                    var typeInfo: Int,
                    var htmlUrl: String?,
                    var androidUrl: String?,
                    var companyLogo:String?, //公司logo
                    var companyName:String?, //公司名称
                    var redpacketMax:String //红包最大金额
    )
}