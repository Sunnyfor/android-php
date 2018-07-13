package com.cocosh.shmstore.person

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.model.AuthenEnter
import com.cocosh.shmstore.mine.ui.ArchiveActivity
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.utils.DataCode
import kotlinx.android.synthetic.main.activity_enterprise_certi_success.*


/**
 * 认证成功页面（实人）
 * Created by zhangye on 2018/3/23.
 */
class PersonSuccessActivity : BaseActivity() {

    var authenEnter: AuthenEnter? = null

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_person_success

    override fun initView() {
        titleManager.defaultTitle("实人认证").setLeftOnClickListener(View.OnClickListener {
            finish()
        })

        authenEnter = SmApplication.getApp().getData<AuthenEnter>(DataCode.AUTHER_ENTER, true)
        initData()
        btGoActive.setOnClickListener(this)
    }

    private fun initData() {
        icon.finishSuccess()
    }

    override fun onListener(view: View) {
        when (view.id) {
            btGoActive.id -> {
                finish()
            }
        }
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, PersonSuccessActivity::class.java))
        }
    }

    override fun finish() {
        super.finish()
        if (authenEnter == AuthenEnter.ARCHIVE) {
            startActivity(Intent(this@PersonSuccessActivity, ArchiveActivity::class.java))
        } else {
            AuthActivity.Companion.start(this@PersonSuccessActivity)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}