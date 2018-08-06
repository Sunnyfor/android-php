package com.cocosh.shmstore.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.mine.model.MemberEntrance2
import com.google.gson.Gson
import jp.wasabeef.glide.transformations.BlurTransformation


/**
 * 用户实体类
 * Created by asus on 2017/1/4.
 */

object UserManager2 {
    //存储用户信息
    private const val LOGIN = "login"
    //我的页面入口数据
    private const val MEMBERENTRANCE = "memberentrance"

    private var bitmap_topbg: Bitmap? = null
    private var default_bg: Bitmap? = null

    private var tag: String? = null

    /**
     * 存储登录信息
     */
    fun setLogin(login: Login2) {
        SharedUtil.setString(LOGIN, Gson().toJson(login))
    }

    /**
     * 获取存储登录信息
     */
    fun getLogin(): Login2? {
        val json = SharedUtil.getString(LOGIN)
        if (json != "") {
            return Gson().fromJson(json, Login2::class.java)
        }
        return null
    }

//    fun getPhone(): String? = getMemberEntrance()?.userName


//    fun getCryptogramPhone(): String? {
//        getPhone()?.let {
//            val sb = StringBuilder()
//            sb.append(it.substring(0, 3))
//            sb.append("****")
//            sb.append(it.substring(7, 11))
//            return sb.toString()
//        }
//        return null
//    }

    /**
     * 是否设置支付密码
     */
//    fun getPayPwdStatus(): Boolean? = getMemberEntrance()?.checkPayPassword

    /**
     * 设置支付密码状态
     */
//    fun setPayPwdStatus(boolean: Boolean) {
//        val data = getMemberEntrance()
//        data?.checkPayPassword = boolean
//        setMemberEntrance(data)
//    }

//    /**
//     *获取档案完整度
//     */
//    fun getArchivalCompletion(): String? {
//        if (getMemberEntrance() == null) {
//            return "0"
//        }
//        return getMemberEntrance()?.degreeOfPerfection
//    }

//    /**
//     *更新档案完整度
//     */
//    fun setArchivalCompletion(value: String) {
//        val data = getMemberEntrance()
//        data?.degreeOfPerfection = value
//        setMemberEntrance(data)
//    }

    /**
     *  存储我的页面数据
     */
    fun setMemberEntrance(memberEntrance: MemberEntrance2?) {
        if (memberEntrance != null) {
            SharedUtil.setString(MEMBERENTRANCE, Gson().toJson(memberEntrance))
        }
    }

//    /**
//     *  存储我的页面数据
//     */
//    fun setPersonStatus(value: String?) {
//        val data = getMemberEntrance()
//        data?.personStatus = value
//        setMemberEntrance(data)
//    }

    /**
     * 获取我的页面数据
     */
    fun getMemberEntrance(): MemberEntrance2? {
        val json = SharedUtil.getString(MEMBERENTRANCE)
        if (json != "") {
            return Gson().fromJson(json, MemberEntrance2::class.java)
        }
        return null
    }


    /**
     * 将所有本地数据置空
     */
    fun setEmptyUser() {
        SharedUtil.remove(LOGIN)
        SharedUtil.remove(MEMBERENTRANCE)
    }

    /**
     * 判断是否登录
     */
    fun isLogin(): Boolean {
        if (getLogin() != null) {
            return true
        }
        return false
    }

    /**
     * 加载用户背景
     */
    fun loadBg(url: String?, imageView: ImageView) {
        if (url.isNullOrEmpty()) {
            imageView.setImageResource(R.mipmap.bg_top_head)
        } else {
            Glide.with(imageView.context).load(url).bitmapTransform(BlurTransformation(imageView.context, 10)).into(imageView)
        }
    }


    /**
     * 加载我的页面数据
     */
    fun loadMemberEntrance(baseActivity: BaseActivity) {
        if (!isLogin()) {
            return
        }
        ApiManager2.get(0, baseActivity, null, Constant.PROFILE, object : ApiManager2.OnResult<BaseBean<MemberEntrance2>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<MemberEntrance2>) {
                UserManager2.setMemberEntrance(data.message)
            }

            override fun onSuccess(data: BaseBean<MemberEntrance2>) {

            }
        })
    }

    //更新用户资料
    fun updateMemberEntrance(baseActivity: BaseActivity, params: HashMap<String, String>, onResult: ApiManager2.OnResult<BaseBean<String>>) {
        ApiManager2.post(baseActivity, params, Constant.PROFILE_UPDATE, onResult)
    }
}
