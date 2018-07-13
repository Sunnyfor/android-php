package com.cocosh.shmstore.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import kotlinx.android.synthetic.main.layout_item_show.view.*

/**
 * 展示key value数据条目
 * Created by zhangye on 2018/3/29.
 */
class ItemShowView : RelativeLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }


    fun initView(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_item_show, this, true)
        attrs?.let {
            val typedarray = context.obtainStyledAttributes(it, R.styleable.ItemShowView)
            val name = typedarray.getString(R.styleable.ItemShowView_nameText)
            val value = typedarray.getString(R.styleable.ItemShowView_valueText)
            typedarray.recycle()
            if (name != null) {
                tvName.text = name
            }
            if (value != null) {
                tvIcon.text = value
            }
        }
    }

    fun setIcon(iconStr: String) {
        tvIcon.text = iconStr
        tvIcon.visibility = View.VISIBLE
    }


    fun setNoValueIcon(iconStr: String) {
        setIcon(iconStr)
        tvValue.visibility = View.GONE
    }


    fun setValue(value: String?) {
        tvValue.text = value
        tvValue.visibility = View.VISIBLE
    }

    fun setNoIconValue(value: String?) {
        setValue(value)
        tvIcon.visibility = View.GONE
        (llayoutValue.layoutParams as RelativeLayout.LayoutParams).rightMargin = context.resources.getDimension(R.dimen.w50).toInt()
    }

    fun setName(name: String?) {
        tvName.text = name
    }

    fun getValue(): String = tvValue.text.toString()

}