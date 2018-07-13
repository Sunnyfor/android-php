package com.cocosh.shmstore.http

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 *
 * Created by 张野 on 2017/9/28.
 */
object ApiManager {
    private lateinit var apiService: ApiService
    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient
    private val gson = Gson()
    private lateinit var host: String
    val SALE_URL = "http://10.10.100.205/webapp2/" //预生产

    const val STRING = 0X1
    const val ORTHER = 0x2

    init {
        init()
    }

    fun getHost(): String {
//        https://dev.shoumeiapp.com/
        host = if (Constant.isDebug()) "http://10.10.100.206/" else "http://app.shoumeiapp.com"
//        host = "http://10.10.100.51:8085/"
        return host
    }


    fun init() {
        /*
        * 初始化OkHttpClient
        */
        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor())
                .addInterceptor(LoggerInterceptor("网络请求"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build()

        /*
         * 初始化Retrofit
         */
        retrofit = Retrofit.Builder()
                .baseUrl(getHost())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        apiService = retrofit.create(ApiService::class.java)
    }


    /**
     * 发起一个网络请求并解析成实体类
     * flag 区分是否是初始化数据请求 0不是 1是
     */
    private fun <T> request(flag: Int, baseActivity: BaseActivity, observable: Observable<ResponseBody>, onResult: OnResult<T>) {
        if (!NetworkUtils.isNetworkAvaliable(baseActivity)) {
            ToastUtil.show("网络连接错误")
//            baseActivity.showError(0)
            baseActivity.hideLoading()
            if (flag == 1) {
                baseActivity.showReTryLayout()
            }
            return
        }
        baseActivity.showLoading()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {

                    override fun onNext(data: ResponseBody) {
                        baseActivity.hideLoading()
                        if (flag == 1) {
                            baseActivity.hideReTryLayout()
                        }
                        val body = data.string()
                        LogUtil.e("Body:$body")
                        parserJson(flag, baseActivity, body, onResult)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        baseActivity.hideLoading()
                        if (flag == 1) {
                            baseActivity.showReTryLayout()
                        }
                        if (e is SocketTimeoutException || e is TimeoutException) {

                            ToastUtil.show("网络请求超时,请稍后重试！")
                            onResult.onFailed(e)
                            return
                        }

                        if (e is ConnectException) {
                            ToastUtil.show("无法连接到服务器！")
                            onResult.onFailed(e)
                            return
                        }

                        if (e is HttpException) {
                            val body = e.response().errorBody()?.string()
                            LogUtil.e("errorBody :" + body)
                            if (body.isNullOrEmpty()) {
                                onResult.onFailed(e)
                            } else {
                                parserJson(flag, baseActivity, body!!, onResult)
                            }
                        }

                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(disposable: Disposable) {
                        baseActivity.composites.add(disposable)
                    }

                })
    }


    /**
     * GET请求
     */
    fun <T> get(baseActivity: BaseActivity, params: Map<String, String>?, url: String, onResult: OnResult<T>) {
        get(0, baseActivity, params, url, onResult)
    }

    /**
     * GET请求
     */
    fun <T> get(flag: Int, baseActivity: BaseActivity, params: Map<String, String>?, url: String, onResult: OnResult<T>) {
        if (params != null) {
            putParmas(params)
            val sb = StringBuilder("?")
            params.forEach {
                sb.append("${it.key}=${it.value}&")
            }
            sb.deleteCharAt(sb.length - 1)
            request(flag, baseActivity, apiService.get(url + sb.toString()), onResult)
        } else {
            request(flag, baseActivity, apiService.get(url), onResult)
        }
    }


    /**
     *  Post请求
     */
    fun <T> post(baseActivity: BaseActivity, params: Map<String, String>, url: String, onResult: OnResult<T>) {
        post(0, baseActivity, params, url, onResult)
    }


    /**
     *  Post请求
     */
    fun <T> post(flag: Int, baseActivity: BaseActivity, params: Map<String, String>, url: String, onResult: OnResult<T>) {
        putParmas(params)
        request(flag, baseActivity, apiService.post(params, url), onResult)
    }


    /**
     * Post一个JSON
     */
    fun <T> postJson(baseActivity: BaseActivity, params: String, url: String, onResult: OnResult<T>) {
        val requestBody = RequestBody.create(MediaType.parse("application/json"), params)
        request(0, baseActivity, apiService.post(requestBody, url), onResult)
    }


    /**
     * Post一张图片
     */
    fun <T> postImage(baseActivity: BaseActivity, path: String, url: String, onResult: OnResult<T>) {
        val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val file = File(path)
        requestBodyBuilder.addFormDataPart("file", path,
                RequestBody.create(MediaType.parse("image/jpeg"), file))
        requestBodyBuilder.addFormDataPart("picturetype", "jpg")
        request(0, baseActivity, apiService.post(requestBodyBuilder.build(), url), onResult)
    }


    /**
     * 结果回调
     */
    abstract class OnResult<in T> {
        lateinit var typeToken: Type
        var tag: Int

        init {
            val t = javaClass.genericSuperclass
            val args = (t as ParameterizedType).actualTypeArguments
            val type = "class java.lang.String"
            if (args[0].toString() == type) {
                tag = STRING
            } else {
                typeToken = args[0]
                tag = ORTHER
            }
        }

        abstract fun onSuccess(data: T)
        abstract fun onFailed(e: Throwable)
        abstract fun onCatch(data: T)
    }

    fun getHttpClient(): OkHttpClient = okHttpClient


    //添加公用参数
    private fun putParmas(params: Map<String, String>) {
        LogUtil.i("参数数量：${params.size}")
        if (params is HashMap) {
            if (params["userId"] == null) {
                params["userId"] = UserManager.getUserId()
//                params["userId"] = "1"
            }
            if (Constant.isDebug()) {
                params.forEach {
                    LogUtil.i("参数名：${it.key}   值：${it.value}")
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun <T> parserJson(flag: Int, baseActivity: BaseActivity, json: String, onResult: OnResult<T>) {
        if (onResult.tag == STRING) {
            onResult.onSuccess(json as T)
        } else {
            if (onResult.typeToken.toString().contains("com.cocosh.shmstore.base.BaseModel")) {
                try {
                    val baseModel = gson.fromJson<T>(json, onResult.typeToken)
                    if ((baseModel as BaseModel<T>).code == IntentCode.IS_LOGOUT) {
                        SmediaDialog(baseActivity).showLogin()
                        return
                    }
                    //增加返回结果处理 get请求方式
                    if (flag == 1) {
                        if (baseModel.success && baseModel.code == 200) {
                            baseActivity.hideReTryLayout()
                        } else {
                            baseActivity.showReTryLayout()
                        }
                    }
                    onResult.onSuccess(baseModel)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                onResult.onSuccess(gson.fromJson(json, onResult.typeToken))
            }
        }
    }
}