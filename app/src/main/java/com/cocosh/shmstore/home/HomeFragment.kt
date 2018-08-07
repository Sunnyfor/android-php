package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.Banner
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.title.HomeTitleFragment
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 *
 */
class HomeFragment : BaseFragment() {

    private var homeTitleFragment: HomeTitleFragment? = null

    override fun setLayout(): Int = R.layout.fragment_home

    override fun reTryGetData() {

    }

    override fun initView() {
        homeTitleFragment = getTitleManager().homeTitle() as HomeTitleFragment
        homeTitleFragment?.let {
            it.homeFragment = this
            showTitle(it)
        }

        getLayoutView().layoutOne.setOnClickListener(this)
        getLayoutView().layoutTwo.setOnClickListener(this)
        getLayoutView().layoutThree.setOnClickListener(this)
        getLayoutView().homeBottomView.setFragmentManager(childFragmentManager)
        loadBanner()
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.layoutOne -> {
                startBonus("大众红包")
            }

            R.id.layoutTwo -> {
                startBonus("精准红包")
//                startBonus("媒体扶贫")

            }

            R.id.layoutThree -> {
//                startBonus("媒体扶贫")
                startBonus("粉丝红包")
//                startBonus("消费扶贫")
            }
        }
    }

    override fun close() {
    }


    /**
     * 认证引导弹窗
     */
//    private fun autEnt() {
//        val info = UserManager.getLogin()?.resResIndexInfo
//        if (info != null) {
//            val mDialog = CertificationDialog(activity)
//            if (info.authStatus == "H") {
//                //新媒人
//                mDialog.setDesc("您接受了<br>${info.realName}<br>发来的<font color='#D8253B'>新媒人认证</font>邀请")
//            } else {
//                //服务商
//                mDialog.setDesc("您接受了<br>${info.corpFname}<br>发来的<font color='#D8253B'>企业主认证</font>邀请")
//            }
//            mDialog.show()
//
//            mDialog.OnClickListener = View.OnClickListener {
//                if (info.authStatus == "H") {
//                    //新媒人
//                    PartnerSplashActivity.start(activity)
//                } else {
//                    //服务商
//                    EnterpriseCertificationActivity.start(activity)
//                }
//            }
//        }
//    }


    //跳转红包页面
    private fun startBonus(title: String) {
        val it = Intent(context, BonusListActivity::class.java)
        it.putExtra("title", title)
        startActivity(it)
    }

    fun loadBanner() {
        val params = hashMapOf<String, String>()
        ApiManager.get(0, activity as BaseActivity, params, Constant.HOME_BANNER, object : ApiManager.OnResult<BaseModel<Banner>>() {
            override fun onSuccess(data: BaseModel<Banner>) {
                if (data.success) {
                    data.entity?.let {
                        getLayoutView().homeAdView.loadData(it.resHomePageBannersVoList?: arrayListOf())
                    }
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<Banner>) {
            }

        })
    }

    override fun onPause() {
        super.onPause()
        getLayoutView().homeAdView.stopLoop()
    }

    override fun onStart() {
        super.onStart()
        getLayoutView().homeAdView.startLooep()
        SmApplication.getApp().removeData(DataCode.BONUS)
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        homeTitleFragment?.onHiddenChanged(hidden)
        if (!hidden) {
            loadBanner()
            SmApplication.getApp().getData<Location>(DataCode.LOCATION, false)?.let {
                getLayoutView().homeBottomView.loadData(it)
            }
        }
    }
}
