package com.cocosh.shmstore.home

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity

/**
 * 红包图文页
 * Created by zhangye on 2018/4/25.
 */
class BonusTextActivity: BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_bonus_text

    override fun initView() {
        titleManager.defaultTitle("红包名称")
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}