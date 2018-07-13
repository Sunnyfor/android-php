package com.cocosh.shmstore.mine.ui

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity


/**
 * 新消息通知页面
 * Created by zhangye on 2018/4/17.
 */
class NewMessageNotifyActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_new_message_notify
    override fun initView() {
        titleManager.defaultTitle("新消息通知")
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {
    }
}