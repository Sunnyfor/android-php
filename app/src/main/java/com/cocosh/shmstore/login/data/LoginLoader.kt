package com.cocosh.shmstore.login.data

import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.ILoginContract
import com.cocosh.shmstore.login.model.Login
import com.cocosh.shmstore.login.model.LoginHistory
import xiaofei.library.comparatorgenerator.ComparatorGenerator
import java.util.*

/**
 * 登录功能数据加载
 * Created by zhangye on 2017/10/20.
 */
class LoginLoader(var activity: BaseActivity, var loginView: ILoginContract.IView) {

    /**
     * 登录
     */
    fun login(phone: String, password: String) {
        val map = HashMap<String, String>()
        map["userName"] = phone.trim()
        map["userPwd"] = password.trim()
        map["deviceModel"] = "android"
        map["longitude"] = "0"
        map["latitude"] = "0"
        map["systemVersion"] = "0"
        map["deviceSerialNumber"] = "0"

        ApiManager.post(activity, map, Constant.LOGIN, object : ApiManager.OnResult<BaseModel<Login>>() {

            override fun onSuccess(data: BaseModel<Login>) {
                loginView.loginResult(data, false)

            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<Login>) {

            }

        })
    }

    /**
     * 第三方登录
     */
    fun otherLogin(accessToken: String, openId: String, type: String) {

        val map = HashMap<String, String>()
        map["type"] = type
        map["openId"] = openId
        map["accessToken"] = accessToken
        map["deviceType"] = "android"

        ApiManager.post(activity, map, Constant.WEB_VALIDATE, object : ApiManager.OnResult<BaseModel<Login>>() {

            override fun onSuccess(data: BaseModel<Login>) {
                data.entity?.type = type
                data.entity?.openId = openId
                loginView.loginResult(data, true)
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<Login>) {

            }

        })

    }


    /**
     * 获取历史数据
     */
    fun getHistorys(): List<LoginHistory> {
        val comparator = ComparatorGenerator<LoginHistory>(LoginHistory::class.java).generate()
        return SmApplication.getApp().dataStorage.loadAll(LoginHistory::class.java, comparator)
    }


    /**
     * 添加历史记录
     */
    fun addHistory(history: LoginHistory) {
        SmApplication.getApp().dataStorage.storeOrUpdate(history)
    }

    /**
     * 删除历史记录
     */
    fun removeHistory(history: LoginHistory) {
        SmApplication.getApp().dataStorage.delete(history)
    }
}


