package com.cocosh.shmstore.newCertification

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.PayResultModel
import com.cocosh.shmstore.utils.LogUtil

/**
 * Created by cjl on 2018/2/8.
 * 支付接口
 */
class CertifirmInforLoader(val activity: BaseActivity, val view: ConfirmlnforContrat.IView) {
    fun getCharge(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String) {
        val map = HashMap<String, String>()
        //支付使用的第三方支付渠道 LOCAL_ACC 本地支付, PINGPP_WX 微信app支付,PINGPP_WX_PUB 微信电脑端支付, PINGPP_ALIPAY 支付宝app支付, PINGPP_ALIPAY_PC 支付宝电脑端支付
        map["payChannel"] = payChannel
        map["amount"] = amount
        //RECHARGE (1,"充值"),PUT_FORWARD (2,"提现"),PAYMENT(3,"消费"), RETURN_TO_INCOME(4,"转入"),SALE(5,"创建"),REFUND(6,"退款"), SEND_RED_PACKET(7,"发红包")
        map["payOperatStatus"] = payOperatStatus
        //订单编号 非必传字段
        map["runningNumber"] = runningNumber
        ApiManager.post(activity, map, Constant.CASH_PAY, object : ApiManager.OnResult<String>() {
            override fun onSuccess(data: String) {
                view.confirmResult(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message ?: "")
            }

            override fun onCatch(data: String) {
                LogUtil.d(data)
            }
        })
    }

    fun getConfirmResult(runningNumber: String) {
        val map = HashMap<String, String>()
        map["runningNumber"] = runningNumber
        ApiManager.get(0, activity, map, Constant.PAY_RESULT, object : ApiManager.OnResult<BaseModel<PayResultModel>>() {
            override fun onSuccess(data: BaseModel<PayResultModel>) {
                view.payConfirmResult(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message ?: "")
            }

            override fun onCatch(data: BaseModel<PayResultModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    fun getLocalPay(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String, paymentPassword: String) {
        val map = HashMap<String, String>()
        //支付使用的第三方支付渠道 LOCAL_ACC 本地支付, PINGPP_WX 微信app支付,PINGPP_WX_PUB 微信电脑端支付, PINGPP_ALIPAY 支付宝app支付, PINGPP_ALIPAY_PC 支付宝电脑端支付
        map["payChannel"] = payChannel
        map["amount"] = amount
        //RECHARGE (1,"充值"),PUT_FORWARD (2,"提现"),PAYMENT(3,"消费"), RETURN_TO_INCOME(4,"转入"),SALE(5,"创建"),REFUND(6,"退款"), SEND_RED_PACKET(7,"发红包")
        map["payOperatStatus"] = payOperatStatus
        //订单编号 非必传字段
        map["runningNumber"] = runningNumber
        map["paymentPassword"] = paymentPassword
        ApiManager.post(activity, map, Constant.LOCAL_PAY, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                view.localPay(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message ?: "")
            }

            override fun onCatch(data: BaseModel<String>) {
                LogUtil.d(data.toString())
            }
        })
    }
}