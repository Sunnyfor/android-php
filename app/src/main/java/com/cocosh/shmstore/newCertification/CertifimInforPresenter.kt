package com.cocosh.shmstore.newCertification

import com.cocosh.shmstore.base.BaseActivity

/**
 * Created by cjl on 2018/2/9.
 */
class CertifimInforPresenter(activity: BaseActivity, view: ConfirmlnforContrat.IView) : ConfirmlnforContrat.IPresenter {
    //业务种类 (必填,'1'-新媒人认证,'2'-发红包支付,'3'-余额充值支付,'4'-购买支付)
    override fun getConfirmResult(runningNumber: String,kind:String) {
        loader.getConfirmResult(runningNumber,kind)
    }

    override fun getLocalPay(amount: String, payOperatStatus: String, runningNumber: String, paymentPassword: String) {
        loader.getLocalPay(amount, payOperatStatus, runningNumber, paymentPassword)
    }

    val loader = CertifirmInforLoader(activity, view)
    override fun start() {

    }

    override fun getCharge(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String) {
        loader.getCharge(payChannel, amount, payOperatStatus, runningNumber)
    }
}