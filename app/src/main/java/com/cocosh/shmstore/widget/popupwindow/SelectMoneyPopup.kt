package com.cocosh.shmstore.widget.popupwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.cocosh.shmstore.R
import com.cocosh.shmstore.mine.model.ADRechargeMoney
import com.cocosh.shmstore.widget.popupwindow.adapter.SelectMoneyAdapter
import kotlinx.android.synthetic.main.layout_select_money_popup.view.*

class SelectMoneyPopup(context: Context,mList: ArrayList<ADRechargeMoney>) : PopupWindow(context) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(R.layout.layout_select_money_popup, null, false)
        width = context.resources.getDimension(R.dimen.w719).toInt()
        height = context.resources.getDimension(R.dimen.h469).toInt()
        setBackgroundDrawable(ColorDrawable(0))
        isFocusable = true
        isOutsideTouchable = true
        contentView.recyclerView.layoutManager = LinearLayoutManager(context)
        contentView.recyclerView.adapter = SelectMoneyAdapter(mList)
    }

}