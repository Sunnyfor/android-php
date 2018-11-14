package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_recharge_balance.*

class ADRBalanceActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_recharge_balance

    override fun initView() {
        titleManager.rightText("我的充值券余额", "充值记录", View.OnClickListener {

        })

        llDesc.setOnClickListener {
            if (llContent.visibility == View.GONE) {
                llContent.visibility = View.VISIBLE
            } else {
                llContent.visibility = View.GONE
            }
        }

        val spannableString = SpannableString(resources.getString(R.string.ad_recharge_desc2))
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this,R.color.red)),spannableString.length-7,spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAd_recharge_desc2.text = spannableString

        btnRecharge.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when(view.id){
            btnRecharge.id -> {
                startActivity(Intent(this,ADRechargeActivity::class.java))
            }
        }

    }

    override fun reTryGetData() {

    }
}