package com.cocosh.shmstore.newCertification.model

data class ApplyPartner(
        var cert: Cert,
        var svc: Svc
) {
    data class Cert(
            var name: String,
            var idno: String
    )

    data class Svc(
            var name: String, // 企业名称
            var province: String, // 服务区域-省份
            var city: String, // 服务区域-城市
            var legal: String // 企业法人
    )
}