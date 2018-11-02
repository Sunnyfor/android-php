package com.cocosh.shmstore.vouchers

import android.support.v4.app.Fragment
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.vouchers.apdater.VouchersViewPagerAdapter
import com.cocosh.shmstore.vouchers.fragment.VouchersListFragment
import kotlinx.android.synthetic.main.activity_vouchers_list.*


class VouchersListActivity : BaseActivity() {

    private var fragmentList = ArrayList<Fragment>()

    override fun setLayout(): Int = R.layout.activity_vouchers_list

    override fun initView() {
        titleManager.rightText("我的红包礼券", "活动规则", View.OnClickListener {

        })

        fragmentList.add(VouchersListFragment().setType(0))
        fragmentList.add(VouchersListFragment().setType(1))
        fragmentList.add(VouchersListFragment().setType(2))
        fragmentList.add(VouchersListFragment().setType(3))
        viewPager.offscreenPageLimit = 3

        val vouchersViewPagerAdapter = VouchersViewPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = vouchersViewPagerAdapter
        tabStrip.setViewPager(viewPager)
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

}