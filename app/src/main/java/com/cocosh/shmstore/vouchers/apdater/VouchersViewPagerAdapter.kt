package com.cocosh.shmstore.vouchers.apdater

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class VouchersViewPagerAdapter(fm: FragmentManager, var fragmentList: ArrayList<Fragment>): FragmentPagerAdapter(fm) {
    private val titleList = arrayOf("未使用(0)","已使用(0)","已赠送(0)","已过期(0)")


    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }
}