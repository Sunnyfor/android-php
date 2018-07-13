package com.cocosh.shmstore.utils

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * <读取Json文件的工具类>
 * Created by zhangye on 2018/4/11.
 */
class GetJsonDataUtil {
    fun getJson(context: Context, fileName: String): String {

        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(
                    assetManager.open(fileName)))

            bf.readLines().forEach {
                stringBuilder.append(it)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }
}