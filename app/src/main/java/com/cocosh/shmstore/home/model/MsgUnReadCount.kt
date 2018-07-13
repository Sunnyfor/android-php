package com.cocosh.shmstore.home.model

/**
 * Created by lmg on 2018/6/15.
 *
 * unreadRedPacketMessageCount (integer, optional),
unreadReplyMessageCount (integer, optional),
unreadSystemMessageCount (integer, optional)
 *
 */
data class MsgUnReadCount(var unreadRedPacketMessageCount: Int?,
                          var unreadReplyMessageCount: Int?,
                          var unreadSystemMessageCount: Int?)