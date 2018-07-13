package com.cocosh.shmstore.http

import com.cocosh.shmstore.utils.UserManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 头信息
 * Created by zhangye on 2018/2/7.
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authorised = originalRequest.newBuilder()
                .header("X-AUTH-TOKEN", "c230f588a4b04b44a577e47220175e9e")
                .header("X-USER-TOKEN", UserManager.getUserToken())
                .build()
        return chain.proceed(authorised)
    }

}