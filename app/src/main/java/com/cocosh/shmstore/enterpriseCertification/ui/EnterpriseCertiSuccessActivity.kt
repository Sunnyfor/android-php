package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.ui.AuthActivity
import kotlinx.android.synthetic.main.activity_person_success.*


/**
 * 认证成功页面（待激活）
 * Created by zhangye on 2018/3/23.
 */
class EnterpriseCertiSuccessActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_enterprise_certi_success

    override fun initView() {
        titleManager.defaultTitle("企业主认证")
        initData()
        btGoActive.setOnClickListener(this)
    }

    private fun initData() {
        icon.finishSuccess()
    }

    override fun onListener(view: View) {
        when (view.id) {
            btGoActive.id -> {
                startActivity(Intent(this, EnterpriseActiveActivity::class.java))
                finish()
            }
        }
    }
}