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
data class CommentData(var id: String?,
                       var floor: String?,
                       var content: String?,
                       var portion:ArrayList<Portion>?,
                        var replies:String?,
                       var user:User?,
                       var time:String?) {

    data class Portion(var id: String?,
                       var parent: String?,
                       var parent_user: User?,
                       var content: String?,
                       var user: User?,
                       var time: String?)

    data class User(
            var smno:String?,
            var nickname:String?,
            var avatar:String?

    )
}