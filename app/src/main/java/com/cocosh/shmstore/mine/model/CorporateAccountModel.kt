package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/3.
accountName (string, optional): 账户名 ,
bankNumber (string, optional): 对公账号 ,
openingBankName (string, optional): 开户行名称 ,
ruleType (账户提现规则, optional): 提取规则参数
des (string, optional),
maxMoney (string, optional): 提现最大金额 ,
minMoney (string, optional): 提现最小金额 ,
ratio (string, optional): 提现手续费
 */
data class CorporateAccountModel(
        var acct: Acctinfo,
        var amt: Amt) {

    data class Acctinfo(
            var account: String,
            var name: String,
            var bank: String)

    data class Amt(
            var amt_min: Int,
            var amt_max: Int,
            var fee: String?
    )
}