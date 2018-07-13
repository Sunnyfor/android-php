package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/10.
 * 收藏列表
 *
advertisementBanner (string, optional): 广告banner ,
advertisementBaseType (integer, optional): 广告类型（1.app，2.可购买，3.不可购买 ,
htmlUrl (string, optional): 广告h5的url ,
idRedPacketBaseInfo (integer, optional): 红包id ,
idRedPacketUserInfo (integer, optional): 红包用户id ,
isGive (integer, optional): 是否赠送(0-未赠送 1-已赠送) ,
redPacketMoney (number, optional): 红包总金额 ,
redPacketMoneyStatus (integer, optional): 红包状态(1-未拆开 2-已打开 ,
redPacketName (string, optional): 红包名称 ,
typeInfo (integer, optional): 红包类型（1.大众 2.精准 3.粉丝）
 *
 */
data class CollectionModel(
        var advertisementBanner: String?,
        var advertisementBaseType: String?,
        var htmlUrl: String?,
        var idRedPacketBaseInfo: String?,
        var idRedPacketUserInfo: String?,
        var isGive: String?,
        var redPacketId: String?,
        var redPacketMoney: String?,
        var redPacketMoneyStatus: String?,
        var redPacketName: String?,
        var typeInfo: String?,
        var iosUrl: String?,
        var androidUrl: String?,
        var companyLogo: String?,
        var companyName: String?,
        var idRedPacketOrderInfo: String?
)