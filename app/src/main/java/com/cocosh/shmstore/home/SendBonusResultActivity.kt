package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.ui.authentication.SendPackageActivity
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.utils.DataCode
import kotlinx.android.synthetic.main.layout_send_bonus_result.*


/**
 *
 * Created by zhangye on 2018/6/7.
 */
class SendBonusResultActivity : BaseActivity() {

    private var type: String? = null  //0是成功 1是等待 2是失败

    override fun setLayout(): Int = R.layout.layout_send_bonus_result

    override fun initView() {
        type = intent.getStringExtra("type")
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        titleManager.defaultTitle("发红包").setLeftOnClickListener(View.OnClickListener {
            if (type == "0") {
                SmApplication.getApp().clearActivity(DataCode.BONUS_SEND_ACTIVITYS)
            }
            finish()
        })
        type?.let {

            if (type == "0") {
                tvDesc.text = "提交成功，审核中"
                viewIcon.setBackgroundResource(R.drawable.success_logo)
                SmApplication.getApp().setData(DataCode.BONUS_PAY,true)
            }


            if (type == "1") {
                tvDesc.text = "银行处理中"
                viewIcon.setBackgroundResource(R.drawable.icon_wait)
                tvDesc2.visibility = View.VISIBLE
            }

            if (type == "2") {
                tvDesc.text = "支付失败"
                button1.text = "重新付款"
                viewIcon.setBackgroundResource(R.drawable.error_fail)
            }
        }

    }

    override fun onListener(view: View) {
        when (view.id) {
            button0.id -> {
                SmApplication.getApp().clearActivity(DataCode.BONUS_SEND_ACTIVITYS)
                startActivity(Intent(this, BonusPoolActivity::class.java))
                finish()
            }

            button1.id -> {
                if (type == "2") {
                    finish()  //关闭页面
                } else {
                    SmApplication.getApp().clearActivity(DataCode.BONUS_SEND_ACTIVITYS)
                    startActivity(Intent(this, SendPackageActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun reTryGetData() {
    }

    override fun onBackPressed() {
        return
    }
}