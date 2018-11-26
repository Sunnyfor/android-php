package com.cocosh.shmstore.mine.ui

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.home.MessageFragment


class MessageActivity: BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_message

    override fun initView() {
        titleManager.defaultTitle("消息")
        supportFragmentManager.beginTransaction().add(R.id.content,MessageFragment()).commit()
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}