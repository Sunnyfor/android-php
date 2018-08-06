package com.cocosh.shmstore.forgetPsd.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.forgetPsd.IdentifyMobileContract
import com.cocosh.shmstore.forgetPsd.data.IdentifyMobileLoader
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.sms.data.SMSLoader
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType

/**
 *
 * Created by zhangye on 2018/1/25.
 */
class IdentifyMoboilePresenter(baseActivity: BaseActivity, private var iView: IdentifyMobileContract.IView) : IdentifyMobileContract.IPresenter {
    private var smskey = ""
    private val loader = IdentifyMobileLoader(baseActivity, iView)
    private val smsLoader = SMSLoader(baseActivity)

    override fun start() {

    }

    /**
     * 发送验证码
     */
    override fun sendCode(phone: String) {
        smsLoader.sendCode(phone, SMSType.FORGOT_PASS, object : ApiManager2.OnResult<BaseBean<SMS>>() {
            override fun onSuccess(data: BaseBean<SMS>) {
                smskey = data.message?.smskey ?: ""
                iView.onCodeResult(data)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<SMS>) {
            }

        })
    }

    /**
     * 检查验证码
     */
    override fun forgetPass(phone: String, password: String, smsCode: String) {
        loader.forgetPass(phone, password, smsCode,smskey,object :ApiManager2.OnResult<BaseBean<String>>(){
            override fun onSuccess(data: BaseBean<String>) {
                iView.onForgetPassResult(data)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}