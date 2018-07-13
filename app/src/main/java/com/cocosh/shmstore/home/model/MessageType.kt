package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/15.
 */
enum class MessageType(var type: String) {
    /**
     * 文本消息
     */
    TEXT_MESSAGE("1"),
    /**
     * 图文消息
     */
    GRAPHIC_MESSAGE("2"),
    /**
     * 提现发起消息
     */
    PUT_FORWARD_LAUNCH_MESSAGE("3"),
    /**
     * 提现到账消息
     */
    PUT_FORWARD_TO_ACCOUNT_MESSAGE("4"),
    /**
     * 身份审核消息
     */
    REVIEW_MESSAGE("5"),

    /**
     * 投放中
     */
    RED_PACKET_IN_THE_LAUNCH_MESSAGE("6"),

    /**
     * 投放结束
     */
    RED_PACKET_PUT_TO_THE_END_MESSAGE("7"),

    /**
     * 投放结束(有退款)
     */
    RED_PACKET_PUT_TO_THE_END_RETURN_MONEY_MESSAGE("8"),

    /**
     * 赠送的红包被领取
     */
    RED_PACKET_GRANT_AND_RECEIVE_MESSAGE("9"),
    /**
     * 赠送的红包超时退回
     */
    RED_PACKET_GIVE_BACK_MESSAGE("10"),
    /**
     *红包审核失败
     */
    RED_PACKET_REJECT_MESSAGE("11"),
    /**
     *回复评论消息
     */
    REPLY_MESSAGE("12"),
    /**
     *评论被删除消息
     */
    REPLY_DELETE_MESSAGE("13")
}