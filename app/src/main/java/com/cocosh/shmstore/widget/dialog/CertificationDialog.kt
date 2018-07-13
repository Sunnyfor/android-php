package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import kotlinx.android.synthetic.main.dialog_certification_main.*

/**
 * 首媒默认样式的对话框
 * Created by zhangye on 2018/1/27.
 */
class CertificationDialog : Dialog, View.OnClickListener {

    var OnClickListener: View.OnClickListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_certification_main)
        tvCancel.setOnClickListener(this)
        setCanceledOnTouchOutside(false)
        window.setBackgroundDrawable(ColorDrawable())
        window.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onClick(v: View) {
        when (v.id) {
            tvCancel.id -> {
                dismiss()
            }
        }
    }

    fun setDesc(desc: String) {
        tv_desc.text = Html.fromHtml(desc)
    }
}
