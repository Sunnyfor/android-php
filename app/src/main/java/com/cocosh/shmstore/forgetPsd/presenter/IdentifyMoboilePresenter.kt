package com.cocosh.shmstore.forgetPsd.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.forgetPsd.IdentifyMobileContract
import com.cocosh.shmstore.forgetPsd.data.IdentifyMobileLoader

/**
 *
 * Created by zhangye on 2018/1/25.
 */
class IdentifyMoboilePresenter(baseActivity: BaseActivity, iView: IdentifyMobileContract.IView) : IdentifyMobileContract.IPresenter {

    private val loader = IdentifyMobileLoader(baseActivity, iView)

    override fun start() {

    }

    /**
     * 发送验证码
     */
    override fun sendCode(phone: String) {
        loader.sendCode(phone)
    }

    /**
     * 检查验证码
     */
    override fun checkCode(phone: String, code: String) {
        loader.checkedCode(phone, code)
    }
}