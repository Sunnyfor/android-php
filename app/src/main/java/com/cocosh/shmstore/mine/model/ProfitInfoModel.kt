package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/6/19.
 *
accountPeriodStr (string, optional): 账期说明 ,
accumulative (number, optional): 累计金额 ,
available (number, optional): 可提现金额 ,
profit (number, optional): 收益金额
 *
 */
data class ProfitInfoModel(var tip: String?,
                           var accumulate: String?,
                           var available: String?,
                           var current: String?
)