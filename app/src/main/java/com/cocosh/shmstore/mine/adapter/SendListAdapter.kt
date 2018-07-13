package com.cocosh.shmstore.mine.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 *
 * Created by lmg on 2018/1/17.
 */
class SendListAdapter(fm: FragmentManager, var list: List<Fragment>, private var mName: List<String>) : FragmentPagerAdapter(fm) {
    //    private var mfragmentManager: FragmentManager? = null

    override fun getCount(): Int = list.size


    override fun getItem(position: Int): Fragment = list[position]//显示第几个页面

    override fun getPageTitle(position: Int): CharSequence = mName[position]
}