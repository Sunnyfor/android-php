package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.ui.CreateOrderActivity
import kotlinx.android.synthetic.main.activity_bonus_goods.*

/**
 * 红包商品页
 * Created by zhangye on 2018/4/24.
 */
class BonusGoodsActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_bonus_goods

    override fun initView() {
        titleManager.defaultTitle("红包名称")
        btnSure.setOnClickListener(this)
        btnOpen.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                //确认订单页
                CreateOrderActivity.start(this)
            }

            btnOpen.id -> {
                startActivity(Intent(this, BonusDetailActivity::class.java))
            }
        }
    }

    override fun reTryGetData() {
    }
}