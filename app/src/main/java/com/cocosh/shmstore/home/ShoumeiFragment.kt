package com.cocosh.shmstore.home

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.adapter.ShoumeiListAdapter
import com.cocosh.shmstore.utils.ViewUtil
import kotlinx.android.synthetic.main.fragment_shoumei.view.*


/**
 * 首媒之家
 */
class ShoumeiFragment : BaseFragment() {
    var fragmentList = arrayListOf<Fragment>()
    lateinit var fragment1: ShoumeiFollowFragment
    lateinit var fragment2: ShoumeiFindFragment
    override fun setLayout(): Int = R.layout.fragment_shoumei

    override fun initView() {
        showTitle(getTitleManager().rightText("企业之家", resources.getString(R.string.iconSearch), View.OnClickListener {
            //跳转品牌搜索页面
            ShouMeiSearchActivity.start(activity)
        }, true, resources.getDimension(R.dimen.w56)))
        val titleList = arrayListOf<String>()
        titleList.add("关注")
        titleList.add("发现")
        fragment1 = ShoumeiFollowFragment()
        fragment2 = ShoumeiFindFragment()
        fragmentList.add(fragment1)
        fragmentList.add(fragment2)

        //设置tab的模式
        getLayoutView().tab.tabMode = TabLayout.MODE_FIXED
        //添加tab选项卡
        titleList.forEach {
            getLayoutView().tab.addTab(getLayoutView().tab.newTab().setText(it))
        }

        //给ViewPager绑定Adapter
        getLayoutView().viewPager.adapter = ShoumeiListAdapter(activity, childFragmentManager, fragmentList, titleList)

        //把TabLayout和ViewPager关联起来
        getLayoutView().tab.setupWithViewPager(getLayoutView().viewPager)

        //设置小红点
        for (i in 0 until getLayoutView().tab.tabCount) {
            val tabView = (getLayoutView().viewPager.adapter as ShoumeiListAdapter).getTabView(i)
            val imageView = tabView.findViewById(R.id.iv_tab_red) as ImageView

            imageView.visibility = View.GONE
            if (i == 0) {
                val tabText = tabView.findViewById(R.id.tv_tab_title) as TextView
                tabText.setTextColor(resources.getColor(R.color.red))
            }
            /**在这里判断每个TabLayout的内容是否有更新，来设置小红点是否显示 */
            getLayoutView().tab.getTabAt(i)?.customView = tabView
        }

        //设置tablayout的选中监听
        getLayoutView().tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val textView = customView!!.findViewById(R.id.tv_tab_title) as TextView
                textView.setTextColor(resources.getColor(R.color.blackText))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                /**在这里记录TabLayout选中后内容更新已读标记 */
                val customView = tab?.customView
                customView?.findViewById(R.id.iv_tab_red)?.visibility = View.GONE
                val textView = customView?.findViewById(R.id.tv_tab_title) as TextView
                textView.setTextColor(resources.getColor(R.color.red))
            }

        })
    }

    override fun reTryGetData() {

    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }

    override fun onStart() {
        super.onStart()
        getLayoutView().tab.let {
            it.post { ViewUtil.setIndicator(it, resources.getDimension(R.dimen.w200).toInt(), resources.getDimension(R.dimen.w200).toInt()) }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (!hidden) {
                fragmentList.let {
                        (it[getLayoutView().viewPager.currentItem] as BaseFragment).reTryGetData()
                }
            }
        }
    }

    fun selectPage(index: Int) {
        getLayoutView().viewPager.currentItem = index
    }
}