package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/26.
 *
resCompanyHomeBrandExclusiveVO (品牌论坛返回的企业信息参数, optional): 企业论坛信息 ,
themeInfoVOS (Array[论坛主题返回的企业主信息参数], optional): 企业文章列表信息 ,
timeStamp (integer, optional): 当前分页的时间戳
announcement (string, optional): 论坛公告 ,
followCount (integer, optional): 关注人数 ,
followStatus (integer, optional): 关注状态（0.未关注，1.已关注） ,
forumHeadImg (string, optional): 公司logo ,
forumName (string, optional): 公司名称 ,
idCompanyHomeBaseInfo (integer, optional): 论坛基础id ,
isBlack (integer, optional): 是否禁言 （1:禁言 0:不禁言） ,
userType (integer, optional): 论坛用户类型(1-企业主 2-新媒人 3-服务商 4-用户)
commentsNumber (integer, optional): 评论数 ,
createTime (string, optional): 发帖时间 ,
idCompanyHomeTheme (integer, optional): 文章id ,
imageUrl (Array[string], optional): 图片url ,
isEssence (integer, optional): 是否是精华贴(0-不是 1-是) ,
isRead (integer, optional): 是否浏览过(1，已浏览过；2，未浏览过) ,
isTop (integer, optional): 是否置顶(0-不是 -1是) ,
readNumber (integer, optional): 浏览数 ,
themeTitle (string, optional): 主题标题
 *
 */
data class SMThemeData(var resCompanyHomeBrandExclusiveVO: SubCompanyHomeBrandExclusive?,
                       var themeInfoVOS: ArrayList<SMCompanyThemeData>?,
                       var timeStamp: String?) {
    data class SubCompanyHomeBrandExclusive(var announcement: String?,
                                            var followCount: String?,
                                            var followStatus: String?,
                                            var forumHeadImg: String?,
                                            var forumName: String?,
                                            var idCompanyHomeBaseInfo: String?,
                                            var isBlack: String?,
                                            var userType: String?)

}