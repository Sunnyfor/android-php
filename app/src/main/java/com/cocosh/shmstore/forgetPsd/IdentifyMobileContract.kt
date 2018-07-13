package com.cocosh.shmstore.forgetPsd

import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView

/**
 *
 * Created by zhangye on 2018/1/25.
 */
interface IdentifyMobileContract {
    interface IView : IBaseView {
        //验证码发送结果回调
        fun onCodeResult(result: BaseModel<String>)

        fun onCheckedCodeResult(result: BaseModel<String>)
    }

    interface IPresenter : IBasePresenter {
        //发送验证码
        fun sendCode(phone: String)

        //验证验证码
        fun checkCode(phone: String, code: String)
    }
}