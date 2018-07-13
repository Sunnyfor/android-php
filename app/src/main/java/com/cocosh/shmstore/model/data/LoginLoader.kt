package com.cocosh.shmstore.model.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.login.ILoginContract
import com.cocosh.shmstore.model.UserModel
import com.cocosh.shmstore.model.source.UserModelSource
import java.util.*

/**
 * 登录网络请求
 * Created by zhangye on 2017/10/20.
 */
class LoginLoader(var activity: BaseActivity) : UserModelSource {

    override fun saveUser(user: UserModel) {

    }


    /**
     * 登录请求
     */
    override fun login(phone: String, password: String, loginView: ILoginContract.IView) {
        val map = HashMap<String, String>()
        map.put("cMob", phone.trim())
        map.put("cPass", password.trim())
        map.put("cLongitude", "")
        map.put("cLatitude", "")
        map.put("deviceId", "10010")
        map.put("deviceType", "0")

//        ApiManager.post(activity, map, Constant.LOGIN, object : ApiManager.OnResult<UserManager> {
//            override fun onSuccess(data: BaseModel<UserManager>) {
//
//                loginView.loginResult(data.information)
//
//                if (data.isSuccess()) {
//                    /**
//                     * 保存用户信息
//                     */
//                    saveUser(data.datas!!)
//                    activity.finish()
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//                loginView.loginResult(e.localizedMessage)
//            }
//
//            override fun onCatch(data: BaseModel<UserManager>) {
//
//            }
//
//        })
    }
}