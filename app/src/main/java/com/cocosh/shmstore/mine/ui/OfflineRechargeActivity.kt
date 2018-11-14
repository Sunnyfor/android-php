package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.model.ADRechargeMoney
import com.cocosh.shmstore.widget.popupwindow.SelectMoneyPopup
import kotlinx.android.synthetic.main.activity_offline_recharge.*

class OfflineRechargeActivity: BaseActivity() {
    val list = arrayListOf<ADRechargeMoney>()

    override fun setLayout(): Int = R.layout.activity_offline_recharge

    override fun initView() {
        titleManager.defaultTitle("专属转账通道")

        rlSelectMoney.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        list.add(ADRechargeMoney("100000.00","5000充值券"))
        list.add(ADRechargeMoney("200000.00","10000充值券"))
        list.add(ADRechargeMoney("300000.00","20000充值券"))
        list.add(ADRechargeMoney("400000.00","25000充值券"))
        list.add(ADRechargeMoney("500000.00","50000充值券"))
    }

    override fun onListener(view: View) {
        when(view.id){
            rlSelectMoney.id -> {
                SelectMoneyPopup(this,list).showAsDropDown(rlSelectMoney)
            }

            btnNext.id -> {
                startActivity(Intent(this,OfflineCommitActivity::class.java))
            }
        }
    }

    override fun reTryGetData() {

    }
}