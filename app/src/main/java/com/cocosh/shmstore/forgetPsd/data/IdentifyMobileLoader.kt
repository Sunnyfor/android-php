package com.cocosh.shmstore.forgetPsd.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.forgetPsd.IdentifyMobileContract
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.register.RegisterContract

/**
 * 验证手机
 * Created by zhangye on 2018/1/25.
 */
class IdentifyMobileLoader(private var baseActivity: BaseActivity, var iView: IdentifyMobileContract.IView) {

    /**
     *   发送验证码
     */
    fun sendCode(phone: String) {

        val map = HashMap<String, String>()
        map["mobile"] = phone

        ApiManager.post(baseActivity, map, Constant.IDENTIFY_MOBILE_GET_CODE, object : ApiManager.OnResult<BaseModel<String>>() {
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
    fun checkedCode(phone: String, code: String) {

        val map = HashMap<String, String>()
        map["userName"] = phone
        map["code"] = code

        ApiManager.get(0, baseActivity, map, Constant.IDENTIFY_MOBILE_CHECKED_CODE, object : ApiManager.OnResult<BaseModel<String>>() {

            override fun onSuccess(data: BaseModel<String>) {
                iView.onCheckedCodeResult(data)
            }

            override fun onFailed(e: Throwable) {
                iView.showError(0)
            }

            override fun onCatch(data: BaseModel<String>) {

            }

        })

    }

}