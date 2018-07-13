package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/15.
 *
 * associatedId (integer, optional): 关联id ,
associatedUrl (string, optional): 跳转url ,
createDate (string, optional): 消息时间 ,
details (string, optional): 消息内容 ,
messageId (integer, optional): 消息ID ,
messageType (integer, optional): 消息类型 ,
putForwardAmount (string, optional): 提现金额 ,
putForwardBankName (string, optional): 银行名称+卡号后四位 ,
putForwardCreateDate (string, optional): 提现时间 ,
putForwardSuccessDate (string, optional): 提现成功时间 ,
resourcesUrl (string, optional): 图片资源url ,
title (string, optional): 消息标题

replyPersonHeadPic (string, optional): 回复人头像 ,
replyPersonName (string, optional): 回复人名称

causeWhy (string, optional): 驳回原因 ,
redPacketStartTime (string, optional): 红包投放开始时间 ,
 */
data class MsgModel(var associatedId: String?,
                    var associatedUrl: String?,
                    var createDate: String?,
                    var details: String?,
                    var messageId: String?,
                    var messageType: String?,
                    var putForwardAmount: String?,
                    var putForwardBankName: String?,
                    var putForwardCreateDate: String?,
                    var putForwardSuccessDate: String?,
                    var resourcesUrl: String?,
                    var title: String?,
                    //回复
                    var replyPersonHeadPic: String?,
                    var replyPersonName: String?,
                    //红包
                    var causeWhy: String?,
                    var redPacketStartTime: String?
)