package com.cocosh.shmstore.vouchers

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.home.SendBonusActivity
import com.cocosh.shmstore.title.LeftRightTitleFragment
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.vouchers.apdater.VouchersListSelectAdapter
import com.cocosh.shmstore.vouchers.model.Vouchers
import kotlinx.android.synthetic.main.activity_vouchers_list_select.*

class VouchersListSelectActivity : BaseActivity() {
    private var titleFragment: LeftRightTitleFragment? = null
    private var adapter: VouchersListSelectAdapter? = null
    override fun setLayout(): Int = R.layout.activity_vouchers_list_select

    override fun initView() {
        titleFragment = titleManager.rightText("我的红包礼券", "全选", View.OnClickListener {
            adapter?.allSelect()
        })

        SmApplication.getApp().getData<ArrayList<Vouchers>>(DataCode.VOUCHERS_LIST, true)?.let { list ->
            adapter = VouchersListSelectAdapter(list, intent.getIntExtra("index", -1)) {
                titleFragment?.getRightText()?.text = it
            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }

        btnUse.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnUse.id -> {
                SmApplication.getApp().setData(DataCode.VOUCHERS_SELECT, adapter?.selectMap)
                startActivity(Intent(this, SendBonusActivity::class.java)
                        .putExtra("type", "comm_person"))
            }
        }
    }

    override fun reTryGetData() {

    }
}