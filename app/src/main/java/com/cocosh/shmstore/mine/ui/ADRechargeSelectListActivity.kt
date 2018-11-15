package com.cocosh.shmstore.mine.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.adapter.ADRechargeSelectAdapter
import com.cocosh.shmstore.vouchers.model.Vouchers
import kotlinx.android.synthetic.main.activity_ad_recharge_list.*

class ADRechargeSelectListActivity : BaseActivity() {

    var list = ArrayList<Vouchers>()

    override fun setLayout(): Int = R.layout.activity_ad_recharge_list

    override fun initView() {
        titleManager.rightText("立即使用", "全选", View.OnClickListener {

        })

        recyclerView.layoutManager = LinearLayoutManager(this)


        list.add(Vouchers("1", "5000", "5000", "2018.11.30", "2018.12.31", "5000"))
        list.add(Vouchers("2", "10000", "10000", "2018.11.30", "2018.12.31", "10000"))
        list.add(Vouchers("3", "15000", "15000", "2018.11.30", "2018.12.31", "15000"))
        recyclerView.adapter = ADRechargeSelectAdapter(list)
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}