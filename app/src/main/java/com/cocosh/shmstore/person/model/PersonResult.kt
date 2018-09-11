package com.cocosh.shmstore.person.model

/**
 * 实人认证结果
 * Created by zhangye on 2018/4/2.
 */
data class PersonResult(
        var cert: Cert, // 认证信息
        var base: Base  // 基础信息

) {
    data class Cert(
            var status: Int, // 认证状态
            var time: String, // 认证时间
            var province:String, // 服务区域:省份
            var city:String, // 服务区域:城市
            var fee:String // 认证费用
    )

    data class Base(
            var name: String, // 认证人
            var gender: Int, // 性别:'0'-女,'1'-男
            var idno: String, // 证件号码
            var addr: String, // 住址
            var beg_time: String, // 证件有效期-开始日期
            var end_time: String, // 证件有效期-结束日期
            var org: String // 签发机关
    )
}
