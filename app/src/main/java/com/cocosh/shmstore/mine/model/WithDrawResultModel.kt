package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/22.

amount (number, optional): 提现金额（单位人民币元） ,
name (string, optional): 银行名称 ,
bankType (string, optional): 卡类型 储蓄卡，信用卡 ,
cardNumber (string, optional): 银行卡号 ,
charge (number, optional): 手续费（单位人民币元） ,
resultCode (integer, optional): 提现结果：0-禁用 1-审核中 2-失败 3-已完成 ,
resultDesc (string, optional): 提现结果首媒 ,
flowno (string, optional): 流水号 ,
userBankName (string, optional): 银行开户名 ,
via (string, optional): 提现途径：1-银行卡，2-微信余额，3-支付宝余额
 *
 */
data class WithDrawResultModel(var time: String?,
                               var intime: String?,
                               var status: String?,
                               var no: String?,
                               var sn: String?)