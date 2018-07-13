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
data class CorporateAccountModel(var openingBankName: String?,
                                 var bankNumber: String?,
                                 var accountName: String?,
                                 var ruleType: RuleTypeData?) {
    data class RuleTypeData(var minMoney: String?,
                            var maxMoney: String?,
                            var ratio: String?,
                            var des: String?)
}