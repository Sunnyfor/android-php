package com.cocosh.shmstore.mine.ui.authentication

/**
 * Created by lmg on 2018/4/20.、
 * 列表展示状态
 */
enum class CommonType(var type: String) {
    /**
     * CERTIFICATION_0 拓展企业主
     * CERTIFICATION_1 拓展个人用户
     * CERTIFICATION_2 平台分配个人用户
     * FACILITTOR_0 拓展新媒人
     * FACILITTOR_1 拓展企业主
     * FACILITTOR_2 拓展个人用户
     * FACILITTOR_3 平台分配企业主
     * FACILITTOR_4 平台分配个人用户
     */
    CERTIFICATION_0("CERTIFICATION_0"),
    CERTIFICATION_1("CERTIFICATION_1"),
    CERTIFICATION_2("CERTIFICATION_2"),
    FACILITTOR_0("FACILITTOR_0"),
    FACILITTOR_1("FACILITTOR_1"),
    FACILITTOR_2("FACILITTOR_2"),
    FACILITTOR_3("FACILITTOR_3"),
    FACILITTOR_4("FACILITTOR_4"),

    CERTIFICATION_INCOME("CERTIFICATION_INCOME"),//新媒人收益
    FACILITATOR_INCOME("FACILITATOR_INCOME"),//服务商收益

    CERTIFICATION_OUTTOWALLET("CERTIFICATION_OUTTOWALLET"),//新媒人
    FACILITATOR_OUTTOWALLET("FACILITATOR_OUTTOWALLET"),//企业
    REDACCOUNT_OUTTOWALLET("REDACCOUNT_OUTTOWALLET"),//红包账户

    //订单详情
    //待付款
    ORDER_NOPAY("order_nopay"),
    //待发货
    ORDER_NOSEND("order_nosend"),
    //确认收货
    ORDER_GET("order_get"),
    //交易完成
    ORDER_FINISH("order_finish"),


    /**
     * 设置密码
     */
    ADDBANK_SETPWD("addbank_setpwd"),//添加银行卡时设置密码
    SETTING_SETPWD("setting_setpwd")//设置中 修改或设置密码

}