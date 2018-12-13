package com.cocosh.shmstore.newhome

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity

class GoodsErrorActivity: BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_goods_error

    override fun initView() {
        titleManager.defaultTitle("商品已下架")
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }


}
