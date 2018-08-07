package com.cocosh.shmstore.login

import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.login.model.LoginHistory
import com.cocosh.shmstore.login.data.LoginLoader
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.mine.model.MemberEntrance2
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.UserManager2
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA

/**
 * 登录业务处理
 * Created by zhangye on 2017/10/20.
 */
class LoginPresenter(private var activity: BaseActivity, private var loginView: ILoginContract.IView) : ILoginContract.IPresenter, UMAuthListener {

    private var loginLoader = LoginLoader(activity, loginView)

    override fun start() {
        loginView.onHistory(loginLoader.getHistorys())
    }

    override fun login(phone: String, password: String) {
        loginLoader.login(phone, password, object : ApiManager2.OnResult<BaseBean<Login2>>() {
            override fun onSuccess(data: BaseBean<Login2>) {
                data.message?.let {
                    it.phone = phone
                    UserManager2.setLogin(it)
                    val history = LoginHistory(phone)
                    addHistory(history)
                }
                updateProfile()
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<Login2>) {

            }

        })
    }

    override fun addHistory(history: LoginHistory) {
        loginLoader.addHistory(history)
    }

    override fun removeHistory(history: LoginHistory) {
        loginLoader.removeHistory(history)
    }


    /**
     * 开始调用第三方登录
     */
    override fun onStart(p0: SHARE_MEDIA?) {
    }

    /**
     * 第三方登录完成回调结果
     */
    override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>?) {


        data?.forEach {
            LogUtil.i("key:${it.key}  value:${it.value}")
        }

        if (data != null) {
            SmApplication.getApp().setData("other", data)

            val accessToken = data["accessToken"]
            val uid = data["uid"]
            if (accessToken != null && uid != null) {
                loginLoader.otherLogin(accessToken, uid, platform.name)
            }
        }

    }

    /**
     * 取消操作
     */
    override fun onCancel(platform: SHARE_MEDIA?, action: Int) {
    }

    /**
     * 发生错误
     */
    override fun onError(platform: SHARE_MEDIA?, action: Int, t: Throwable?) {
    }


    /**
     * 开始调用第三方登录
     */
    override fun authVerify(type: SHARE_MEDIA) {
        UMShareAPI.get(SmApplication.getApp()).getPlatformInfo(activity, type, this)
    }

    private fun updateProfile() {
        UserManager2.loadMemberEntrance(activity, object : ApiManager2.OnResult<BaseBean<MemberEntrance2>>() {
            override fun onSuccess(data: BaseBean<MemberEntrance2>) {
                UserManager2.setMemberEntrance(data.message)
                loginView.loginResult(false)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<MemberEntrance2>) {
            }

        })
    }
}