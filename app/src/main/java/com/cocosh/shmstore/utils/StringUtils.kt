package com.cocosh.shmstore.utils

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.Collections.replaceAll
import java.util.regex.Pattern


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


    //时间戳格式化时间
    fun dateFormat(date:String):String{
        if (date.isEmpty()){
            return ""
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date(date.toLong() * 1000))
    }


    fun getTimeStamp():String =  (System.currentTimeMillis()/1000).toString()


    //根据长度生成随机字符串
    private fun getRandomChar(length: Int): String {            //生成随机字符串
        val chr = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
        val random = Random()
        val buffer = StringBuffer()
        for (i in 0 until length) {
            buffer.append(chr[random.nextInt(36)])
        }
        return buffer.toString()
    }

    fun getNoncestr(): String {
        val min = 2
        val max = 20
        return getRandomChar(Random().nextInt(max - min) + min)
    }


    //删除HTML标签代码
    fun delHTMLTag(htmlStr: String): String {
        var result = htmlStr
        val regExScript = "<script[^>]*?>[\\s\\S]*?</script>" //定义script的正则表达式
        val regExStyle = "<style[^>]*?>[\\s\\S]*?</style>" //定义style的正则表达式
        val regExHtml = "<[^>]+>" //定义HTML标签的正则表达式

        val pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE)
        val mScript = pScript.matcher(result)
        result = mScript.replaceAll("") //过滤script标签

        val pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE)
        val mStyle = pStyle.matcher(result)
        result = mStyle.replaceAll("") //过滤style标签

        val pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE)
        val mHtml = pHtml.matcher(result)
        result = mHtml.replaceAll("") //过滤html标签
        return result.trim { it <= ' ' } //返回文本字符串
    }
}