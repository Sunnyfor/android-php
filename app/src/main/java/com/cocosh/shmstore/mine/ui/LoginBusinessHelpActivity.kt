package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newhome.HomeActivity
import kotlinx.android.synthetic.main.activity_login_business_help.*

class LoginBusinessHelpActivity: BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_login_business_help
    override fun initView() {
        titleManager.defaultTitle("如何登录企业主后台")
        tvDesc.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when(view.id){
            tvDesc.id ->{
                startActivity(Intent(this,HomeActivity::class.java).putExtra("type","Forum"))
            }
        }

    }

    override fun reTryGetData() {

    }
}