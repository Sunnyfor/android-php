package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.enterpriseCertification.ui.EnterpriseCertificationActivity
import com.cocosh.shmstore.home.model.Bonus2
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.model.CommonData
import com.cocosh.shmstore.model.Location
import com.cocosh.shmstore.title.HomeTitleFragment
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.CertificationDialog
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
        autEnt()
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
    private fun autEnt() {
        UserManager2.getLogin()?.invitee?.let {
            if (it.code != null) {

                UserManager2.loadCommonData(0, getBaseActivity(), object : ApiManager2.OnResult<BaseBean<CommonData>>() {
                    override fun onSuccess(data: BaseBean<CommonData>) {
                        UserManager2.setCommonData(data.message)
                        val mDialog = CertificationDialog(activity)
                        val type = it.type

                        if (type == "x") {
                            if (data.message?.cert?.x == AuthenStatus.NEW_MATCHMAKER_OK.type) {
                                return
                            }
                            //新媒人
                            mDialog.setDesc("您接受了<br>${it.inviter}<br>发来的<font color='#D8253B'>新媒人认证</font>邀请")
                        } else {
                            if (data.message?.cert?.b == AuthenStatus.BUSINESS_OK.type) {
                                return
                            }
                            //服务商
                            mDialog.setDesc("您接受了<br>${it.inviter}<br>发来的<font color='#D8253B'>企业主认证</font>邀请")
                        }

                        mDialog.OnClickListener = View.OnClickListener {
                            if (type == "x") {
                                //新媒人
                                startActivity(Intent(context, AuthActivity::class.java).putExtra("type", "NEW_MATCHMAKER"))
                            } else {
                                //服务商
                                startActivity(Intent(context, AuthActivity::class.java).putExtra("type", "BUSINESS"))
                            }
                        }
                        mDialog.show()
                    }

                    override fun onFailed(code: String, message: String) {
                    }

                    override fun onCatch(data: BaseBean<CommonData>) {
                    }

                })

            }

        }
    }


    //跳转红包页面
    private fun startBonus(title: String) {
        val it = Intent(context, BonusListActivity::class.java)
        it.putExtra("title", title)
        startActivity(it)
    }

    fun loadBanner() {
        val params = hashMapOf<String, String>()
        ApiManager2.get(0, activity as BaseActivity, params, Constant.RP_HOME, object : ApiManager2.OnResult<BaseBean<ArrayList<Bonus2>>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<ArrayList<Bonus2>>) {
                data.message?.let {
                    getLayoutView().homeAdView.loadData(it)
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<Bonus2>>) {
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
