package com.cocosh.shmstore.home.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import android.view.LayoutInflater
import android.view.View
import com.cocosh.shmstore.R


/**
 * Created by lmg on 2018/1/17.
 */
class ShoumeiListAdapter(var context: Context, fm: FragmentManager, list: List<Fragment>, var mName: List<String>) : FragmentPagerAdapter(fm) {
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

    fun getTabView(position: Int): View {
        val tabView = LayoutInflater.from(context).inflate(R.layout.tab_view, null)
        val tabTitle = tabView.findViewById(R.id.tv_tab_title) as TextView
        tabTitle.text = mName[position]
        return tabView
    }

}