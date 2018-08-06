package com.cocosh.shmstore.home

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.MessageListAdapter
import com.cocosh.shmstore.home.model.MsgUnReadCount
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ViewUtil
import kotlinx.android.synthetic.main.fragment_message.view.*


/**
 * 消息
 */
class MessageFragment : BaseFragment() {
    var fragmentList = arrayListOf<Fragment>()
    override fun setLayout(): Int = R.layout.fragment_message

    override fun initView() {
        showTitle(getTitleManager().textTitle("消息"))
        val titleList = arrayListOf<String>()
        titleList.add("系统消息")
        titleList.add("红包消息")
//        titleList.add("订单消息")
        titleList.add("回复我的")



        fragmentList.add(MessageSystemFragment())
        fragmentList.add(MessageRedFragment())
//        fragmentList.add(MessageOrderFragment())
        fragmentList.add(MessageReplyFragment())

        //设置tab的模式
        getLayoutView().tab.tabMode = TabLayout.MODE_FIXED
        //添加tab选项卡
        titleList.forEachIndexed { index, s ->
            getLayoutView().tab.addTab(getLayoutView().tab.newTab().setText(titleList[index]))
        }

        //给ViewPager绑定Adapter
        getLayoutView().viewPager.adapter = MessageListAdapter(activity, childFragmentManager, fragmentList, titleList)

        //把TabLayout和ViewPager关联起来
        getLayoutView().tab.setupWithViewPager(getLayoutView().viewPager)

        //设置小红点
        for (i in 0 until getLayoutView().tab.tabCount) {
            val tabView = (getLayoutView().viewPager.adapter as MessageListAdapter).getTabView(i)
            val imageView = tabView.findViewById(R.id.iv_tab_red) as ImageView
            val textView = tabView?.findViewById(R.id.tv_tab_title) as TextView
            /**在这里判断每个TabLayout的内容是否有更新，来设置小红点是否显示 */
            getLayoutView().tab.getTabAt(i)?.customView = tabView
            imageView.visibility = View.GONE
            if (i == 0) {
                textView.setTextColor(resources.getColor(R.color.red))
            }
        }

        //设置tablayout的选中监听
        getLayoutView().tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val textView = customView?.findViewById(R.id.tv_tab_title) as TextView
                textView.setTextColor(resources.getColor(R.color.grayText))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                getMsgCount()
                /**在这里记录TabLayoupt选中后内容更新已读标记 */
                val customView = tab?.customView
                customView?.findViewById(R.id.iv_tab_red)?.visibility = View.INVISIBLE
                val textView = customView?.findViewById(R.id.tv_tab_title) as TextView
                textView.setTextColor(resources.getColor(R.color.red))
            }

        })
        getBaseActivity().showLoading()
        getMsgCount()
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
            it.post { ViewUtil.setIndicator(it, resources.getDimension(R.dimen.w10).toInt(), resources.getDimension(R.dimen.w10).toInt()) }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getMsgCount()
            fragmentList.let {
                if (it[getLayoutView().viewPager.currentItem] != null) {
                    (it[getLayoutView().viewPager.currentItem] as BaseFragment).reTryGetData()
                }
            }
        }
    }

    fun getMsgCount() {
        val map = HashMap<String, String>()
        ApiManager.get(0, activity as BaseActivity, map, Constant.MSG_UNREAD, object : ApiManager.OnResult<BaseModel<MsgUnReadCount>>() {
            override fun onSuccess(data: BaseModel<MsgUnReadCount>) {
                if (data.success && data.code == 200) {
                    if (data.entity?.unreadRedPacketMessageCount == 0 && data.entity?.unreadReplyMessageCount == 0 && data.entity?.unreadSystemMessageCount == 0) {
                        //隐藏
                        (activity as HomeActivity).isShowMsgPoint(false)
                    } else {
                        //显示
                        (activity as HomeActivity).isShowMsgPoint(true)
                    }

                    //更改消息按钮
                    changeRedPoint(0, data.entity?.unreadSystemMessageCount == 0)
                    changeRedPoint(1, data.entity?.unreadRedPacketMessageCount == 0)
                    changeRedPoint(2, data.entity?.unreadReplyMessageCount == 0)
                } else {
//                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<MsgUnReadCount>) {

            }
        })
    }

    fun changeRedPoint(index: Int, boolean: Boolean) {
        //隐藏
        val customView = getLayoutView().tab.getTabAt(index)?.customView
        val imageView = customView?.findViewById(R.id.iv_tab_red)
        if (!boolean) {
            imageView?.visibility = View.VISIBLE
        } else {
            imageView?.visibility = View.INVISIBLE
        }

    }
}
