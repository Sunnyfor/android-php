package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 *
 * Created by zhangye on 2018/5/19.
 */
class SplashActivity : BaseActivity() {

    private var time = 3 * 1000

    override fun setLayout(): Int = R.layout.activity_splash
    override fun initView() {

        titleManager.goneTitle()

        launch(UI) {
            delay(time)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    override fun onBackPressed() {}
}