package com.cocosh.shmstore.widget.eidttext

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.EditText
import java.util.regex.Pattern
import android.text.Spanned


/**
 * 设置拦截器
 * Created by zhangye on 2018/2/6.
 */
class SmEditText : EditText {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val filter = InputFilter { source, _, _, _, _, _ ->
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (source == " ") "" else null
        }

        val filter2 = InputFilter { source, _, _, _, _, _ ->
            val speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())
            if (matcher.find()) "" else null;
        }

        //表情过滤器
        val emojiFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val emoji = Pattern.compile(
                    "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
            val emojiMatcher = emoji.matcher(source)
            if (emojiMatcher.find()) {
                ""
            } else null
        }

        //保留布局内的过滤条件
        val mFilters = arrayOfNulls<InputFilter>(filters.size + 3)
        filters.forEachIndexed<InputFilter?> { i: Int, inputFilter: InputFilter? ->
            if (inputFilter != null) {
                mFilters[i] = inputFilter
            }
        }
        mFilters[filters.size] = filter
        mFilters[filters.size + 1] = filter2
        mFilters[filters.size + 2] = emojiFilter
        filters = mFilters
    }
}
