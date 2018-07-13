package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/6/19.
 *
 * dateTime (string, optional): 交易时间 ,
detailDesc (string, optional): 交易描述 ,
detailId (integer, optional): 交易ID ,
money (number, optional): 交易金额 ,
runningNum (string, optional): 流水号
 viewType 1 ,2
 *
 */
class ProfitModel() {
    var dateTime: String? = null
    var detailDesc: String? = null
    var detailId: String? = null
    var money: String? = null
    var runningNum: String? = null
    var viewType: Int? = 1
}