package com.cocosh.shmstore.login

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.login.model.LoginHistory
import com.umeng.socialize.bean.SHARE_MEDIA

/**
 * 登录的presente和view接口
 * Created by zhangye on 2017/10/20.
 */
interface ILoginContract {

    interface IView : IBaseView {
        //返回登录信息
        fun loginResult(result: BaseBean<Login2>, isOtherLogin:Boolean)

        //返回历史帐号记录数据
        fun onHistory(history: List<LoginHistory>)

        //隐藏下拉菜单
        fun gonePhone(history: LoginHistory)
    }


    interface IPresenter : IBasePresenter {
        //用户名密码登录
        fun login(phone: String, password: String)

        //第三方登录
        fun authVerify(type: SHARE_MEDIA)

        //添加登录历史记录
        fun addHistory(history: LoginHistory)

        //删除登录历史记录
        fun removeHistory(history: LoginHistory)

    }
}