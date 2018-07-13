package com.cocosh.shmstore.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShoumeiListAdapter
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.ViewUtil
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.activity_shoumei_brand.*


/**
 * Created by lmg on 2018/6/4.
 */
class ShouMeiBrandActivity : BaseActivity() {
    var baseId: String? = ""
    override fun setLayout(): Int = R.layout.activity_shoumei_brand

    override fun initView() {
        titleManager.defaultTitle("品牌论坛")
        val titleList = arrayListOf<String>()
        titleList.add("全部")
        titleList.add("精华")
        baseId = intent.getStringExtra("baseId")
        var fragmentList = arrayListOf<Fragment>()
        var fragment1 = ShoumeiBrandFragment()
        var b1 = Bundle()
        b1.putString("TYPE", "全部")
        fragment1.arguments = b1
        var fragment2 = ShoumeiBrandFragment()
        var b2 = Bundle()
        b2.putString("TYPE", "精华")
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

    override fun onListener(view: View) {
    }

    private fun initListener() {
        appbar.let {
            it.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
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
        fun start(mContext: Context, id: String) {
            mContext.startActivity(Intent(mContext, ShouMeiBrandActivity::class.java).putExtra("baseId", id))
        }
    }

    var status: String? = ""
    fun setData(url: String, name: String, number: String, content: String, status: String) {
        GlideUtils.loadRound(1, this, url, ivLogo)
//        titleManager.defaultTitle(name)
        tvName.text = name
        tvNumber.text = "关注人数：$number 人"
        desc.setText(content)
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
        params["idCompanyHomeBaseInfo"] = idCompanyHomeBaseInfo
        params["isFollow"] = isFollow
        ApiManager.post(this, params, Constant.SM_FOLLOW_OR_CANCEL, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                isShowLoading = false
                if (data.success && data.code == 200) {
                    if (isFollow == "0") {
                        tvStatus.text = "+关注"
                        tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_red)
                    } else {
                        tvStatus.text = "已关注"
                        tvStatus.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
                    }
                    ObserverManager.getInstance().notifyObserver(3, baseId ?: "", isFollow, "")
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }

}