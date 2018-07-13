package com.cocosh.shmstore.mine.ui

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.SharedUtil
import com.cocosh.shmstore.utils.UserManager
import kotlinx.android.synthetic.main.activity_developer.*

/**
 * 开发者调试环境
 * Created by zhangye on 2018/6/27.
 */
class DeveloperActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_developer

    override fun initView() {
        titleManager.defaultTitle("开发者调试")
        tvName.text = ("当前环境:${ApiManager.getHost()}")
        btnSure.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                UserManager.setEmptyUser() //删除登录状态
                SharedUtil.setBoolean(DataCode.ISDEBUG,!Constant.isDebug())
                tvName.text = ("当前环境:${ApiManager.getHost()}")
                ApiManager.init()
            }
        }
    }

    override fun reTryGetData() {

    }
}