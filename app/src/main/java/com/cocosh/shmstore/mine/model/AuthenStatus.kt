package com.cocosh.shmstore.mine.model

/**
 * 认证状态
 * Created by zhangye on 2018/3/22.
 */
enum class AuthenStatus(var type: Int) {

    //新媒人认证状态
    NEW_MATCHMAKER_NO(0), //未认证
    NEW_MATCHMAKER_WAIT(1),//待付款
    NEW_MATCHMAKER_OK(2),   //已认证
//    0'-未认证,'1'-待付款,'2'-已认证
    //    PRE_AUTH("PRE_AUTH"),   //认证中
//    EXIT("3"),//已注销
//    CANCEL("4"),//已作废


    //    AUTH_FAILED("6"), //认证失败，
//
//    //企业主激活
    BUSINESS_NO(0), //未认证
    BUSINESS_ACTIVE(1), //待激活
    BUSINESS_EXAMINE(2),//审核中
    BUSINESS_FAIL(3), //激活失败
    BUSINESS_OK(4), // //已激活

//
    //个人认证状态
    PERSION_NO(0), //实人未认证
    PERSION_OK(1), //实人已认证

    //服务商认证状态
    SERVER_DEALER_NO(0), //未认证
    SERVER_DEALER_ING(1), //认证中
    SERVER_DEALER_FAIL(2), //认证失败
    SERVER_DEALER_OK(3) //已认证
//    //充值 状态
//    RECHARGE("RECHARGE"), //充值
//    PUT_FORWARD("PUT_FORWARD"), //提现
//    PAYMENT("PAYMENT"), //消费
//    RETURN_TO_INCOME("RETURN_TO_INCOME"), //转入
//    SALE("SALE"), //创建
//    REFUND("REFUND"), //退款
//    SEND_RED_PACKET("SEND_RED_PACKET") //发红包
}