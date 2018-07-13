package com.cocosh.shmstore.login.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.BindingPhoneContract
import com.cocosh.shmstore.login.model.Login

/**
 * 绑定手机数据请求
 * Created by zhangye on 2018/1/26.
 */
class BindingLoader(private var baseActivity: BaseActivity, var iView: BindingPhoneContract.IView) {

    /**
     *   发送验证码
     */
    fun sendCode(type: String, openId: String, phone: String) {

        val map = HashMap<String, String>()
        map["type"] = type
        map["openId"] = openId
        map["phone"] = phone

        ApiManager.post(baseActivity, map, Constant.WEB_SEND_MESSGE, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                iView.onCodeResult(data)
            }

            override fun onFailed(e: Throwable) {
                iView.showError(0)
            }

            override fun onCatch(data: BaseModel<String>) {

            }

        })
    }

    /**
     * 校验验证码
     */
    fun checkedCode(code: String, openId: String, phone: String, type: String) {

        val map = HashMap<String, String>()
        map["code"] = code
        map["openId"] = openId
        map["phone"] = phone
        map["type"] = type
        map["deviceType"] = "android"

        ApiManager.post(baseActivity, map, Constant.WEB_AUTH_CODE, object : ApiManager.OnResult<BaseModel<Login>>() {
            override fun onSuccess(data: BaseModel<Login>) {
                iView.onCheckedCodeResult(data)
            }

            override fun onFailed(e: Throwable) {
                iView.showError(0)
            }

            override fun onCatch(data: BaseModel<Login>) {

            }

        })

    }
}
