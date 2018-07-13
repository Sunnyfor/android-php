package com.cocosh.shmstore.about

import android.content.Intent
import android.os.SystemClock
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.ui.DeveloperActivity
import com.cocosh.shmstore.utils.IntentCode
import kotlinx.android.synthetic.main.activity_about.*


/**
 * 关于首媒
 * Created by zhangye on 2018/4/8.
 */
class AboutActivity : BaseActivity() {
    private var mHits:LongArray? = null

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_about

    override fun initView() {
        titleManager.defaultTitle("关于首媒")

        tvDesc.text = ("版本号：首媒${SmApplication.getApp().getVersionName()}")
        tvDesc.setOnClickListener(this)
    }

    override fun onListener(view: View) {
    }

    override fun onClick(view: View) {
        when(view.id){
            tvDesc.id -> {
                intentDeveloperActivity()
            }
        }
    }


    private fun intentDeveloperActivity() {
        if (mHits == null) {
            mHits = LongArray(5)
        }

        mHits?.let {
            System.arraycopy(it, 1, it, 0, it.size - 1)//把从第二位至最后一位之间的数字复制到第一位至倒数第一位
            it[it.size - 1] = SystemClock.uptimeMillis()//记录一个时间
            if (it[0] > SystemClock.uptimeMillis() - 1500) {//一秒内连续点击。
                mHits = null	//这里说明一下，我们在进来以后需要还原状态，否则如果点击过快，第六次，第七次 都会不断进来触发该效果。重新开始计数即可
                startActivity(Intent(this,DeveloperActivity::class.java))
                setResult(IntentCode.FINISH)
            }
        }
    }
}