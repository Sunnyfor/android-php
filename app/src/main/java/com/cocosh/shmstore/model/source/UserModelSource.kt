package com.cocosh.shmstore.model.source

import com.cocosh.shmstore.login.ILoginContract
import com.cocosh.shmstore.model.UserModel

/**
 * 用户数据接口
 * Created by zhangye on 2017/10/20.
 */
interface UserModelSource {

    //保存用户信息
    fun saveUser(user: UserModel)

    //登录方法
    fun login(phone: String, password: String, loginView: ILoginContract.IView)
}