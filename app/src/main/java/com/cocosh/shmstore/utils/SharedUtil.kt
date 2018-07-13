package com.cocosh.shmstore.utils

import android.content.Context
import android.content.SharedPreferences
import com.cocosh.shmstore.application.SmApplication


/**
 * Function：SharedPreferences保存数据工具类
 */
object SharedUtil {

    /**
     * 文件名
     */
    private const val FILE_NAME = "SM"

    /**
     * 获取SharedPreferences对象
     *
     * @return SharedPreferences
     */
    private val sharedPreferences: SharedPreferences
        get() = SmApplication.getApp()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)




    /**
     * 保存String信息
     *
     * @param key     键名
     * @param content 默认值
     */
    fun setString(key: String, content: String) {
        sharedPreferences.edit().putString(key, content).apply()
    }

    fun getString(key: String): String = sharedPreferences.getString(key, "")


    /**
     * 保存Boolean类型的信息
     *
     * @param key  键名
     * @param flag 默认值
     */
    fun setBoolean(key: String, flag: Boolean) {
        sharedPreferences.edit().putBoolean(key, flag).apply()
    }

    fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, false)

    fun getBoolean(key: String, flag: Boolean): Boolean = sharedPreferences.getBoolean(key, flag)

    /**
     * 保存Integer信息
     *
     * @param key     键名
     * @param content 默认值
     */
    fun setInteger(key: String, content: Int) {
        sharedPreferences.edit().putInt(key, content).apply()
    }

    fun getInteger(key: String): Int = sharedPreferences.getInt(key, 0)


    /**
     * 删除元素
     */
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

}
