package com.cocosh.shmstore.newhome

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_refund.*

class RefundActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_refund

    override fun initView() {
        view_up.showLoading = {
            showLoading()
        }
        view_up.hideLoading = {
            hideLoading()
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        view_up.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}