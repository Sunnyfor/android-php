package com.cocosh.shmstore.login.model

import xiaofei.library.comparatorgenerator.Criterion
import xiaofei.library.comparatorgenerator.Order
import xiaofei.library.datastorage.annotation.ObjectId

/**
 * 历史帐号实体类
 * Created by zhangye on 2018/1/4.
 */
data class LoginHistory(@ObjectId var phone: String) {
    @Criterion(priority = 1, order = Order.DESCENDING) // order默认是升序
    val timer = System.currentTimeMillis() / 1000
}