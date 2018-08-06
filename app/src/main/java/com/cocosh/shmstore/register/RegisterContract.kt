package com.cocosh.shmstore.register

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.sms.model.SMS

/**
 *
 * Created by zhangye on 2018/1/25.
 */
interface RegisterContract {
    interface IView : IBaseView {
        //验证码发送结果回调
        fun onCodeResult(data: BaseBean<SMS>)

        fun onRegister(result: BaseBean<Login2>)
    }

    interface IPresenter : IBasePresenter {
        //发送验证码
        fun sendCode(phone: String)

        //验证验证码
        fun register(phone: String, password: String, smscode:String)
    }
}