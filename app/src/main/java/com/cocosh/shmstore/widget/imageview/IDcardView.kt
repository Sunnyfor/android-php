package com.cocosh.shmstore.widget.imageview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.layout_update_idcard.view.*

/**
 * 身份证上传
 * Created by zhangye on 2018/3/26.
 */
class IDcardView : RelativeLayout, View.OnClickListener {
    private var direction = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_update_idcard, this, true)
    }


    fun setType(direction: Boolean) {
        this.direction = direction
        if (direction) {
            ivBg.setImageResource(R.drawable.bg_default_id_back)
            tvDesc.text = "点击扫描身份证 背面"
        }
    }

    override fun onClick(v: View?) {
        if (direction) {

        } else {

        }
    }

    //加载身份证图片
    fun loadImage(path: String) {
        tvDesc.visibility = View.GONE
        GlideUtils.noCacheload(context, path, ivBg)
    }

    fun defaultImage() {
        tvDesc.visibility = View.VISIBLE

        if (direction) {
            ivBg.setImageResource(R.drawable.bg_default_id_back)
            tvDesc.text = "点击扫描身份证 背面"
        } else {
            ivBg.setImageResource(R.drawable.bg_default_id_front)
            tvDesc.text = "点击扫描身份证 正面"
        }
    }

}