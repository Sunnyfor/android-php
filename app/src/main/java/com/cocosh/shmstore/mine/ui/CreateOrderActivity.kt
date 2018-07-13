package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newCertification.ui.PayActivity
import kotlinx.android.synthetic.main.activity_create_order.*

/**
 * Created by lmg on 2018/4/25.
 * 订单详情
 */
class CreateOrderActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_create_order

    override fun initView() {
        titleManager.defaultTitle("确认订单")
        chooseAddress.setOnClickListener(this)
        pay.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            chooseAddress.id -> {
                AddressMangerActivity.start(this)
            }
            pay.id -> {
                PayActivity.start(this,"343","100","")
            }
            else -> {
            }
        }
    }



    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, CreateOrderActivity::class.java))
        }
    }
}