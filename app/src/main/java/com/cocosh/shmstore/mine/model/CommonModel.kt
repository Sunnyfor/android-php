package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/4/19.
 * type:0 父  1 子  2 title
 */
data class CommonModel(
        var name: String?,
        var smno: String?,
        var isExpand: Boolean? = false,
        var itemType: String,
        var new: ArrayList<CommonModel>)