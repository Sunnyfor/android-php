package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/7/5.
 *
 * attTime (string, optional): 认证时间 ,
name (string, optional): 名称 ,
numEnt (integer, optional): 拓展的企业主数量 ,
numEntBeDistribution (integer, optional): 平台分配的企业主数量 ,
numPartnet (integer, optional): 拓展的新媒人数量 ,
numPerson (integer, optional): 拓展的个人用户数量 ,
numPersonBeDistribution (integer, optional): 平台分配的个人用户数量 ,
operatorInfoOfThis (string, optional): 所属服务商名称 ,
operatorSpecialIdOfThis (integer, optional): 所属服务商特殊身份ID ,
profit (number, optional): 收益 ,
serviceArea (string, optional): 服务区域
 *
 */
data class OperatorMainData(var attTime: String?,
                            var name: String?,
                            var numEnt: String?,
                            var numEntBeDistribution: String?,
                            var numPartnet: String?,
                            var numPerson: String?,
                            var numPersonBeDistribution: String?,
                            var operatorInfoOfThis: String?,
                            var operatorSpecialIdOfThis: String?,
                            var profit: String?,
                            var serviceArea: String?
)