package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.mine.model.IndustryModel
import com.cocosh.shmstore.widget.dialog.adapter.SelectDialogAdapter
import kotlinx.android.synthetic.main.layout_select_dialog.*

/**
 * 选择对话框
 * Created by zhangye on 2018/4/12.
 */
class SelectDialog(context: Context?) : Dialog(context) {

    var onDialogResult:OnDialogResult? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_select_dialog)
    }

    fun setData(datas: ArrayList<IndustryModel>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SelectDialogAdapter(datas)
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                adapter.index = index
                adapter.notifyDataSetChanged()
                onDialogResult?.onResult(adapter.getData(index))
            }
        })
        recyclerView.adapter = adapter
    }



}