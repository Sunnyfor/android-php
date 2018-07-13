package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/4.
 * 添加银行卡成功返回数据
 */
data class AddBankModel(var cardNumber: String?,
                        var cardType: String?,
                        var cardUserName: String?,
                        var cardUserPhone: String?,
                        var createTime: String?,
                        var idUserBankInfo: String?,
                        var idxBankBaseInfo: String?,
                        var status: String?,
                        var userId: String?)