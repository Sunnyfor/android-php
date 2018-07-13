package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/4/19.
 * type:0 父  1 子  2 title
 */
data class CommonModel(var listType: String?,
                       var infoList: ArrayList<SubCommonModel>) {
    data class SubCommonModel(var isExpand: Boolean? = false,
                              var itemType: String?,
                              var lowLevelUserName: String?,
                              var lowLevelUserNameSMNumber: String?,
                              var idxLowLevelUserSpecialInfo: String?,
                              var lowLevelUserSpecialType: String?,
                              var isBeDistribution: String?,
                              var entList: ArrayList<SubCommonModel>?)
}