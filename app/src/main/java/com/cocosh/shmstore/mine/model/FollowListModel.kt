package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/10.
 * 关注列表
 */
data class FollowListModel(var list: ArrayList<Follow>, var timeStamp: String?) {
    class Follow(
            var followCount: String?,
            var companyName: String?,
            var companyLogo: String?,
            var idCompanyInfoBase: String?
    )
}