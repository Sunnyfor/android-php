package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.mine.model.BankModel
import com.cocosh.shmstore.widget.dialog.adapter.BankListAdapter
import kotlinx.android.synthetic.main.dialog_bank_list_layout.*


/**
 *
 * Created by lmg on 2018/3/23.
 */
class BankListDialog(context: Context?, list: ArrayList<BankModel>) : Dialog(context), View.OnClickListener {
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
        setContentView(R.layout.dialog_bank_list_layout)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = BankListAdapter(context!!, list)
        (recyclerView.adapter as BankListAdapter).setOnItemClickListener(object : com.cocosh.shmstore.base.OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onItemClick(v, index)
                    dismiss()
                }
            }
        })
        tvCancel!!.setOnClickListener(this)
        setCancelable(true)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvCancel -> this.dismiss()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, index: Int)
    }

    var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }
}

