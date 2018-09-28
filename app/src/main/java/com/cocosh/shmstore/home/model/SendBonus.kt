package com.cocosh.shmstore.home.model

/**
 *
 * Created by zhangye on 2018/6/7.
 */
data class SendBonus(
        var status: String?, //状态值
        var rp_id: String?,
        var no:String?,
        var pay_sn: String?, //订单编号
        var name: String, // 红包名称
        var pubtime: String?, //投放时间
        var amount: String?, //投放金额
        var price: String?,// 单个金额
        var total: String?, //投放数量
        var city: String?, //投放位置
        var addtime: String?, //投放时间
        var flowno: String?, //流水号
        var pay_type: String?, //付款方式
        var payment_time: String?, //付款时间
        var server_time: String?, // 服务器时间（status=0 才有）
        var reject: String?, //驳回理由（status=3 才有）
        var reject_time: String?, //驳回时间（status=3 才有）
        var url:String?,//服务器缺失字段
        var isExtend: Boolean //内部定义是否展开显示

)