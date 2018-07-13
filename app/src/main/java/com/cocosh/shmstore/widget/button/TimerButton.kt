package com.cocosh.shmstore.widget.button

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import com.cocosh.shmstore.R
import java.util.*

/**
 * 带计时器的button
 * Created by zhangye on 2018/1/25.
 */
class TimerButton : Button, View.OnClickListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val defualtSecond = 60
    private var second = 0
    private var isClick = false
    private var isShow = false
    private val timer = Timer()
    private var listener: View.OnClickListener? = null

    private val mHandler = Handler(Handler.Callback {
        if (second <= 0) {
            if (isShow) {
                setBackgroundResource(R.drawable.rounded_rectangle_red)
            } else {
                setBackgroundResource(R.drawable.rounded_rectangle_gray)
            }

            isClick = false

            text = resources.getString(R.string.getCode)
        } else {

            text = ("${resources.getString(R.string.getCode)}${second}s")
            setBackgroundResource(R.drawable.rounded_rectangle_gray)
        }

        second--

        return@Callback false
    })


    init {

        setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (isClick || !isShow) {
            return
        }
        listener?.onClick(v)

    }

    //根据环境设置按钮是否可以点击
    fun setClick(isShow: Boolean) {
        this.isShow = isShow

        if (isShow && !isClick) {
            setBackgroundResource(R.drawable.rounded_rectangle_red)
        } else {
            setBackgroundResource(R.drawable.rounded_rectangle_gray)
        }
    }

    fun setCallListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun action() {
        isClick = true

        second = defualtSecond

        val timerTask = object : TimerTask() {
            override fun run() {

                mHandler.sendEmptyMessage(second)

                if (second <= 0) {
                    cancel()
                }
            }
        }
        timer.schedule(timerTask, 0, 1000)
    }
}