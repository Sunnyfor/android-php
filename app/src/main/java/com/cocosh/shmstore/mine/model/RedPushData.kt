package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/6/13.
 * 红包投放数据
 * advertisingExposure (string, optional): 广告曝光数 ,
paidAmount (string, optional): 红包已领取金额 ,
realAdvertisingExposure (string, optional): 实际广告曝光数 ,
realRedPacketNumber (string, optional): 实际红包领取数 ,
redPacketNumber (string, optional): 红包领取数 ,
totalMoney (string, optional): 红包投放金额
 *
 */
data class RedPushData(var advertisingExposure: String?,
                       var paidAmount: String?,
                       var realAdvertisingExposure: String?,
                       var realRedPacketNumber: String?,
                       var redPacketNumber: String?,
                       var totalMoney: String?)