package com.cocosh.shmstore.newhome.fragment

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.title.DefaultTitleFragment

class ShoppingFragment: BaseFragment() {
    override fun setLayout(): Int = R.layout.fragment_shopping_car

    override fun initView() {
        val defaultTitleFragment = DefaultTitleFragment()
        defaultTitleFragment.singleText()
        defaultTitleFragment.title("购物车")
        showTitle(defaultTitleFragment)
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }
}