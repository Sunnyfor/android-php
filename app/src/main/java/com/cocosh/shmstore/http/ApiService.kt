package com.cocosh.shmstore.http

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 *  动态生成Observable
 * Created by 张野 on 2017/9/28.
 */
interface ApiService {

    /*
        POST请求
     */
    @POST
    @FormUrlEncoded
    fun post(@FieldMap map: Map<String, String>, @Url url: String): Observable<ResponseBody>


    /*
        POST请求传递Body
     */
    @POST
    fun post(@Body requestBody: RequestBody, @Url url: String): Observable<ResponseBody>


    /*
     * POST上传文件
     */
    fun post(@Body body: MultipartBody, @Url url: String): Observable<ResponseBody>

    /*
       GET请求无参
     */
    @GET
    fun get(@Url url: String): Observable<ResponseBody>

}