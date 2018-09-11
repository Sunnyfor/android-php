package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/2.
 * 银行卡
 *
 *  bankCardLogo (string, optional): 银行卡logo地址 ,
bankCode (string, optional): 银行编码 ,
name (string, optional): 银行名称 ,
bankType (string, optional): 卡类型时间 ,
id (integer, optional): 储蓄卡，信用卡 ,
time (string, optional): 创建 银行卡id
 */
data class BankModel(var id: String?, // 银行卡id
                     var bank_kind: String?, //开户行id
                     var bank_name: String?, // 开户行名称
                     var bank_logo: String?, // 开户行logo
                     var card_no: String?, // 银行卡号
                     var card_kind: String?, // 银行卡类型:'0'-储蓄卡,'1'-信用卡,...
                     var phone: String?, // 预留手机号码
                     var realname: String?, //持卡人-姓名
                     var idno: String?, // 持卡人-身份证号码
                     var default: String, // 是否为默认卡
                     var addtime: String // 添加时间
)
