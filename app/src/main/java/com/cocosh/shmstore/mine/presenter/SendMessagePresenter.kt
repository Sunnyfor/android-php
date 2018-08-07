package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader
import com.cocosh.shmstore.sms.data.SMSLoader
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType

/**
 *
 * Created by lmg on 2018/3/26.
 */
class SendMessagePresenter(var mActivity: BaseActivity, var mView: MineContrat.ISendMessageView) : MineContrat.ISendMessagePresenter {
    private val smsLoader = SMSLoader(mActivity)
    private var smsKey = ""

    override fun requestAuthCodeData(type: String, code: String) {
        loader.requestAuthCodeData(type, smsKey, code)
    }

    override fun requestSendMessageData(phone: String, boolean: Boolean) {
        smsLoader.sendCode(phone, SMSType.RESET_PAYPASS, object : ApiManager2.OnResult<BaseBean<SMS>>() {
            override fun onSuccess(data: BaseBean<SMS>) {
                smsKey = data.message?.smskey ?: ""
                mView.sendMessageData(data)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<SMS>) {
            }
        })
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}