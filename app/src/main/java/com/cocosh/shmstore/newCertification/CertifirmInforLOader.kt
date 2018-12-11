package com.cocosh.shmstore.newCertification

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.PayResultModel
import com.cocosh.shmstore.utils.DigestUtils
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.StringUtils
import java.text.DecimalFormat

/**
 * Created by cjl on 2018/2/8.
 * 支付接口
 */
class CertifirmInforLoader(val activity: BaseActivity, val view: ConfirmlnforContrat.IView) {
    fun getCharge(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String) {
        val map = HashMap<String, String>()
        //支付使用的第三方支付渠道,'wx'-微信支付,'alipay'-支付宝)
        map["pay_type"] = payChannel
        map["amount"] = amount
        //业务种类 (必填,'1'-新媒人认证,'2'-发红包支付,'3'-余额充值支付,'4'-购买支付)
        map["kind"] = payOperatStatus
        //订单编号 非必传字段
        map["data"] = runningNumber
        ApiManager2.post(activity, map, Constant.PAYMENT, object : ApiManager2.OnResult<String>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: String) {
                view.confirmResult(data)
            }


            override fun onCatch(data: String) {
                LogUtil.d(data)
            }
        })
    }

    fun getConfirmResult(runningNumber: String,kind:String) {
        val map = HashMap<String, String>()
        map["kind"]	= kind
               // 业务种类 (必填,'1'-新媒人认证,'2'-发红包支付,'3'-余额充值支付,'4'-购买支付)
        map["sn"] = runningNumber
        ApiManager2.post(0, activity, map, Constant.PAYMENT_CHECK, object : ApiManager2.OnResult<BaseBean<PayResultModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<PayResultModel>) {
                view.payConfirmResult(data)
            }


            override fun onCatch(data: BaseBean<PayResultModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    fun getLocalPay(amount: String, payOperatStatus: String, runningNumber: String, paymentPassword: String) {
        val map = HashMap<String, String>()
        map["ts"] = StringUtils.getTimeStamp()
        map["amount"] = DecimalFormat("0.00").format(amount.toFloat())
        //业务种类 (必填,'1'-新媒人认证,'2'-发红包支付,'3'-余额充值支付,'4'-购买支付)
        map["kind"] = payOperatStatus
        //订单编号 非必传字段
        map["data"] = runningNumber
        map["paypass"] = DigestUtils.sha1(DigestUtils.md5(paymentPassword) + map["ts"])

        val url = if(amount =="0") Constant.SMPAY_RP else Constant.SMPAY

        ApiManager2.post(activity, map, url, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
                val baseBean = BaseBean<String>()
                baseBean.status = code
                baseBean.message = message
                view.localPay(baseBean)
            }

            override fun onSuccess(data: BaseBean<String>) {
                view.localPay(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }
}