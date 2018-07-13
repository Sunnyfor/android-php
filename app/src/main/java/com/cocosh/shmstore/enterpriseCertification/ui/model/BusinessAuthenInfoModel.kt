package com.cocosh.shmstore.enterpriseCertification.ui.model

/**
 * 企业主认证回显信息
 * Created by zhangye on 2018/3/29.
 */
data class BusinessAuthenInfoModel(
        var entrepreneurAgency: BusinessAuthenInfo?,
        var entrepreneurLegal: BusinessAuthenInfo?) {

    data class BusinessAuthenInfo(
            var birth: String?,
            var cardAddress: String?,
            var entrepreneurId: String?,
            var ethnic: String?,
            var id: String?,
            var idBack: String?,
            var idFront: String?,
            var idNo: String?,
            var issuingAgency: String?,
            var proxyImg: String?,
            var realName: String?,
            var sex: String?,
            var validityPeriodEndTime: String?,
            var validityPeriodStartTime: String?

    )
}