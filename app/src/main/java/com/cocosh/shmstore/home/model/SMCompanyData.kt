package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/25.
 *
 * followStatus (integer, optional): 关注状态（0.未关注，1.已关注） ,
forumHeadImg (string, optional): 公司logo ,
forumName (string, optional): 公司名称 ,
idCompanyHomeBaseInfo (integer, optional): 论坛基础id ,
userType (integer, optional): 论坛用户类型(1-企业主 2-新媒人 3-服务商 4-用户)
 */
data class SMCompanyData(
        var id: String,
        var eid: String,
        var company: String,
        var name:String,
        var logo: String,
        var desc: String,
        var attention: String,
        var silence: String,
        var follow: String,
        var follow_nums: String
)