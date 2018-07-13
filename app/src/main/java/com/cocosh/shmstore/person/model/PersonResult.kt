package com.cocosh.shmstore.person.model

/**
 * 实人认证结果
 * Created by zhangye on 2018/4/2.
 */
data class PersonResult(
        var status: String?,
        var applyTime: String?,
        var smCode: String?,
        var userName: String?,
        var realName: String?,
        var sex: String?,
        var idNo: String?,
        var cardAddress: String?,
        var validityPeriodStartTime: String?,
        var validityPeriodEndTime: String?,
        var issuingAgency: String?,
        var faceRecognition: String?,
        var idFront: String?,
        var idBack: String?
)
