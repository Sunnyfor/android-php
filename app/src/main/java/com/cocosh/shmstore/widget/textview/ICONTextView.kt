package com.cocosh.shmstore.widget.textview

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.cocosh.shmstore.application.SmApplication

/**
 * 图标字体的TextView
 * Created by zhangye on 2018/1/16.
 */
open class ICONTextView : TextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        typeface = SmApplication.getApp().iconFontType
    }

    /**
     * 字体加粗
     */
    fun boldText() {
        paint.isFakeBoldText = true
    }
}