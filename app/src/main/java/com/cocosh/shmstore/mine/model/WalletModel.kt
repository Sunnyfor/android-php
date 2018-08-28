package com.cocosh.shmstore.mine.model

/**
 * 我的钱包
 * Created by lmg on 2018/4/17.
 *  balance (number, optional): 余额 ,
rp_sum (number, optional): 红包余额 ,
rp_cash_limit (string, optional): 红包提款金额
 */
data class WalletModel(var p: Data?,
                       var f: Data?) {
    data class Data(
            var status: String, //钱包状态:'0'-正常,'f'-已冻结,'k'-已锁定
            var rp: Rp,
            var balance: Balance) {

        data class Rp(
                var sum: String, // 红包余额
                var cash_limit: String) // 红包取现限制

        data class Balance(
                var total: String,
                var frozen: String)
    }
}