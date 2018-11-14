package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ad_recharge_success.*

class AdRechargeSuccessActivity: BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_ad_recharge_success

    override fun initView() {
        titleManager.defaultTitle("充值成功")
        tvLoin.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when(view.id){
            tvLoin.id -> {
                startActivity(Intent(this,LoginBusinessHelpActivity::class.java))
            }

        }
    }

    override fun reTryGetData() {

    }
}