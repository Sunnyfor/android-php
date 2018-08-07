package com.cocosh.shmstore.mine.model

/**
 * 修改登录密码校验
 * Created by zhangye on 2018/8/7.
 */
data class ResetPass(
        var ticket: String, // 检测临时票据
        var ts: String // 检测时间
)