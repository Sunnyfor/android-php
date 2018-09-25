package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/25.
 *
themeInfoVOList (Array[论坛主题返回的企业主信息参数], optional): 文章信息和企业主信息 ,
timeStamp (integer, optional): 当前分页的时间戳
commentsNumber (integer, optional): 评论数 ,
time (string, optional): 发帖时间 ,
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
silence (integer, optional): 是否禁言 （1:禁言 0:不禁言） ,
userType (integer, optional): 论坛用户类型(1-企业主 2-新媒人 3-服务商 4-用户)
 */
data class SMCompanyThemeData(var bbs: BBS,
                              var posts: Posts) {

    data class BBS(var id: String?,  // 论坛id
                   var eid: String?, // 企业id
                   var company:String?,
                   var name: String?,// 论坛名称
                   var logo: String?, //论坛logo
                   var desc: String?, // 论坛公告
                   var follow: String?, //是否关注
                   var follow_nums: String?,//关注人数
                   var silence:String?
    )

    data class Posts(var id: String?, //帖子ID
                     var title: String?, // 帖子主题
                     var images:String?,//帖子图片
                     var url:String?, //链接地址URL
                     var views: String?, // 该帖浏览量计数
                     var sum: Int,  // 该帖回复计数 = 回复数 + 评论数
                     var time:String? // 发布时间
    )
}