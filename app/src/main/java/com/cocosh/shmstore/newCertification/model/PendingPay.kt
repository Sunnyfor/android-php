package com.cocosh.shmstore.newCertification.model

/**
 * 新媒人待支付数据模型
 * Created by zhangye on 2018/4/3.
 */
data class PendingPay(
        var cert: Cert?, // 认证信息
        var svc: Svc) { // 基础信息

    data class Cert(
            var status: String, // 认证状态: '2'-认证失败
            var fee: String, // 认证金额
            var name: String, // 个人姓名
            var idno: String //身份证号

    )

    data class Svc(
            var name:String, // 服务商名字
            var province:String, // 服务商省份
            var city:String, // 服务商城市
            var legal:String //法人
    )
}