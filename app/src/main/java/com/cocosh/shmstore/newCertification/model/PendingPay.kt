package com.cocosh.shmstore.newCertification.model

/**
 * 新媒人待支付数据模型
 * Created by zhangye on 2018/4/3.
 */
data class PendingPay(
        var areaCode: String?,
        var idNo: String?,
        var money: String?,
        var operatorArea: String?,
        var operatorId: String?,
        var operatorLegalName: String?,
        var operatorName: String?,
        var realName: String?,
        var bizCode:String?)