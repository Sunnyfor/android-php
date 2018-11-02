package com.cocosh.shmstore.title

import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import kotlinx.android.synthetic.main.layout_left_right_title.view.*

/**
 * 默认标题
 */
class LeftRightTitleFragment : BaseFragment() {
    override fun reTryGetData() {

    }

    private var title = ""
    private var iconLeft = ""
    private var iconRight = ""
    private var color = 0
    private var rightColor = 0
    private var leftMargin = 0
    private var rightTextSize = 0f
    private var isGoneLeft = false

    private var leftListener: OnClickListener? = null
    private var rightListener: OnClickListener? = null

    override fun setLayout(): Int = R.layout.layout_left_right_title

    override fun initView() {
        getLayoutView().tvTitle.text = title

        //调整图标样式
        if (iconLeft != "") {
            getLayoutView().tvLeft.text = iconLeft
        }

        if (iconRight != "") {
            getLayoutView().tvRight.text = iconRight
        }

        if(rightTextSize != 0f){
            getLayoutView().tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,rightTextSize)
        }


        if(isGoneLeft){
            getLayoutView().tvLeft.visibility = View.GONE
        }

        //调整颜色
        if (color != 0) {
            getLayoutView().tvLeft.setTextColor(ContextCompat.getColor(context, color))
            getLayoutView().tvTitle.setTextColor(ContextCompat.getColor(context, color))
            getLayoutView().tvRight.setTextColor(ContextCompat.getColor(context, color))
        }

        if(rightColor != 0){
            getLayoutView().tvRight.setTextColor(ContextCompat.getColor(context, rightColor))
        }

        //调整边距
        if (leftMargin != 0) {
            val params = RelativeLayout.LayoutParams(getLayoutView().tvLeft.layoutParams.width, getLayoutView().tvLeft.layoutParams.height)
            params.leftMargin = leftMargin
            getLayoutView().tvLeft.layoutParams = params
        }

        //点击事件
        if (leftListener != null) {
            getLayoutView().tvLeft.setOnClickListener(leftListener)
        } else {
            getLayoutView().tvLeft.setOnClickListener(this)
        }

        if (rightListener != null) {
            getLayoutView().tvRight.setOnClickListener(rightListener)
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            getLayoutView().tvLeft.id -> activity.finish()
        }
    }

    override fun close() {

    }

    /**
     * 设置title
     */
    fun title(title: String) {
        this.title = title
    }

    fun goneLeft(){
        isGoneLeft = true
    }


    /**
     * 设置左边图标与颜色
     */
    fun leftIcon(iconStr: String?) {
        if (iconStr != null) {
            iconLeft = iconStr
        }
    }

    fun setColor(color: Int) {
        if (color != 0) {
            this.color = color
        }
    }

    fun setRightColor(color: Int) {
        if (color != 0) {
            this.rightColor = color
        }
    }


    fun leftMargin(margin: Int) {
        if (margin != 0) {
            leftMargin = margin
        }
    }

    fun rightIcon(iconStr: String?) {
        if (iconStr != null) {
            iconRight = iconStr
        }
    }

    fun rightTextSize(px:Float){
        rightTextSize = px
    }


    fun setLeftOnClickListener(onClickListener: OnClickListener?) {
        leftListener = onClickListener
    }


    fun setRightOnClickListener(onClickListener: OnClickListener?) {
        rightListener = onClickListener
    }

    fun getLeftText() = getLayoutView().tvLeft

    fun getRightText() = getLayoutView().tvRight
}