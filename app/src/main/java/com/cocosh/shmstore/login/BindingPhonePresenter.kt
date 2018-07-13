package com.cocosh.shmstore.login

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.login.data.BindingLoader

/**
 * 第三方绑定手机的Presenter
 * Created by zhangye on 2018/1/26.
 */
class BindingPhonePresenter(baseActivity: BaseActivity, iView: BindingPhoneContract.IView) : BindingPhoneContract.IPresenter {

    override fun sendCode(type: String, openId: String, phone: String) {
        bindingLoader.sendCode(type, openId, phone)
    }

    override fun checkCode(code: String, openId: String, phone: String, type: String) {
        bindingLoader.checkedCode(code, openId, phone, type)
    }

    private val bindingLoader = BindingLoader(baseActivity, iView)

    override fun start() {
    }

}