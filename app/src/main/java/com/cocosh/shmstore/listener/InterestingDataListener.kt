package com.cocosh.shmstore.listener

import com.cocosh.shmstore.mine.model.InterestModel

/**
 * Created by lmg on 2018/4/12.
 */
interface InterestingDataListener {
    fun dataCallBack(listPosition:Int,itemPosition:Int,data: InterestModel.Data)
}