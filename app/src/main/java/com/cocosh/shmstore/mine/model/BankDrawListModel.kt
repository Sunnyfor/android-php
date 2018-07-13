package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/8.
 */
data class BankDrawListModel(var list: ArrayList<BankModel>?,
                             var ruleType: RuleType?) {
    data class RuleType(var des: String?,
                        var maxMoney: String?,
                        var minMoney: String?, var ratio: String?)
}