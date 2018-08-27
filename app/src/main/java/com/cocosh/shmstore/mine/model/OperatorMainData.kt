package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/7/5.
 *
 * time (string, optional): 认证时间 ,
name (string, optional): 名称 ,
expand_enterprise_num (integer, optional): 拓展的企业主数量 ,
numEntBeDistribution (integer, optional): 平台分配的企业主数量 ,
numPartnet (integer, optional): 拓展的新媒人数量 ,
expand_personal_num (integer, optional): 拓展的个人用户数量 ,
platform_personal_num (integer, optional): 平台分配的个人用户数量 ,
provider_name (string, optional): 所属服务商名称 ,
operatorSpecialIdOfThis (integer, optional): 所属服务商特殊身份ID ,
profit (number, optional): 收益 ,
place (string, optional): 服务区域
 *
 */
data class OperatorMainData(var time: String?,
                            var name: String?,
                            var expand_enterprise_num: String?,
                            var numEntBeDistribution: String?,
                            var numPartnet: String?,
                            var expand_personal_num: String?,
                            var platform_personal_num: String?,
                            var provider_name: String?,
                            var operatorSpecialIdOfThis: String?,
                            var profit: String?,
                            var place: String?
)