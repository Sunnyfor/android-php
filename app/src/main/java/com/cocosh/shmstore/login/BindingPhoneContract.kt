package com.cocosh.shmstore.login

import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.login.model.Login

/**
 *
 * Created by zhangye on 2018/1/26.
 */
interface BindingPhoneContract {
    interface IView : IBaseView {
        //验证码发送结果回调
        fun onCodeResult(result: BaseModel<String>)

        fun onCheckedCodeResult(result: BaseModel<Login>)
    }

    interface IPresenter : IBasePresenter {
        //发送验证码
        fun sendCode(type: String, openId: String, phone: String)

        //验证验证码
        fun checkCode(code: String, openId: String, phone: String, type: String)
    }
}