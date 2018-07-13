package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity

/**
 *
 * Created by cjl on 2018/3/13.
 * 激活失败
 */
class CompanyAuthStatueActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    override fun setLayout(): Int {
        return R.layout.activity_company_auth_stute
    }

    override fun initView() {
        titleManager.defaultTitle("企业主认证激活")
    }

    override fun onListener(view: View) {
    }

}
