package com.cocosh.shmstore.login.model

/**
 * 第三方登录实体类
 * Created by zhangye on 2018/1/26.
 */
data class Login(
        var hasUser: Boolean,
        var headPic:String,
        var nickName:String,
        var token: String?,
        var userId: String?,
        var smCode:String?,
        var type: String?,
        var openId:String?,
        var resResIndexInfo: ResResIndexInfoModel?) {
    class ResResIndexInfoModel {
        var authStatus: String = ""
        var corpFname: String = ""
        var realName = ""
    }
}