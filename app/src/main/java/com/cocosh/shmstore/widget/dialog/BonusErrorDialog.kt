package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.dialog_bonus_error.*

/**
 *
 * Created by zhangye on 2018/5/29.
 */
class BonusErrorDialog : Dialog, View.OnClickListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    override fun onClick(v: View) {
        if (v.id == isv_cancle.id) {
            dismiss()
        }
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bonus_error)
        setCanceledOnTouchOutside(false)
        isv_cancle.setOnClickListener(this)
        window.setBackgroundDrawableResource(R.color.transparent)
    }

    //抢光
    fun showNone(head: String?, nickname: String?) {
        tvDesc.text = "手慢了!红包已被抢空"
        load(head, nickname)
    }


    //过期
    fun showOverdue(head: String?, nickname: String?) {
        tvDesc.text = "该红包已过期"
        load(head, nickname)
    }

    private fun load(head: String?, nickname: String?) {
        head?.let {
            GlideUtils.loadHead(context, it, ivHead)
        }
        nickname?.let {
            tvName.text = it
        }
        show()
    }
}