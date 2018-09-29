package com.cocosh.shmstore.register.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.mine.model.MemberEntrance2
import com.cocosh.shmstore.register.RegisterContract
import com.cocosh.shmstore.register.data.RegisterLoader
import com.cocosh.shmstore.sms.data.SMSLoader
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.UserManager2

/**
 *
 * Created by zhangye on 2018/1/25.
 */
class RegisterPresenter(var baseActivity: BaseActivity, private var iView: RegisterContract.IView) : RegisterContract.IPresenter {

    private val registerLoaderr = RegisterLoader(baseActivity, iView)
    private val smsLoader = SMSLoader(baseActivity)

    private var smskey = ""
    override fun start() {
    }

    /**
     * 发送验证码
     */
    override fun sendCode(phone: String) {
        smsLoader.sendCode(phone, SMSType.REGISTER, object : ApiManager2.OnResult<BaseBean<SMS>>() {
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
    override fun register(phone: String, password: String, smscode: String) {
        registerLoaderr.regist(phone, password, smscode, smskey, object : ApiManager2.OnResult<BaseBean<Login2>>() {
            override fun onSuccess(data: BaseBean<Login2>) {
                data.message?.let {
                    UserManager2.setLogin(it)
                    updateProfile(data)
                }
            }
            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<Login2>) {
            }
        })

    }

    /**
     * 更新用户资料
     */
    private fun updateProfile(login:BaseBean<Login2>) {
        UserManager2.loadMemberEntrance(baseActivity, object : ApiManager2.OnResult<BaseBean<MemberEntrance2>>() {
            override fun onSuccess(data: BaseBean<MemberEntrance2>) {
                UserManager2.setMemberEntrance(data.message)
                iView.onRegister(login)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<MemberEntrance2>) {
            }

        })
    }
}