package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import kotlinx.android.synthetic.main.layout_interesting_dialog.*

/**
 * 自定义兴趣爱好弹窗
 * Created by zhangye on 2018/5/9.
 */
class InterestingDialog(context: Context?) : Dialog(context),View.OnClickListener {
    var onSureClickResult: OnSureClickResult? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_interesting_dialog)
        tvCancel.setOnClickListener(this)
        tvSure.setOnClickListener(this)
        setCanceledOnTouchOutside(false)
        window.setBackgroundDrawableResource(R.color.transparent)
    }


    override fun onClick(v: View) {
        when(v.id){
            tvCancel.id -> dismiss()

            tvSure.id -> {
                onSureClickResult?.onSureClick(edtName.text.toString())
                dismiss()
            }
        }
    }

    interface OnSureClickResult{
        fun onSureClick(value:String)
    }
}