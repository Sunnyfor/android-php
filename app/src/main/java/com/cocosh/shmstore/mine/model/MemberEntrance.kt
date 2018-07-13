package com.cocosh.shmstore.mine.model

/**
 * 我的页面入口实体类
 * Created by zhangye on 2018/3/22.
backGround (string, optional): 个人背景图片 ,
checkPayPassword (boolean, optional): 是否设置过支付密码 true:设置 false：未设置 ,
cityOpertorsStatus (string, optional): 服务商认证状态 ,
degreeOfPerfection (string, optional): 用户信息完善度 ,
entStatus (string, optional): 企业主认证状态 ,
headPic (string, optional): 用户头像图片 ,
partnerStatus (string, optional): 新媒人认证状态 ,
personStatus (string, optional): 个人认证状态 ,
realName (string, optional): 真实姓名 ,
smCode (string, optional): 首媒号 ,
userName (string, optional): 用户账号 ,
userNick (string, optional): 昵称
 *
 *
 */
class MemberEntrance(var headPic: String?,
                     var userNick: String?,
                     var smCode: String?,
                     var personStatus: String?,
                     var partnerStatus: String?,//新媒人认证状态
                     var cityOpertorsStatus: String?, //服务商认证状态
                     var entStatus: String?,
                     var backGround: String?,
                     var userName: String?,
                     var realName: String?,
                     var checkPayPassword: Boolean?,
                     var degreeOfPerfection: String?)
