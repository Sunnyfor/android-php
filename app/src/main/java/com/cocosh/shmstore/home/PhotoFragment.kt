package com.cocosh.shmstore.home

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_photo.view.*

/**
 *
 * Created by zhangye on 2018/7/20.
 */
class PhotoFragment: BaseFragment() {

    var onClickListener:View.OnClickListener? = null
    var res = 0

    override fun setLayout(): Int = R.layout.fragment_photo

    override fun initView() {
        if (res != 0){
            getLayoutView().imgPhoto.setImageResource(res)
        }
        getLayoutView().imgPhoto.setOnClickListener(this)
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
        if (view.id == getLayoutView().imgPhoto.id){
            onClickListener?.onClick(view)
        }
    }

    override fun close() {

    }

}