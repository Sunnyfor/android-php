package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/16.
 *
 * amount (number, optional): 金额 ,
time (string, optional): 创建时间 ,
flowno (integer, optional): 记录id ,
idxUserAccount (integer, optional): 账户id ,
memo (string, optional): 描述 ,
resultType (integer, optional): 记录类型(1-充值 2-提现 3-消费 4-转入) 1,4金额为 正，2,3金额为 负 ,
resultTypeVia (string, optional): 充值:1-微信 2-支付宝 3-银行卡提现:10-银行卡扣款:20-系统扣款 ,
runningNumber (string, optional): 流水编号 ,
status (integer, optional): 状态(0-禁用 1-审核中 2-系统异常 3-已完成)
 */
data class RedWaterModel(var amount: String?,
                         var time: String?,
                         var flowno: String?,
                         var memo: String?,
                         var fine_type:String)