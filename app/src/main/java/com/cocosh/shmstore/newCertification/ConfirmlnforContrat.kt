package com.cocosh.shmstore.newCertification

import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.mine.model.PayResultModel

/**
 * Created by cjl on 2018/2/8.
 */
interface ConfirmlnforContrat {
    interface IView : IBaseView {
        fun confirmResult(result: String)
        fun payConfirmResult(result: BaseModel<PayResultModel>)
        fun localPay(result: BaseModel<String>)
    }

    interface IPresenter : IBasePresenter {
        fun getCharge(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String)
        fun getConfirmResult(runningNumber: String)
        fun getLocalPay(payChannel: String, amount: String, payOperatStatus: String, runningNumber: String, paymentPassword: String)
    }
}