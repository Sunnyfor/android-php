package com.cocosh.shmstore.home.model

/**
 *
 * Created by zhangye on 2018/5/15.
 */
data class BonusAmount(
        var percentPopularRedEnvelopes: String?,   //大众红包个数百分比
        var percentPopularRedEnvelopesFiles: String?,  //大众红包文案
        var percentPrecisionRedEnvelopes:String?,  //精准红包个数百分比
        var percentVermicelliRed: String?,   //粉丝红包个数百分比
        var percentVermicelliRedFiles:String?,  //粉丝红包文案
        var vermicelliRedHeadPicture:String?,  //粉丝红包头像
        var popularRedEnvelopes:Double, //大众红包金额
        var popularRedEnvelopesHeadPicture:String?, //大众红包头像
        var precisionRedEnvelopes:Double,  //精准红包金额
        var precisionRedEnvelopesFiles:String?,//精准红包文案
        var precisionRedEnvelopesHeadPicture:String?,//精准红包头像
        var redPacketAllAmount:Double,//红包总金额
        var vermicelliRed: Double,//粉丝红包金额
        var redPacketPicture:String?)