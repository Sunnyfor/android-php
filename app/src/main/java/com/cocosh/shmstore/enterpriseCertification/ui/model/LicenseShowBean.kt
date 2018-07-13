package com.cocosh.shmstore.enterpriseCertification.ui.model

/**
 * Created by lmg on 2018/3/28.
 * 营业执照回显bean
 *
bizAddress (string?, optional): 营业执照住址 ?,
bizValidityPeriod (string?, optional): 营业执照有效期 ?,
corpFname (string?, optional): 公司全名称 ?,
corpTax (string?, optional): 公司税号 ?,
domicile (string?, optional): 住所 ?,
entrepreneurId (integer?, optional): 企业主id ?,
foundingTime (string?, optional): 成立时间 ?,
id (integer?, optional): 营业执照id ?,
legalRepresentative (string?, optional): 营业执照上的法人信息 ?,
licenceImg (string?, optional): 营业执照图片地址 ?,
registeredCapital (string?, optional): 注册资本 ?,
registeredType (string?, optional): 注册类型 ?,
scope (string?, optional): 经营范围
 */
class LicenseShowBean(var bizAddress: String?,
                      var bizValidityPeriod: String?,
                      var corpFname: String?,
                      var corpTax: String?,
                      var domicile: String?,
                      var entrepreneurId: String?,
                      var foundingTime: String?,
                      var id: String?,
                      var legalRepresentative: String?,
                      var licenceImg: String?,
                      var registeredCapital: String?,
                      var registeredType: String?,
                      var scope: String?)
