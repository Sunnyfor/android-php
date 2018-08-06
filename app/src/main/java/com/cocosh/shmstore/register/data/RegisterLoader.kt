package com.cocosh.shmstore.register.data

import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.register.RegisterContract
import com.cocosh.shmstore.utils.DigestUtils

/**
 * 注册请求发送
 * Created by zhangye on 2018/1/25.
 */
class RegisterLoader(private var baseActivity: BaseActivity, var iView: RegisterContract.IView) {

//    /**
//     *   发送验证码
//     */
//    fun sendCode(phone: String) {
//        val map = HashMap<String, String>()
//        map["mobile"] = phone
//
//        ApiManager.post(baseActivity, map, Constant.REGISTER_SEND_CODE, object : ApiManager.OnResult<BaseModel<String>>() {
//            override fun onSuccess(data: BaseModel<String>) {
//                iView.onCodeResult(data)
//            }
//
//            override fun onFailed(e: Throwable) {
//                iView.showError(0)
//            }
//
//            override fun onCatch(data: BaseModel<String>) {
//
//            }
//
//        })
//    }
//
//    /**
//     * 校验验证码
//     */
//    fun checkedCode(phone: String, code: String) {
//        val map = HashMap<String, String>()
//        map["userName"] = phone
//        map["code"] = code
//        ApiManager.get(0, baseActivity, map, Constant.REGISTER_AUTH_CODE, object : ApiManager.OnResult<BaseModel<String>>() {
//            override fun onSuccess(data: BaseModel<String>) {
//                iView.onRegister(data)
//            }
//
//            override fun onFailed(e: Throwable) {
//                iView.showError(0)
//            }
//
//            override fun onCatch(data: BaseModel<String>) {
//
//            }
//
//        })
//
//    }

    /*注册*/
    fun regist(phone:String, passwd:String, smscode:String, smskey:String,onResult: ApiManager2.OnResult<BaseBean<Login2>>){
            val map = HashMap<String, String>()
            map["phone"] = phone
            map["passwd"] = DigestUtils.md5(passwd)
            map["smscode"] = smscode
            map["smskey"] = smskey
            map["client"] = Constant.CLIENT
            map["device"] =  SmApplication.getApp().getDeviceID()
            ApiManager2.post(baseActivity, map, Constant.REGISTER,onResult)
    }
}