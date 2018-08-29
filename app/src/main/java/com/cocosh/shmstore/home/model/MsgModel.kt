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
data class MsgModel(var id: Int, //消息ID
                    var kind: String?,  // 消息具体种类: '0'-系统活动(默认),'1'-资金变动提醒,... 业务待定
                    var body: Body?, // 消息正文
                    var ext: String?, //扩展参数
                    var read: String?, // 是否已读
                    var time: String? //发帖时间

) {
    data class Body(
            //系统消息字段
            var title: String?,
            var content:String?,
            var image:String?,
            var time:String?,
            //核审字段
            var reason:String?,
            //提现字段
            var amount:String?,
            var bankname:String?,
            var actual_time:String?,
            //红包字段
            var refund:Int,
            var name:String?,
            var nickname:String?,
            var smno:String?
    )

}