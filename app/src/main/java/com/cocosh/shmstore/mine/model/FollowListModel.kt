package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/10.
 * 关注列表
 */
data class FollowListModel(
        var bbs_id: String,
        var eid: String,
        var company: String,
        var name: String,
        var logo: String,
        var desc: String,  // 论坛说明
        var follow: String, // 是否已关注(默认都是已关注的)
        var follow_nums: String, // 关注人数
        var silence: String // 是否已禁言
)