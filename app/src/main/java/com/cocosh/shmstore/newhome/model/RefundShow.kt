package com.cocosh.shmstore.newhome.model

data class RefundShow(
        var number: String, //退货单号
        var order_type: String, //订单类型：0退款，1退货退款
        var reason: String?, //退款原因
        var status: String, //退款状态：0：等待商家处理；1：退货/款成功；2：不同意退货/款
        var pay_type: String,//支付类型
        var actual: String, //退款金额
        var discount: String,//退款红包金额
        var explain: String, //退款说明
        var refund_image: ArrayList<String>?, //退款凭证
        var express_status: String,//退货状态 0：申请发货；1：等待买家发货；2：买家已发货；3：商家已收货；4：商家拒绝收货
        var express_code: String, //发货单号
        var express_type: String, //物流公司编号
        var refuse_reason: String,//拒绝退款原因
        var refuse_explain: String,//拒绝退款说明
        var addtime: String
)