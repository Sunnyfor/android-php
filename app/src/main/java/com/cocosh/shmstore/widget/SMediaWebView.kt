package com.cocosh.shmstore.widget

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.webkit.WebView


/**
 * Created by lmg on 2018/5/31.
 */
class SMediaWebView : WebView {
    private var lastContentHeight = 0
    private val MSG_CONTENT_CHANGE = 1
    private var onContentChangeListener: OnContentChangeListener? = null
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_CONTENT_CHANGE -> if (onContentChangeListener != null) {
                    onContentChangeListener!!.onContentChange()
                }
            }
        }
    }


    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (contentHeight != lastContentHeight) {
            handler.sendEmptyMessage(MSG_CONTENT_CHANGE)
            lastContentHeight = contentHeight
        }

    }

    fun setOnContentChangeListener(onContentChangeListener: SMediaWebView.OnContentChangeListener) {
        this.onContentChangeListener = onContentChangeListener
    }

    /**
     * 监听内容高度发生变化
     */
    public interface OnContentChangeListener {
        fun onContentChange()
    }
}