package com.cocosh.shmstore.vouchers

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.title.LeftRightTitleFragment
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.vouchers.apdater.VouchersListSelectAdapter
import com.cocosh.shmstore.vouchers.model.Vouchers
import kotlinx.android.synthetic.main.activity_vouchers_list_select.*

class VouchersListSelectActivity : BaseActivity() {
    private var titleFragment: LeftRightTitleFragment? = null
    private  var adapter:VouchersListSelectAdapter? = null
    override fun setLayout(): Int = R.layout.activity_vouchers_list_select

    override fun initView() {
        titleFragment = titleManager.rightText("我的红包礼券", "全选", View.OnClickListener {
            adapter?.allSelect()
        })

        SmApplication.getApp().getData<ArrayList<Vouchers>>(DataCode.VOUCHERS, true)?.let { list ->
            adapter = VouchersListSelectAdapter(list,intent.getIntExtra("index",-1)){
                titleFragment?.getRightText()?.text = it
            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }
}