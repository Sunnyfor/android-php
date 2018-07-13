package com.cocosh.shmstore.utils

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern


/**
 * Created by lmg on 2018/5/8.
 */
class CashierInputFilter : InputFilter {
    /**
     * 小数点后的数字的位数
     */
    val PONTINT_LENGTH = 2
    var p: Pattern

    constructor() : super() {
        p = Pattern.compile("([0-9]|\\.)*") //除数字外的其他的
    }

    /**
     * source    新输入的字符串
     * start    新输入的字符串起始下标，一般为0
     * end    新输入的字符串终点下标，一般为source长度-1
     * dest    输入之前文本框内容
     * dstart    原内容起始坐标，一般为0
     * dend    原内容终点坐标，一般为dest长度-1
     */
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {

        val oldtext = dest.toString()

        //验证删除等按键
        if ("" == source.toString()) {
            return ""
        }

        //验证非数字或者小数点的情况
        val m = p.matcher(source)
        if (oldtext.contains(".")) {
            //已经存在小数点的情况下，只能输入数字
            if (!m.matches()) {
                return ""
            }
        } else {
            if (!m.matches()) {
                return ""
            } else {
                if ("0" == source && "0" == oldtext) {
                    return ""
                }
            }
            if ("." == source && TextUtils.isEmpty(oldtext)) {
                return ""
            }
        }

        //验证小数位精度是否正确
        if (oldtext.contains(".")) {
            val index = oldtext.indexOf(".")
            val len = dend - index
            //小数位只能2位
            if (len > PONTINT_LENGTH) {
                return dest.subSequence(dstart, dend)
            }
        }

        return dest.subSequence(dstart, dend).toString() + source.toString()
    }
}