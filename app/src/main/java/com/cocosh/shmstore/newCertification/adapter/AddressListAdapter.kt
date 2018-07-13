package com.cocosh.shmstore.newCertification.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 * Created by lmg on 2018/1/17.
 */
class AddressListAdapter(fm: FragmentManager, list: List<Fragment>, var mName: List<String>) : FragmentPagerAdapter(fm) {
    //    private var mfragmentManager: FragmentManager? = null
    private var mlist: List<Fragment>? = null

    init {
        this.mlist = list
    }

    override fun getCount(): Int {
        return mlist?.size!!
    }


    override fun getItem(position: Int): Fragment {
        return mlist?.get(position)!!//显示第几个页面
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mName[position]
    }
}