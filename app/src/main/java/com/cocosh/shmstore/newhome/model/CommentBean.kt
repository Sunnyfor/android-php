package com.cocosh.shmstore.newhome.model

data class CommentBean(
        var id: String, // 评论id
        var smno: String, // 用户-首媒号
        var nickname: String, // 用户-昵称
        var avatar: String, // 用户-头像
        var stars: Int, // 评论星数,最小1星,最大5星
        var words: String, // 评论内容
        var time: String)