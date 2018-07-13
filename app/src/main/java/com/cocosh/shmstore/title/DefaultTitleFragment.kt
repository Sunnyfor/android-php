package com.cocosh.shmstore.title

import android.support.v4.content.ContextCompat
import android.view.View
import android.view.View.OnClickListener
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.utils.LogUtil
import kotlinx.android.synthetic.main.layout_default_title.view.*

/**
 * 默认标题
 * Created by zhangye on 2018/1/24.
 */
class DefaultTitleFragment : BaseFragment() {
    override fun reTryGetData() {

    }

    var singleText = false
    var title = ""
    var iconLeft = ""
    var color = 0
    var leftMargin = 0

    var listener: OnClickListener? = null

    override fun setLayout(): Int = R.layout.layout_default_title

    override fun initView() {
        getLayoutView().tvTitle.text = title

        if(singleText){
            getLayoutView().tvLeft.visibility = View.GONE
            return
        }

        //调整图标样式
        if (iconLeft != "") {
            getLayoutView().tvLeft.text = iconLeft
        }

        //调整颜色
        if (color != 0) {
            getLayoutView().tvLeft.setTextColor(ContextCompat.getColor(context, color))
        }

        //调整边距
        if (leftMargin != 0) {
            val params = RelativeLayout.LayoutParams(getLayoutView().tvLeft.layoutParams.width, getLayoutView().tvLeft.layoutParams.height)
            params.leftMargin = leftMargin
            getLayoutView().tvLeft.layoutParams = params
        }

        //点击事件
        if (listener != null) {
            getLayoutView().tvLeft.setOnClickListener(listener)
        } else {
            getLayoutView().tvLeft.setOnClickListener(this)
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

    fun singleText(){
        singleText = true
    }

    /**
     * 设置左边图标与颜色
     */
    fun leftIcon(iconStr: String?) {
        if(iconStr!= null){
            iconLeft = iconStr
        }
    }

    fun leftColor(color: Int) {
        if (color != 0) {
            this.color = color
        }
    }

    fun leftMargin(margin: Int) {
        if(margin != 0){
            leftMargin = margin
        }
    }

    fun setLeftOnClickListener(onClickListener: OnClickListener?) {
        listener = onClickListener
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            initView()
        }
    }
}