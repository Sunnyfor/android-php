package com.cocosh.shmstore.mine.model

/**
 * 认证状态
 * Created by zhangye on 2018/3/22.
 */
enum class AuthenStatus(var type: String) {
    UNCERTIFIED("1"), //未认证
    PRE_DRAFT("2"), //待付款
    PRE_AUTH("PRE_AUTH"),   //认证中
    EXIT("3"),//已注销
    CANCEL("4"),//已作废
    PRE_PASS("5"),   //已认证
    AUTH_FAILED("6"), //认证失败，

    //企业主激活
    NOT_ACTIVE("NOT_ACTIVE"), //待激活
    AUDIT("AUDIT"), // 审核中
    ALREADY_ACTIVATED("ALREADY_ACTIVATED"), //已激活
    REJECT("REJECT"), //审核驳回
    ENT_OPEN_TYPE("ent_open_type"), //打开类型 111 未激活

    //个人认证状态
    PERSION_NO("0"), //实人未认证
    PERSION_OK("1"), //实人已认证
    //充值 状态
    RECHARGE("RECHARGE"), //充值
    PUT_FORWARD("PUT_FORWARD"), //提现
    PAYMENT("PAYMENT"), //消费
    RETURN_TO_INCOME("RETURN_TO_INCOME"), //转入
    SALE("SALE"), //创建
    REFUND("REFUND"), //退款
    SEND_RED_PACKET("SEND_RED_PACKET") //发红包
}