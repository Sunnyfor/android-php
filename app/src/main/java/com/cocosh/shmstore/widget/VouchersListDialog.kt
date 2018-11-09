package com.cocosh.shmstore.widget

import android.app.Dialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.vouchers.model.Vouchers
import com.cocosh.shmstore.widget.dialog.adapter.VouchersListDialogAdapter
import kotlinx.android.synthetic.main.dialog_vouchers_list.*

@Suppress("UNCHECKED_CAST")
class VouchersListDialog(var arrayList: ArrayList<Vouchers>, selectMap:HashMap<String,Vouchers> = HashMap(), var activity: BaseActivity, var action: () -> Unit) : Dialog(activity), View.OnClickListener {

    private val vouchersListDialogAdapter: VouchersListDialogAdapter by lazy {
        VouchersListDialogAdapter(arrayList)
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_vouchers_list)
        tvBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        window.setBackgroundDrawableResource(R.color.transparent)
        recyclerView.layoutManager = LinearLayoutManager(context)
        vouchersListDialogAdapter.selectMap = selectMap.clone() as HashMap<String, Vouchers>
        recyclerView.adapter = vouchersListDialogAdapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            tvBack.id -> {
                dismiss()
            }
            btnNext.id -> {
                if (vouchersListDialogAdapter.selectMap.isNotEmpty()) {
                    SmApplication.getApp().setData(DataCode.VOUCHERS_SELECT, vouchersListDialogAdapter.selectMap)
                    dismiss()
                    action()
                }else{
                    ToastUtil.show("请选择红包礼券！")
                }
            }

        }
    }
}