package com.cocosh.shmstore.forgetPsd

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.sms.model.SMS

/**
 *
 * Created by zhangye on 2018/1/25.
 */
interface IdentifyMobileContract {
    interface IView : IBaseView {
        //验证码发送结果回调
        fun onCodeResult(result: BaseBean<SMS>)

        fun onForgetPassResult(result: BaseBean<String>)
    }

    interface IPresenter : IBasePresenter {
        //发送验证码
        fun sendCode(phone: String)

        //验证验证码
        fun forgetPass(phone: String, password: String,smsCode:String)
    }
}