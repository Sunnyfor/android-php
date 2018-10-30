package com.cocosh.shmstore.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
class SplashActivity : AppCompatActivity() {

    private var time = 1 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        launch(UI) {
//            delay(time)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
//        }
    }


    override fun onBackPressed() {}
}