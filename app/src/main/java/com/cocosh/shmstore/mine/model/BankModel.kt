package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/2.
 * 银行卡
 *
 *  bankCardLogo (string, optional): 银行卡logo地址 ,
bankCode (string, optional): 银行编码 ,
bankName (string, optional): 银行名称 ,
bankType (string, optional): 卡类型时间 ,
idBankBaseInfo (integer, optional): 储蓄卡，信用卡 ,
createTime (string, optional): 创建 银行卡id
 */
data class BankModel(var bankCode: String?,
                     var bankLogo: String?,
                     var bankName: String?,
                     var idBankBaseInfo: String?,
                     var bankType: String?,
                     var bankCardLogo: String?,
                     var idUserBankInfo: String?,
                     var createTime: String?)