package com.cocosh.shmstore.mine.ui

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity


/**
 *
 * Created by zhangye on 2018/6/21.
 */
class HelpActivity: BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_help

    override fun initView() {
        titleManager.defaultTitle("帮助中心")
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}