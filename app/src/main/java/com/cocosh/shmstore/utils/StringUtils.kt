package com.cocosh.shmstore.utils

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 字符串工具类
 * Created by zhangye on 2018/6/11.
 */
object StringUtils {
    /**
     * 金额格式化
     * @param s 金额
     * @param len 小数位数
     * @return 格式后的金额
     */

    fun insertComma(money: String?, len: Int): String {
        if (money == null || money.isEmpty() || money == "0.0" || money == "0" || money == "0.00") {
            return "0.00"
        }
        val num = money.toDouble()
        val formater = if (len == 0) {
            DecimalFormat("###,###")

        } else {
            val buff = StringBuffer()
            buff.append("###,##0.")
            for (i in 0 until len) {
                buff.append("0")
            }
            DecimalFormat(buff.toString())
        }
        return formater.format(num)
    }


    fun insertComma(money: Double): String = insertComma(money.toString(), 2)
    fun insertComma(money: Float): String = insertComma(money.toString(), 2)


    fun isTimeOut(date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val mDate = dateFormat.parse(date)
        val calendar = Calendar.getInstance()
        val currentDate =  dateFormat.parse( dateFormat.format(calendar.time))
        return if (mDate.time < currentDate.time){
            dateFormat.format(calendar.time)
        }else{
            date
        }
    }
}