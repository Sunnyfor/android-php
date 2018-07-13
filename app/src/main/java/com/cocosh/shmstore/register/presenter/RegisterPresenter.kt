package com.cocosh.shmstore.register.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.register.RegisterContract
import com.cocosh.shmstore.register.data.RegisterLoader

/**
 *
 * Created by zhangye on 2018/1/25.
 */
class RegisterPresenter(baseActivity: BaseActivity, iView: RegisterContract.IView) : RegisterContract.IPresenter {

    private val registerLoaderr = RegisterLoader(baseActivity, iView)

    override fun start() {
    }

    /**
     * 发送验证码
     */
    override fun sendCode(phone: String) {
        registerLoaderr.sendCode(phone)
    }

    /**
     * 检查验证码
     */
    override fun checkCode(phone: String, code: String) {
        registerLoaderr.checkedCode(phone, code)
    }
}