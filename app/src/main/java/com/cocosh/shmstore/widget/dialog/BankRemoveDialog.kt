package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.dialog_bankcard_remove.*

/**
 * 银行卡去除绑定
 * Created by lmg on 2018/04/16.
 */
class BankRemoveDialog : Dialog, View.OnClickListener {
    private var baseActivity: BaseActivity
    var OnClickListener: View.OnClickListener? = null
    private var cancleIsFinish = false

    constructor(context: Context?) : this(context, 0)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        baseActivity = context as BaseActivity
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.setPadding(0, 0, 0, 0)
        val attributes = window.attributes
        attributes.width = RelativeLayout.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.CENTER_HORIZONTAL
        window.attributes = attributes
        window.setBackgroundDrawableResource(R.color.transparent)
        setContentView(R.layout.dialog_bankcard_remove)
        cancel.setOnClickListener(this)
        btnBankOk.setOnClickListener(this)
        setCanceledOnTouchOutside(false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            cancel.id -> {
                dismiss()
                if (cancleIsFinish) {
                    baseActivity.finish()
                }
            }
            btnBankOk.id -> {
                OnClickListener?.onClick(v)
                dismiss()
            }
        }
    }

    fun setData(mContext: Context?, pic: String?, name: String?, num: String?) {
        GlideUtils.load(mContext, pic, rivLogo)
        Glide.with(mContext).load(pic).placeholder(R.drawable.default_content).into(rivLogo)
        tvBankName.text = name
        tvBankNum.text = num
    }

    //取消按钮销毁页面
    fun cancleFinish() {
        cancleIsFinish = true
    }
}
