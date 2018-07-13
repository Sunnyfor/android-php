package com.cocosh.shmstore.mine.model

/**
 * 档案实体类
 * Created by zhangye on 2018/5/7.
 */
data class ArchiveModel(
        var headPic: String?,
        var nickName: String?,
        var birthday: String?,
        var sex: String?,
        var companyName: String?,
        var realName: String?,
        var industryName: String?,
        var areaCode: String?,
        var regionName: String?,
        var interestList: ArrayList<String>?,
        var degreeOfPerfection: Float)