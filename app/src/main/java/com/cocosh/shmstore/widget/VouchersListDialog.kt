package com.cocosh.shmstore.widget

import android.app.Dialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.vouchers.model.Vouchers
import com.cocosh.shmstore.widget.dialog.adapter.VouchersListDialogAdapter
import kotlinx.android.synthetic.main.dialog_vouchers_list.*

class VouchersListDialog(var arrayList:ArrayList<Vouchers>,var activity: BaseActivity): Dialog(activity),View.OnClickListener {

    private val vouchersListDialogAdapter:VouchersListDialogAdapter by lazy {
        VouchersListDialogAdapter(arrayList)
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_vouchers_list)
        tvBack.setOnClickListener(this)
        window.setBackgroundDrawableResource(R.color.transparent)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = vouchersListDialogAdapter
    }

    override fun onClick(v: View) {
        when(v.id){
            tvBack.id -> {
                dismiss()
            }
        }
    }
}