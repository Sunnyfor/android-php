package com.cocosh.shmstore.mine.model

/**
 * 修改支付密码类型
 * Created by zhangye on 2018/8/7.
 */
enum class PayPassworType(var type:String) {
    INIT("init"), //初始化
    FORGOT("forgot"),//忘记找回
    RESET("reset") //重置
}