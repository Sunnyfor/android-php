package com.cocosh.shmstore.login.model

/**
 * 用户登录实体类
 * Created by zhangye on 2018/8/2.
 */
data class Login2(
        var code: String,  //首媒号
        var sid: String, // 会话唯一凭证,用于<注销>操作
        var access_token: String, // 令牌,用于客户端判断用户是否在线
        var expires_in: String, // 令牌生命周期(单位,秒):默认30天
        var time: String, // 令牌生效开始时间
        var invitee: Invitee,
        var cert: String // 认证状态:'0'-未认证,'1'-待激活,'2'-审核中,'3'-激活失败,'4'-已认证

) {
    data class Invitee(
            var code: String, //被邀请码
            var type: String, // 被邀约的认证类型: '0'-注册用户,'x'-新媒人,'b'-企业主
            var inviter: String,// 邀约人
            var inviter_type: String // 邀约人类型: 'x'-新媒人,'f'-服务商
    )
}