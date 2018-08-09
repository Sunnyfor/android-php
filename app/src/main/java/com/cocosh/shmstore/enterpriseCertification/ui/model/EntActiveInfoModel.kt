package com.cocosh.shmstore.enterpriseCertification.ui.model

/**
 * Created by lmg on 2018/3/26.
var authReason: String 审核理由
var entBankcardStatus: String 对公账号状态
var entId: String 企业主id
var entIdentityStatus: String 身份认证状态
var entLicenseStatus: String  营业执照状态
var status: String 企业主认证状态
 *
 */
data class EntActiveInfoModel(
        var cert:Cert,
        var base:Base

){
    data class Cert(
            var status:Int,  //认证状态: '0'-未认证,'1'-(已认证)待激活,'2'-(提交激活信息)审核中,
            var time:String,
            var province:String, // 服务区域:省份
            var city:String, // 服务区域:城市
            var fee:String, // 认证费用
            var reason:String // 审核原因
    )

    data class Base(
            var uscc:String,// 统一社会信用代码
            var name:String,// 企业名称
            var legal:String,// 法定代表人
            var beg_time:String,// 营业期限-开始日期(格式:'Y-m-d')
            var end_time:String,// 营业期限-结束日期(格式:'Y-m-d')
            var kind:String, // 类型
            var addr:String,// 住所
            var capital:String, // 注册资本
            var found:String,// 成立日期
            var bank:String, // 开户银行
            var account:String,// 对公帐号
            var tel:String,// 座机号码
            var linker:String // 联系人
    )
}