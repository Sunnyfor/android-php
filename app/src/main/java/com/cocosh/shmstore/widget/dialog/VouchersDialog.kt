package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import kotlinx.android.synthetic.main.dialog_vouchers.*

class VouchersDialog(context:Context,var result:()->Unit): Dialog(context),View.OnClickListener {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_vouchers)
        rlBg.setOnClickListener(this)
        window.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onClick(v: View?) {
        result()
    }
}