package com.cocosh.shmstore.newCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.tencent.tauth.AuthActivity
import kotlinx.android.synthetic.main.activity_partner_success.*

/**
 *
 * Created by cjl on 2018/2/9.
 */
class SuccessActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_partner_success

    override fun initView() {
        titleManager.defaultTitle(getString(R.string.newAuthen), "", 0, 0, null)
        itv.finishSuccess()
        btn.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.btn ->startActivity(Intent(this,AuthActivity::class.java))
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SuccessActivity::class.java))
        }
    }
}