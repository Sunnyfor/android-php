package com.cocosh.shmstore.mine.ui.mywallet

/**
 * Created by lmg on 2018/6/6.
 */
class ChargeModel {
    var amount: String? = null
    var amountRefunded: String? = null
    var amountSettle: String? = null
    var app: String? = null
    var body: String? = null
    var channel: String? = null
    var clientIp: String? = null
    var created: String? = null
    var credential: CredentialBean? = null
    var currency: String? = null
    var description: String? = null
    var extra: ExtraBean? = null
    var failureCode: String? = null
    var failureMsg: String? = null
    var id: String? = null
    var livemode: Boolean = false
    var metadata: MetadataBean? = null
    var `object`: String? = null
    var orderNo: String? = null
    var paid: Boolean = false
    var refunded: Boolean = false
    var refunds: RefundsBean? = null
    var subject: String? = null
    var timeExpire: String? = null
    var timePaid: String? = null
    var timeSettle: String? = null
    var transactionNo: String? = null

    class RefundsBean {
        var hasMore: Boolean = false
        var `object`: String? = null
        var url: String? = null
        var data: List<DataBean>? = null
    }

    class DataBean {
        var amount: String? = null
        var charge: String? = null
        var chargeOrderNo: String? = null
        var created: String? = null
        var description: String? = null
        var failureCode: String? = null
        var failureMsg: String? = null
        var fundingSource: String? = null
        var id: String? = null
        var instanceURL: String? = null
        var metadata: MetadataBeanX? = null
        var `object`: String? = null
        var orderNo: String? = null
        var status: String? = null
        var succeed: Boolean = false
        var timeSucceed: String? = null
        var transactionNo: String? = null
    }

    class MetadataBean {}
    class CredentialBean {
        var `object`: String? = null
        var wx: WXBean? = null
    }

    class WXBean {
        var appId: String? = null
        var partnerId: WXBean? = null
        var prepayId: String? = null
        var nonceStr: WXBean? = null
        var timeStamp: String? = null
        var packageValue: WXBean? = null
        var sign: String? = null
    }

    class ExtraBean {}
    class MetadataBeanX {}
}