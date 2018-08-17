package com.cocosh.shmstore.http

import android.text.TextUtils
import android.util.Log
import com.cocosh.shmstore.utils.LogUtil
import okhttp3.*
import okio.Buffer
import java.io.IOException

/**
 * OKHttp日志拦截
 * Created by 张野 on 2017/10/12.
 */
class LoggerInterceptor constructor(tag: String, private val showResponse: Boolean = false) : Interceptor {
    private val tag: String

    init {
        var mTag = tag
        if (TextUtils.isEmpty(mTag)) {
            mTag = TAG



        }
        this.tag = mTag
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        logForRequest(request)
        val response = chain.proceed(request)
        return logForResponse(response)
    }

    private fun logForResponse(response: Response): Response {
        try {
            //===>response log
            LogUtil.web("========response'log=======")
            val builder = response.newBuilder()
            val clone = builder.build()
            LogUtil.web("url : " + clone.request().url())
            LogUtil.web("code : " + clone.code())
            LogUtil.web("protocol : " + clone.protocol())
            if (!TextUtils.isEmpty(clone.message()))
                LogUtil.web("message : " + clone.message())

            if (showResponse) {
                var body = clone.body()
                if (body != null) {
                    val mediaType = body.contentType()
                    if (mediaType != null) {
                        LogUtil.web("responseBody's contentType : " + mediaType.toString())
                        if (isText(mediaType)) {
                            val resp = body.string()
                            LogUtil.web("responseBody's content : " + resp)

                            body = ResponseBody.create(mediaType, resp)
                            return response.newBuilder().body(body).build()
                        } else {
                            LogUtil.web("responseBody's content : " + " maybe [file part] , too large too print , ignored!")
                        }
                    }
                }
            }

            LogUtil.web("========response'log=======end")
        } catch (e: Exception) {
            //            e.printStackTrace();
        }

        return response
    }

    private fun logForRequest(request: Request) {
        try {
            val url = request.url().toString()
            val headers = request.headers()

            LogUtil.web("========request'log=======")
            LogUtil.web("method : " + request.method())
            LogUtil.web("url : " + url)
            if (headers != null && headers.size() > 0) {
                LogUtil.web("headers : " + headers.toString())
            }
            val requestBody = request.body()
            if (requestBody != null) {
                val mediaType = requestBody.contentType()
                if (mediaType != null) {
                    LogUtil.web("requestBody's contentType : " + mediaType.toString())
                    if (isText(mediaType)) {
                        LogUtil.web("requestBody's content : " + bodyToString(request))
                    } else {
                        LogUtil.web("requestBody's content : " + " maybe [file part] , too large too print , ignored!")
                    }
                }
            }
            LogUtil.web("========request'log=======end")
        } catch (e: Exception) {
            //            e.printStackTrace();
        }

    }

    private fun isText(mediaType: MediaType): Boolean {
        if (mediaType.type() != null && mediaType.type() == "text") {
            return true
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype() == "json" ||
                    mediaType.subtype() == "xml" ||
                    mediaType.subtype() == "html" ||
                    mediaType.subtype() == "webviewhtml"||
                    mediaType.subtype() == "x-www-form-urlencoded")
                return true
        }
        return false
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "something error when show requestBody."
        }

    }

    companion object {
        val TAG = "HttpUtil"
    }
}