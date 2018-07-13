package com.cocosh.shmstore.home

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_bonus_none.*


/**
 * 红包没钱页面
 * Created by zhangye on 2018/5/21.
 */
class BonusNoneActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_bonus_none

    override fun initView() {
        titleManager.defaultTitle("红包")

        intent.getStringExtra("type")?.let {
            if (it == "timeOut"){
                tvDesc.text = ""
            }
        }


    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}