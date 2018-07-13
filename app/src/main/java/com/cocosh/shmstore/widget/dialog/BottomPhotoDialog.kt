package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import kotlinx.android.synthetic.main.dialog_photo_layout.*


/**
 *
 * Created by lmg on 2018/3/23.
 */
class BottomPhotoDialog(context: Context?) : Dialog(context), View.OnClickListener {
    private var listener: OnCompleteListener? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 让dialog置于底部
        val window = window!!
        window.decorView.setPadding(0, 0, 0, 0)
        val attributes = window.attributes
        attributes.width = RelativeLayout.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window.attributes = attributes
        window.setBackgroundDrawableResource(R.color.transparent)
        setContentView(R.layout.dialog_photo_layout)

        tvCancel!!.setOnClickListener(this)
        tv_one.setOnClickListener(this)
        tv_two.setOnClickListener(this)
        tv_three.setOnClickListener(this)
        setCancelable(true)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvCancel -> this.dismiss()
            R.id.tv_one -> {
                this.dismiss()
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onTopClick()
                }
            }
            R.id.tv_two -> {
                this.dismiss()
                //读取
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onBottomClick()
                }
            }
            R.id.tv_three -> {
                this.dismiss()
                //拍照
            }
        }
    }

    interface OnCompleteListener {
        fun onComplete()
    }

    /**
     * 设置完成的监听
     *
     * @param listener
     */
    fun setOnCompleteListener(listener: OnCompleteListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onTopClick()
        fun onBottomClick()
    }

    var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }


    fun setValue(topValue: String, bottomValue: String) {
        tv_one.text = topValue
        tv_two.text = bottomValue
    }
}

