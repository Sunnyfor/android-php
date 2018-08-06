package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/26.
 *
 * pageInfo (PageInfo«评论列表», optional): 评论列表 ,
sortType (integer, optional): 排序类型 （1:正序 0:倒序） ,
timeStamp (integer, optional): 当前分页的时间戳
currentPage (integer, optional): 当前页 ,
currentResult (integer, optional): 查询开始位置 ,
data (Array[评论列表], optional),
order (string, optional),
showCount (integer, optional): 每页显示数据行数 ,
sortField (string, optional),
timeStamp (integer, optional): 当前分页的时间戳 ,
totalPage (integer, optional): 总页数 ,
totalResult (integer, optional): 总记录数
childResThemeCommentVoList (Array[评论列表], optional): 回复信息 ,
commentCreateTime (string, optional): 评论时间 ,
commentDesc (string, optional): 评论的内容 ,
commentUserId (integer, optional): 评论用户id ,
commentUserType (integer, optional): 评论用户类型(1-管理员 2-用户) ,
floorId (integer, optional): 评论楼层 ,
idCompanyHomeThemeComment (integer, optional): 评论id ,
logo (string, optional): 评论人的头像logo ,
nickName (string, optional): 评论人的昵称 ,
replyId (integer, optional): 是评论还是回复 replyId 0：评论 其他：回复
totalComment 评论总数
 *
 */
data class CommentData(var pageInfo: SubData?,
                       var timeStamp: String?,
                       var totalComment: String?,
                       var sortType: String?) {
    data class SubData(var showCount: String?,
                       var totalPage: String?,
                       var totalResult: String?,
                       var currentPage: String?,
                       var currentResult: String?,
                       var timeStamp: String?,
                       var sortField: String?,
                       var order: String?,
                       var data: ArrayList<SubComment>?)

    data class SubComment(var childResThemeCommentVoList: ArrayList<SubComment>?,
                          var commentCreateTime: String?,
                          var commentDesc: String?,
                          var commentUserId: String?,
                          var commentUserType: String?,
                          var floorId: String?,
                          var replayNickName: String?,
                          var idCompanyHomeThemeComment: String?,
                          var logo: String?,
                          var headImg:String?,
                          var numberOfReplies: String?,
                          var myselfComment: String?,//是否是自己发的评论或者回复 1:是 0:否 ,
                          var nickName: String?,
                          var replyId: String?)
}