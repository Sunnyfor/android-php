package com.cocosh.shmstore.widget

import android.content.Context
import android.os.Handler
import android.support.v7.widget.AppCompatTextView
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.cocosh.shmstore.R
import android.graphics.drawable.Drawable
import com.cocosh.shmstore.application.SmApplication


/**
 * Created by lmg on 2018/7/9.
 * 扩展textView
 */
class SMSpannableTextView : AppCompatTextView {
    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        limitTextViewString(this.text.toString(), 70, this, OnClickListener {
            //设置监听函数
        })
    }

    init {
        typeface = SmApplication.getApp().iconFontType
    }

    /**
     * get the last char index for max limit row,if not exceed the limit,return -1
     * @param textView
     * @param content
     * @param width
     * @param maxLine
     * @return
     */
    private fun getLastCharIndexForLimitTextView(textView: TextView, content: String, width: Int, maxLine: Int): Int {
        val textPaint = textView.paint
        val staticLayout = StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        return if (staticLayout.lineCount > maxLine)
            staticLayout.getLineStart(maxLine) - 1//exceed
        else
            -1//not exceed the max line
    }

    /**
     * 限制TextView显示字符字符，并且添加showMore和show more的点击事件
     * @param textString
     * @param textView
     * @param clickListener textView的点击监听器
     */
    fun limitTextViewString(textString: String, maxFirstShowCharCount: Int, textView: TextView?, clickListener: View.OnClickListener?) {
        //计算处理花费时间
        if (textView == null) return
        var width = textView.width//在recyclerView和ListView中，由于复用的原因，这个TextView可能以前就画好了，能获得宽度
        if (width == 0) width = 1000//获取textView的实际宽度，这里可以用各种方式（一般是dp转px写死）填入TextView的宽度
        var lastCharIndex = getLastCharIndexForLimitTextView(textView, textString, width, 2)
        //返回-1表示没有达到行数限制
        if (lastCharIndex < 0 && textString.length <= maxFirstShowCharCount) {
            //如果行数没超过限制
            textView.text = textString
            return
        }
        //如果超出了行数限制
        textView.movementMethod = LinkMovementMethod.getInstance()//this will deprive the recyclerView's focus
        if (lastCharIndex > maxFirstShowCharCount || lastCharIndex < 0) {
            lastCharIndex = maxFirstShowCharCount
        }
        //构造spannableString
        var explicitText: String? = null
        val explicitTextAll: String
        if (textString[lastCharIndex] == '\n') {//manual enter
            explicitText = textString.substring(0, lastCharIndex)
        } else if (lastCharIndex > 12) {
            //如果最大行数限制的那一行到达12以后则直接显示 显示更多
            explicitText = textString.substring(0, lastCharIndex - 12)
        }
        val sourceLength = explicitText!!.length
        val showMore = "  更多"+resources.getString(R.string.iconMoreDown)
        explicitText = "$explicitText$showMore"
        val mSpan = SpannableString(explicitText)


        val dismissMore = "" //收起
        explicitTextAll = "$textString"//...$dismissMore
        val mSpanALL = SpannableString(explicitTextAll)
        mSpanALL.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textView.resources.getColor(R.color.red)
                ds.isAntiAlias = true
                ds.isUnderlineText = false
            }

            override fun onClick(widget: View) {
//                textView.text = mSpan
//                textView.setOnClickListener(null)
//                Handler().postDelayed(Runnable {
//                    if (clickListener != null)
//                        textView.setOnClickListener(clickListener)//prevent the double click
//                }, 20)
            }
        }, textString.length, explicitTextAll.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        mSpan.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textView.resources.getColor(R.color.hailan)
                ds.isAntiAlias = true
                ds.isUnderlineText = false
            }

            override fun onClick(widget: View) {//"...show more" click event
                textView.text = mSpanALL
                textView.setOnClickListener(null)
                Handler().postDelayed(Runnable {
                    if (clickListener != null) {
                        textView.setOnClickListener(clickListener)//prevent the double click
                    }
                }, 20)
            }
        }, sourceLength, explicitText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        val drawable = resources.getDrawable(R.drawable.down)
//        drawable.setBounds(0, -resources.getDimension(R.dimen.h40).toInt(), resources.getDimension(R.dimen.h40).toInt(),0)//
//        val imageSpan = ImageSpan(drawable)
//        mSpan.setSpan(imageSpan, explicitText.length - 1, explicitText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        //设置为“显示更多”状态下的TextVie
        textView.text = mSpan
    }
}