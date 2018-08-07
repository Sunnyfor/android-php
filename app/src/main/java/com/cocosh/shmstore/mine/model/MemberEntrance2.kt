package com.cocosh.shmstore.mine.model

/**
 * 个人资料
 * Created by zhangye on 2018/8/6.
 */
data class MemberEntrance2(
        var degree: String, //25, // 完善度,百分比整数
        var avatar: String, //个人头像
        var nickname: String, //昵称
        var realname: String, //真实姓名
        var birth: String, // 生日,格式:'Y-m-d'
        var gender: String,// 性别:'0'-女,'1'-男,'2'-未知
        var province: String, // 省份地区代码
        var city: String,//城市地区代码
        var town: String, // 区县地区代码
        var district:String,
        var company: String,//公司名称
        var industry: String,//所属行业编号
        var industry_name:String,//所属行业名字
        var hobby: String, //兴趣爱好id列表(多笔用英文逗号分隔)
        var personStatus:String //个人认证状态
)