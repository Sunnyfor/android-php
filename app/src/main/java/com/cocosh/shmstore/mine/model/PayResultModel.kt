package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/6/7.
 * 支付结果页
 */
data class PayResultModel(var status: String?,
                          var detail: Detail?) {
    data class Detail(
            var flowno: String?,
            var sn: String?,
            var status: String?,
            var pay_flowno: String?,
            var amount: String?,
            var actual: String?,
            var time: String?,
            var pay_type: String
    )

}