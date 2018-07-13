package com.cocosh.shmstore.home.model

/**
 * 发送红包实体类
 * Created by zhangye on 2018/6/6.
 */
data class BonusConfig(
        var prevAction: Int, //前次操作的结果（0-无记录； 1-红包数据已填写完毕； 2-广告数据已填写完毕 ,
        var unitPriceRedPacket: Float, //系统定义的单个红包金额 ,
        var minRedPacketCount: Int, //系统定义的一次最少发放个数, 为0表示不限制 ,
        var maxRedPacketCount: Int, //系统定义的一次最多发放个数, 为0表示不限制 ,
        var userActionDurationTime: Int, // 红包发放行为的持续时间（单位为分钟），由系统定义，自动退款时间(红包结束时间）为开始时间加此变量值
        var orderNumber: String?,
        var newOrderNumber:String,//红包操作流水号 ,
        var businessRedPacketInfo: BusinessRedPacketInfo?, //发红包行为的红包基本信息, 当prevAction为 '1-红包数据已填写完毕' 时此对象内容有效
        var businessADInfo: BusinessADInfo?  //发红包行为的广告基本信息, 当prevAction为 '2-广告数据已填写完毕' 时此对象内容有效
) {

    data class BusinessRedPacketInfo(
            var idRedPacketOrder: Int, // 红包投放ID
            var orderNumber: String?, //操作流水号
            var redPacketCount: Int, // 红包个数
            var redPacketImg: String?, // 红包图片地址
            var redPacketName: String?, // 红包名称 ,
            var redPacketTotalPrice: Float, // 红包总额, 此变量仅供后台校验使
            var startTime: String?, // 投放开始时间
            var targetAreaCode: String?, // 投放区域ID
            var targetAreaName:String?
    )

    data class BusinessADInfo(
            var bannerImg: Img?,  //广告图，当前版本仅支持一张，此处为List仅为兼容后续需求 ,
            var contentImg: Img?, //最少一张广告内容图 ,
            var desc: String?,
            var idRedPacketOrder: Int
    ){
        data class Img(
                var idAdvertisementBaseTypeValueInfo:Int,
                var orderId:Int,
                var url:String?,
                var typeKey:String
        )

    }
}