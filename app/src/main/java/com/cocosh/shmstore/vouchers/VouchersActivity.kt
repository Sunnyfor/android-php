package com.cocosh.shmstore.vouchers

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.home.SendBonusActivity
import kotlinx.android.synthetic.main.activity_vouchers.*


class VouchersActivity : BaseActivity() {

    override fun setLayout(): Int = R.layout.activity_vouchers

    override fun initView() {
        titleManager.rightText("我的红包礼券", "活动规则", View.OnClickListener {

        })
        tvNext.setOnClickListener(this)
        tvDesc.setOnClickListener(this)

        val money = intent.getStringExtra("money")
        tvMoney.text = money

        if (money.isNullOrEmpty() || money == "0") {
            tvHint.text = "手慢了！红包礼券已被抢光了！"
            tvNext.visibility = View.GONE
            tvDesc.visibility = View.GONE
            btnShare.visibility = View.GONE
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            tvNext.id -> startActivity(Intent(this, SendBonusActivity::class.java).putExtra("type", "comm_person"))
            tvDesc.id -> startActivity(Intent(this, VouchersListActivity::class.java))
        }
    }

    override fun reTryGetData() {
    }
}