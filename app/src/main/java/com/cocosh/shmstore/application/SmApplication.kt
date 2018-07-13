package com.cocosh.shmstore.application

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Typeface
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.http.Constant
import com.squareup.leakcanary.LeakCanary
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import xiaofei.library.datastorage.DataStorageFactory
import xiaofei.library.datastorage.IDataStorage
import java.util.*
import kotlin.collections.ArrayList

/**
 * 首媒APP
 * Created by 张野 on 2017/10/12.
 */
class SmApplication : MultiDexApplication() {
    private val storeMap = HashMap<String, Any>() //内存数据存储
    lateinit var dataStorage: IDataStorage //持久化数据存储
    lateinit var iconFontType: Typeface  //字体图库
    var activityName: Class<BaseActivity>? = null
    var isDelete: Boolean = false
    //分割dex文件
    override fun attachBaseContext(base: Context?) {
        MultiDex.install(this)
        super.attachBaseContext(base)
    }

    /**
     * 单例
     */
    companion object {
        private lateinit var instance: SmApplication
        fun getApp(): SmApplication = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        iconFontType = Typeface.createFromAsset(assets, "font/iconfont.ttf")

        dataStorage = DataStorageFactory.getInstance(
                applicationContext,
                DataStorageFactory.TYPE_DATABASE)

        if (Constant.isDebug()) {
            LeakCanary.install(this)
        }

        initUmeng()
    }

    /**
     * 初始化友盟SDK
     */
    private fun initUmeng() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        UMConfigure.setLogEnabled(true)
        UMShareAPI.get(this)
        PlatformConfig.setWeixin("wx8ae0d35163a7aac4", "b56616b7f9e8d99f21e7c09186f29dbd")
        PlatformConfig.setSinaWeibo("2761266399", "dfca02d093e11a2d9e1bf315053a393d", "http://1.202.14.86:8088/ucenter/wb/callback")
        PlatformConfig.setQQZone("1105781269", "7RCRVAzbRq8mOnEA")

    }

    /**
     * @return 应用版本号
     */
    fun getVersionName(): String {
        try {
//                 获取packageManager的实例
            val packageManager = this.packageManager
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            val packInfo = packageManager.getPackageInfo(this.packageName, 0)
            return packInfo.versionName
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String, isDelete: Boolean): T? {

        if (!storeMap.containsKey(key)) {
            return null
        }

        val result = storeMap[key]

        if (isDelete) {
            removeData(key)
        }
        return result as T
    }


    /**
     * 存储数据
     */
    fun setData(key: String, t: Any?) {
        if (t != null) {
            storeMap[key] = t
        }
    }

    /**
     * 删除数据
     */
    fun removeData(key: String) {
        storeMap.remove(key)
    }


    fun addActivity(key: String, baseActivity: BaseActivity) {
        var list = getData<ArrayList<Activity>>(key, false)
        if (list != null) {
            list.add(baseActivity)
        } else {
            list = arrayListOf()
            list.add(baseActivity)
            setData(key, list)
        }
    }

    fun deleteActivity(key: String,baseActivity: BaseActivity){
        getData<ArrayList<Activity>>(key, false)?.remove(baseActivity)
    }

    fun clearActivity(key: String){
        getData<ArrayList<Activity>>(key, true)?.let {
            it.forEach {
                it.finish()
            }
            it.clear()
        }
    }



    fun setActivityCallBack(clazz: Class<BaseActivity>) {
        activityName = clazz
    }

    fun getActivityCallBack(): Class<BaseActivity>? = activityName
}
