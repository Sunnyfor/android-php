package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/5/8.
 *
 *  arriveTime (string, optional): 提现到达时间 ,
bankInfo (string, optional): 提现银行卡名称 ,
drawTime (string, optional): 提现时间 ,
profit (number, optional): 提现金额 ,
ratioMoney (number, optional): 提现手续费 ,
serialNum (string, optional): 流水号 ,
status (string, optional): 提现状态
 *
 */
data class DrawResultData(var arriveTime: String?,
                          var bankInfo: String?,
                          var drawTime: String?,
                          var money: String?,
                          var ratioMoney: String?,
                          var serialNum: String?,
                          var status: String?)