package com.cocosh.shmstore.widget.popupwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.PopupWindow
import com.cocosh.shmstore.R
import com.cocosh.shmstore.newhome.model.GoodsNav
import com.cocosh.shmstore.widget.popupwindow.adapter.GoodsNavAdapter
import kotlinx.android.synthetic.main.layout_goods_nav_popup.view.*

class GoodsNavPopup(context: Context, mList: ArrayList<GoodsNav.Data>, index:Int,result:(Int)->Unit) : PopupWindow(context)  {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(R.layout.layout_goods_nav_popup,null)
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.WRAP_CONTENT

        isFocusable = true
        setBackgroundDrawable(ColorDrawable(0x7f000000))
        contentView.recyclerView.layoutManager = GridLayoutManager(context,4)
        contentView.recyclerView.adapter = GoodsNavAdapter(mList,index,result,this)

        contentView.setOnClickListener {
            dismiss()
        }
    }


}