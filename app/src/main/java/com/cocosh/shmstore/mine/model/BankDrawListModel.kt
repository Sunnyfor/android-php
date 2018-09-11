package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/8.
 */
data class BankDrawListModel(var bankcard: ArrayList<BankModel>?,
                             var amt: Amt?) {
    data class Amt(
            var amt_min: Int,
            var amt_max: Int,
            var fee: String?
    )
}