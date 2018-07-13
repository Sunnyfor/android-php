package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.android.synthetic.main.dialog_shoumei_comment.*

/**
 * 首媒之家选择回复还是删除弹窗
 * Created by lmg on 2018/1/27.
 */
class ShouMeiCommentDialog : Dialog, View.OnClickListener {
    private var baseActivity: BaseActivity

    interface OnItemClickListener {
        fun onDelete()
        fun onReply()
        fun cancel()
    }

    var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    private var cancleIsFinish = false

    constructor(context: Context?) : this(context, 0)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        baseActivity = context as BaseActivity
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_shoumei_comment)
        tvCancel.setOnClickListener(this)
        tv_one.setOnClickListener(this)
        tv_two.setOnClickListener(this)
        setCanceledOnTouchOutside(false)
        window.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onClick(v: View) {
        when (v.id) {
            tvCancel.id -> {
                mOnItemClickListener?.cancel()
                dismiss()
                if (cancleIsFinish) {
                    baseActivity.finish()
                }
            }
            tv_one.id -> {
                mOnItemClickListener?.onReply()
                dismiss()
            }
            tv_two.id -> {
                mOnItemClickListener?.onDelete()
                dismiss()
            }
        }
    }

    fun setCancelText(posiText: String) {
        tvCancel.text = posiText
    }

    fun singleButton() {
        tv_two.visibility = View.GONE
        line_two.visibility = View.GONE
    }

    //取消按钮销毁页面
    fun cancleFinish() {
        cancleIsFinish = true
    }

}
