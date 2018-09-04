package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.adapter.SendListAdapter
import kotlinx.android.synthetic.main.activity_order_list.*
import android.widget.LinearLayout


/**
 * 订单
 * Created by lmg on 2018/4/20.
 */
class OrderListActivity : BaseActivity() {
    var list = arrayListOf<String>()
    var type: String = "全部"
    override fun setLayout(): Int = R.layout.activity_order_list
    override fun initView() {
        titleManager.defaultTitle("订单")
        initData()
    }

    private fun initData() {
        //测试数据
        list.add("待付款")
        list.add("待发货")
        list.add("待收货")
        list.add("交易完成")
        //设置ViewPager里面也要显示的图片
        val fragmentList = arrayListOf<Fragment>()
        val f1 = OrderListFragment()
        val b1 = Bundle()
        b1.putString("TYPE", "全部")
        f1.arguments = b1

        val f2 = OrderListFragment()
        val b2 = Bundle()
        b2.putString("TYPE", "待付款")
        f2.arguments = b2

        val f3 = OrderListFragment()
        val b3 = Bundle()
        b3.putString("TYPE", "待发货")
        f3.arguments = b3

        val f4 = OrderListFragment()
        val b4 = Bundle()
        b4.putString("TYPE", "待收货")
        f4.arguments = b4

        val f5 = OrderListFragment()
        val b5 = Bundle()
        b5.putString("TYPE", "交易完成")
        f5.arguments = b5
        fragmentList.add(f1)
        fragmentList.add(f2)
        fragmentList.add(f3)
        fragmentList.add(f4)
        fragmentList.add(f5)
        //设置标题
        val titleList = arrayListOf<String>()
        titleList.add("全部")
        titleList.add("待付款")
        titleList.add("待发货")
        titleList.add("待收货")
        titleList.add("交易完成")

        //设置tab的模式
        tab.tabMode = TabLayout.MODE_FIXED
        //添加tab选项卡

        titleList.forEach {
            tab.addTab(tab.newTab().setText(it))
        }
        val linearLayout = tab.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = ContextCompat.getDrawable(this,
                R.drawable.layout_divider_vertical)
        linearLayout.dividerPadding = resources.getDimension(R.dimen.h30).toInt()
        //给ViewPager绑定Adapter
        viewPager.adapter = SendListAdapter(supportFragmentManager, fragmentList, titleList)
        viewPager.offscreenPageLimit = 4
        //把TabLayout和ViewPager关联起来
        tab.setupWithViewPager(viewPager)
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, OrderListActivity::class.java))
        }
    }
}