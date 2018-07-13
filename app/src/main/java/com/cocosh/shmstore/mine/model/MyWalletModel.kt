package com.cocosh.shmstore.mine.model

/**
 * 我的钱包
 * Created by lmg on 2018/4/17.
 *  balance (number, optional): 余额 ,
redBalance (number, optional): 红包余额 ,
withdrawalMoney (string, optional): 红包提款金额
 */
data class MyWalletModel(var balance: String?,
                         var redBalance: String?,
                         var withdrawalMoney: String?)