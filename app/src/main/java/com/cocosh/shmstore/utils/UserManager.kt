package com.cocosh.shmstore.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.HomeActivity
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.model.Login
import com.cocosh.shmstore.mine.model.MemberEntrance
import com.google.gson.Gson
import jp.wasabeef.glide.transformations.BlurTransformation


/**
 * 用户实体类
 * Created by asus on 2017/1/4.
 */

object UserManager {
    //存储用户信息
    private const val LOGIN = "login"
    //我的页面入口数据
    private const val MEMBERENTRANCE = "memberentrance"

    private var bitmap_topbg: Bitmap? = null
    private var default_bg: Bitmap? = null

    private var tag: String? = null
    /**
     * 获取用户ID
     */
    fun getUserId(): String = getLogin()?.userId ?: ""


    /**
     * 获取用户Token
     */
    fun getUserToken(): String = getLogin()?.token ?: ""


    /**
     * 存储登录信息
     */
    fun setLogin(login: Login) {
        SharedUtil.setString(LOGIN, Gson().toJson(login))
    }

    /**
     * 获取存储登录信息
     */
    fun getLogin(): Login? {
        val json = SharedUtil.getString(LOGIN)
        if (json != "") {
            return Gson().fromJson(json, Login::class.java)
        }
        return null
    }

    fun getPhone(): String? = getMemberEntrance()?.userName


    fun getCryptogramPhone(): String? {
        getPhone()?.let {
            val sb = StringBuilder()
            sb.append(it.substring(0, 3))
            sb.append("****")
            sb.append(it.substring(7, 11))
            return sb.toString()
        }
        return null
    }

    /**
     * 是否设置支付密码
     */
    fun getPayPwdStatus(): Boolean? = getMemberEntrance()?.checkPayPassword

    /**
     * 设置支付密码状态
     */
    fun setPayPwdStatus(boolean: Boolean) {
        val data = getMemberEntrance()
        data?.checkPayPassword = boolean
        setMemberEntrance(data)
    }

    /**
     *获取档案完整度
     */
    fun getArchivalCompletion(): String? {
        if (getMemberEntrance() == null) {
            return "0"
        }
        return getMemberEntrance()?.degreeOfPerfection
    }

    /**
     *更新档案完整度
     */
    fun setArchivalCompletion(value: String) {
        val data = getMemberEntrance()
        data?.degreeOfPerfection = value
        setMemberEntrance(data)
    }

    /**
     *  存储我的页面数据
     */
    fun setMemberEntrance(memberEntrance: MemberEntrance?) {
        if (memberEntrance != null) {
            SharedUtil.setString(MEMBERENTRANCE, Gson().toJson(memberEntrance))
        }
    }

    /**
     *  存储我的页面数据
     */
    fun setPersonStatus(value: String?) {
        val data = getMemberEntrance()
        data?.personStatus = value
        setMemberEntrance(data)
    }

    /**
     * 获取我的页面数据
     */
    fun getMemberEntrance(): MemberEntrance? {
        val json = SharedUtil.getString(MEMBERENTRANCE)
        if (json != "") {
            return Gson().fromJson(json, MemberEntrance::class.java)
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
        if (getUserId() != "") {
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
        }else{
            Glide.with(imageView.context).load(url).bitmapTransform(BlurTransformation(imageView.context,10)).into(imageView)
        }
    }


    /**
     * 加载我的页面数据
     */
    fun loadMemberEntrance(baseActivity: BaseActivity) {
        if (!isLogin()) {
            return
        }
        ApiManager.get(0, baseActivity, hashMapOf(), Constant.PROFILE, object : ApiManager.OnResult<BaseModel<MemberEntrance>>() {
            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<MemberEntrance>) {

            }

            override fun onSuccess(data: BaseModel<MemberEntrance>) {
                if (data.success) {
                    data.entity?.let {
                        UserManager.setMemberEntrance(it)  //存储信息
                        if (baseActivity is HomeActivity) {
                            if (baseActivity.mineFragment?.isAdded == true){
                                baseActivity.mineFragment?.loadDate()
                            }
                        }
                    }
                }
            }
        })
    }
}
