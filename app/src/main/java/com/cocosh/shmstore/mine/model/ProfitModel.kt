package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/6/19.
 *
 * time (string, optional): 交易时间 ,
detailDesc (string, optional): 交易描述 ,
id (integer, optional): 交易ID ,
profit (number, optional): 交易金额 ,
flowno (string, optional): 流水号
 viewType 1 ,2
 *
 */
class ProfitModel {
    var time: String? = null
    var date:String? = null
    var detailDesc: String? = null
    var id: String? = null
    var profit: String? = null
    var flowno: String? = null
    var fine_type:String? = null
    var income_type:String? = null
    var viewType: Int? = 1
}