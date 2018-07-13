package com.cocosh.shmstore.newCertification

import com.cocosh.shmstore.base.BaseActivity

/**
 * Created by cjl on 2018/2/9.
 */
class CertifimInforPresenter(activity: BaseActivity, view: ConfirmlnforContrat.IView) : ConfirmlnforContrat.IPresenter {
    override fun getConfirmResult(runningNumber: String) {
        loader.getConfirmResult(runningNumber)
    }

    override fun getLocalPay(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String, paymentPassword: String) {
        loader.getLocalPay(payChannel, amount, payOperatStatus, runningNumber, paymentPassword)
    }

    val loader = CertifirmInforLoader(activity, view)
    override fun start() {

    }

    override fun getCharge(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String) {
        loader.getCharge(payChannel, amount, payOperatStatus, runningNumber)
    }
}