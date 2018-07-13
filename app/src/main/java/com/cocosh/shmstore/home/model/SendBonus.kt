package com.cocosh.shmstore.home.model

/**
 *
 * Created by zhangye on 2018/6/7.
 */
data class SendBonus(
        var list: ArrayList<Bonus>?, //红包分页数据
        var timeStamp: String? //当前分页的时间戳
) {
    data class Bonus(
            var advertisementBanner: String?, //广告banner
            var advertisementBaseType: Int, //广告类型
            var androidUrl: String?, //安卓下载url
            var createTime: String?, // 订单创建时间
            var htmlUrl: String, //H5地址
            var idAdvertisementBaseInfo: String?, // 广告id
            var orderNumber: String?, //订单编号
            var orderStatus: String?, //订单状态
            var redPacketCount: String?, //红包投放数量
            var redPacketId: String?, //红包ID
            var redPacketMoney: String?, //红包投放金额
            var redPacketName: String?, //红包名称
            var redPacketOrderId: String?, //红包订单ID
            var redPacketOrderPayType: Int, //支付方式 1：首媒支付 2： 支付宝支付 3： 微信支付
            var redPacketOrderRunningNum: String?,//支付流水号
            var redPacketStartTime: String, //投放时间
            var redPacketStatus: Int, //红包的状态(1已抢，2无红包，0未抢)
            var redPayTime: String?, //付款时间
            var regionName: String?, //投放位置
            var rejectTime: String?,//驳回时间
            var singleRedPacketAmount: String?, //单个红包金额
            var typeInfo: String?, // 红包类型（1.大众 2.精准 3.粉丝）
            var endTimeStamp:Long,
            var vareaCode: String?,
            var isExtend:Boolean
    ){
        override fun equals(other: Any?): Boolean {
            if (other is Bonus){
                if (other.redPacketOrderId == redPacketOrderId){
                    return true
                }
            }
            return false
        }

        override fun hashCode(): Int {
            var result = advertisementBanner?.hashCode() ?: 0
            result = 31 * result + advertisementBaseType
            result = 31 * result + (androidUrl?.hashCode() ?: 0)
            result = 31 * result + (createTime?.hashCode() ?: 0)
            result = 31 * result + htmlUrl.hashCode()
            result = 31 * result + (idAdvertisementBaseInfo?.hashCode() ?: 0)
            result = 31 * result + (orderNumber?.hashCode() ?: 0)
            result = 31 * result + (orderStatus?.hashCode() ?: 0)
            result = 31 * result + (redPacketCount?.hashCode() ?: 0)
            result = 31 * result + (redPacketId?.hashCode() ?: 0)
            result = 31 * result + (redPacketMoney?.hashCode() ?: 0)
            result = 31 * result + (redPacketName?.hashCode() ?: 0)
            result = 31 * result + (redPacketOrderId?.hashCode() ?: 0)
            result = 31 * result + redPacketOrderPayType
            result = 31 * result + (redPacketOrderRunningNum?.hashCode() ?: 0)
            result = 31 * result + redPacketStartTime.hashCode()
            result = 31 * result + redPacketStatus
            result = 31 * result + (redPayTime?.hashCode() ?: 0)
            result = 31 * result + (regionName?.hashCode() ?: 0)
            result = 31 * result + (rejectTime?.hashCode() ?: 0)
            result = 31 * result + (singleRedPacketAmount?.hashCode() ?: 0)
            result = 31 * result + (typeInfo?.hashCode() ?: 0)
            result = 31 * result + endTimeStamp.hashCode()
            result = 31 * result + (vareaCode?.hashCode() ?: 0)
            return result
        }
    }
}