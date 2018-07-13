package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/25.
 *
themeInfoVOList (Array[论坛主题返回的企业主信息参数], optional): 文章信息和企业主信息 ,
timeStamp (integer, optional): 当前分页的时间戳
commentsNumber (integer, optional): 评论数 ,
createTime (string, optional): 发帖时间 ,
idCompanyHomeTheme (integer, optional): 文章id ,
imageUrl (Array[string], optional): 图片url ,
isEssence (integer, optional): 是否是精华贴(0-不是 1-是) ,
isRead (integer, optional): 是否浏览过(1，已浏览过；2，未浏览过) ,
isTop (integer, optional): 是否置顶(0-不是 -1是) ,
readNumber (integer, optional): 浏览数 ,
resCompanyHomeInfoVO (论坛主题列表返回的企业主信息参数, optional): 企业主信息 ,
themePageUrl (string, optional): 文章静态url ,
themeTitle (string, optional): 主题标题
followStatus (integer, optional): 关注状态（0.未关注，1.已关注） ,
forumHeadImg (string, optional): 公司logo ,
forumName (string, optional): 公司名称 ,
idCompanyHomeBaseInfo (integer, optional): 论坛基础id ,
isBlack (integer, optional): 是否禁言 （1:禁言 0:不禁言） ,
userType (integer, optional): 论坛用户类型(1-企业主 2-新媒人 3-服务商 4-用户)
 */
data class SMCompanyThemeData(var themeInfoVOList: ArrayList<SubCompanyTheme>?,
                              var timeStamp: String?) {

    data class SubCompanyTheme(var commentsNumber: String?,
                               var createTime: String?,
                               var idCompanyHomeTheme: String?,
                               var imageUrl: ArrayList<String>?,
                               var isEssence: String?,
                               var isRead: String?,
                               var isTop: String?,
                               var readNumber: String?,
                               var themePageUrl: String?,
                               var themeTitle: String?,
                               var resCompanyHomeInfoVO: SubTheme?)

    data class SubTheme(var followStatus: String?,
                        var forumHeadImg: String?,
                        var forumName: String?,
                        var idCompanyHomeBaseInfo: String?,
                        var isBlack: String?,
                        var userType: String?
    )
}