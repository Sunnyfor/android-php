package com.cocosh.shmstore.mine.ui

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity

class OfflineCommitActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_offline_commit

    override fun initView() {
        titleManager.rightText("提交成功", "完成", View.OnClickListener {
                finish()
        }).goneLeft()
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}