package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/18.
 *
 *  createDate (string, optional): 创建时间 ,
 *  money (number, optional): 转出金额 ,
 *  runningNumber (string, optional): 流水号
 *
 */
data class RedToWalletModel(var createDate: String,
                            var money: String,
                            var runningNumber: String)