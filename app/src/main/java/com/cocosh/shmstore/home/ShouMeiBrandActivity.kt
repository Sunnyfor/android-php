package com.cocosh.shmstore.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShoumeiListAdapter
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.ViewUtil
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.activity_shoumei_brand.*


/**
 * 首媒之家
 * Created by lmg on 2018/6/4.
 */
class ShouMeiBrandActivity : BaseActivity() {
    var baseId: String? = ""
    override fun setLayout(): Int = R.layout.activity_shoumei_brand

    override fun initView() {
        SmApplication.getApp().getData<SMCompanyThemeData.BBS>(DataCode.BBS, true)?.let {
            baseId = it.eid
            setData(it.logo ?: "", it.name ?: "", it.follow_nums ?: "", it.desc ?: "", it.follow
                    ?: "")

            val titleList = arrayListOf<String>()
            titleList.add("全部")
            titleList.add("精华")
            val fragmentList = arrayListOf<Fragment>()
            val fragment1 = ShoumeiBrandFragment()
            val b1 = Bundle()
            b1.putString("TYPE", "全部")
            b1.putString("EID", baseId ?: "")
            fragment1.arguments = b1
            val fragment2 = ShoumeiBrandFragment()
            val b2 = Bundle()
            b2.putString("TYPE", "精华")
            b2.putString("EID", baseId ?: "")
            fragment2.arguments = b2
            fragmentList.add(fragment1)
            fragmentList.add(fragment2)

            //设置tab的模式
            tabs.tabMode = TabLayout.MODE_FIXED
            //添加tab选项卡s
            titleList.forEach {
                tabs.addTab(tabs.newTab().setText(it))
            }
            //给ViewPager绑定Adapter
            view_pager.adapter = ShoumeiListAdapter(this, supportFragmentManager, fragmentList, titleList)
            //把TabLayout和ViewPager关联起来
            tabs.setupWithViewPager(view_pager)
            initListener()
        }

    }

    override fun onListener(view: View) {
    }

    private fun initListener() {
        appbar.let {
            it.addOnOffsetChangedListener { _, verticalOffset ->
                if (Math.abs(verticalOffset) >= it.totalScrollRange) {

                } else {

                }
            }
        }
        tvStatus.setOnClickListener {
            if (status == "0") {
                cancelOrConfirm(baseId ?: "", "1")
                return@setOnClickListener
            }
            cancelOrConfirm(baseId ?: "", "0")
        }

    }

    override fun reTryGetData() {

    }

    override fun onStart() {
        super.onStart()
        tabs.let {
            it.post { ViewUtil.setIndicator(it, resources.getDimension(R.dimen.w70).toInt(), resources.getDimension(R.dimen.w70).toInt()) }
        }
    }

    companion object {
        fun start(mContext: Context, bbs: SMCompanyThemeData.BBS) {
            SmApplication.getApp().setData(DataCode.BBS, bbs)
            mContext.startActivity(Intent(mContext, ShouMeiBrandActivity::class.java))
        }
    }

    var status: String? = ""
    fun setData(url: String, name: String, number: String, content: String, status: String) {
        GlideUtils.loadRound(1, this, url, ivLogo)
        titleManager.defaultTitle(name)
        tvName.text = name
        tvNumber.text = ("关注人数：$number 人")
        desc.text = content
        this.status = status
        if (status == "0") {
            tvStatus.text = "+关注"
            tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_red)
        } else {
            tvStatus.text = "已关注"
            tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
        }
        desc.limitTextViewString(desc.text.toString(), 60, desc, View.OnClickListener {
            //设置监听函数
        })
    }

    private fun cancelOrConfirm(idCompanyHomeBaseInfo: String, isFollow: String) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["eid"] = idCompanyHomeBaseInfo
        params["op"] = if(isFollow == "1") "follow" else "cancel" //动作类型 (必填,'cancel'-取消关注,'follow'-关注)
        ApiManager2.post(this, params, Constant.EHOME_FOLLOW_OPERATE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<String>) {
                isShowLoading = false
                    if (isFollow == "0") {
                        tvStatus.text = "+关注"
                        status = "0"
                        tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_red)
                    } else {
                        tvStatus.text = "已关注"
                        status = "1"
                        tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
                    }
                    ObserverManager.getInstance().notifyObserver(3, baseId ?: "", isFollow, "")
            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }

}