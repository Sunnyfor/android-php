package com.cocosh.shmstore.sms.type

/**
 * 短信类型枚举
 * Created by zhangye on 2018/8/2.
 */
enum class SMSType(var value: String) {
    REGISTER("register"), //注册
    FORGOT_PASS("forgot_pass"),//忘记密码
    RESET_PASS("reset_pass"), //重置密码
    INVITE("invite"), //分享接受邀约
    INIT_PAYPASS("init_paypass"), //设置支付密码
    FORGOT_PAYPASS("forgot_paypass"), //忘记支付密码
    RESET_PAYPASS("reset_paypass"),//重置支付密码
    ENT_CASH("ent_cash"),//企业主提现
    CASH("cash"), //提现
    BANK("bankphone") //绑定银行卡
}