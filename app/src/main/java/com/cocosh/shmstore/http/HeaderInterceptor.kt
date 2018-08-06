package com.cocosh.shmstore.http

import com.cocosh.shmstore.utils.DigestUtils
import com.cocosh.shmstore.utils.StringUtils
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.utils.UserManager2
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * 头信息
 * Created by zhangye on 2018/2/7.
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authorised = originalRequest.newBuilder()
                .header("authorization", getAuthorization())
                .build()
        return chain.proceed(authorised)
    }

    private fun getAuthorization(): String {

        if (UserManager2.isLogin()) {
            val paramsArray = TreeMap<String, String>()
            //测试参数
//        paramsArray["auth_session"] = "95270"
//        paramsArray["timestamp"] = "1530491029"
//        paramsArray["noncestr"] = "JxRaZevm3"
//        paramsArray["sid"] = "28"
//        paramsArray["token"] = "wlliromhhx29dq8fyjrece4tihcwj5:jr0vvxZS3kDMRMeWSGe9olHqJ1s="
            paramsArray["app_id"] = "android_id"
            paramsArray["auth_session"] = UserManager2.getLogin()?.code ?: ""
            paramsArray["timestamp"] = (System.currentTimeMillis() / 1000).toString()
            paramsArray["noncestr"] = StringUtils.getNoncestr()
            paramsArray["sid"] = UserManager2.getLogin()?.sid ?: ""
            paramsArray["token"] = UserManager2.getLogin()?.access_token ?: ""


            val signStr = StringBuilder()
            paramsArray.forEach {
                signStr.append(it.key)
                signStr.append(it.value)
            }

            val authorizationStr = StringBuilder("sm-auth-${Constant.VERSION}").append("/")
            authorizationStr.append(paramsArray["auth_session"]).append("/")
            authorizationStr.append(paramsArray["app_id"]).append("/")
            authorizationStr.append(paramsArray["timestamp"]).append("/")
            authorizationStr.append(paramsArray["noncestr"]).append("/")
            authorizationStr.append(paramsArray["sid"]).append("/")
            authorizationStr.append(DigestUtils.md5(signStr.toString() + Constant.APPSECRET))
            return authorizationStr.toString()
        }

        return ""
    }
}