package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.facilitator.ui.FacilitatorFailActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorSplashActivity
import com.cocosh.shmstore.facilitator.ui.PayFranchiseFeeActivity
import com.cocosh.shmstore.home.model.UserInCome
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.ui.ArchiveActivity
import com.cocosh.shmstore.mine.ui.WebActivity
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.mine.ui.authentication.IncomeActivity
import com.cocosh.shmstore.mine.ui.mywallet.RedAccountActivity
import com.cocosh.shmstore.newCertification.ui.PartherPendingPayActivity
import com.cocosh.shmstore.newCertification.ui.PartnerSplashActivity
import com.cocosh.shmstore.utils.OpenType
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.fragment_income.*
import kotlinx.android.synthetic.main.fragment_income.view.*


/**
 * 收益页面
 */
class IncomeFragment : BaseFragment() {
    private var progress = 0
    var cerUrl: String? = null
    var facUrl: String? = null
    override fun setLayout(): Int = R.layout.fragment_income

    override fun initView() {
        showTitle(getTitleManager().textTitle("收益"))
        progress = UserManager2.getMemberEntrance()?.degree?:0
        if (progress < 1) {
            getLayoutView().toSetInfo.visibility = View.VISIBLE
        } else {
            getLayoutView().toSetInfo.visibility = View.GONE
            getLayoutView().rightIcon.visibility = View.GONE
        }
        getLayoutView().toSetInfo.setOnClickListener(this)
        getLayoutView().btnRed.setOnClickListener {
            RedAccountActivity.start(activity)
        }

        getLayoutView().cerRl.setOnClickListener(this)
        getLayoutView().facRl.setOnClickListener(this)

        getLayoutView().whatCer.setOnClickListener(this)
        getLayoutView().tipCer.setOnClickListener(this)

        getLayoutView().whatFac.setOnClickListener(this)
        getLayoutView().tipFac.setOnClickListener(this)

        requestData(0)
    }

    override fun reTryGetData() {
        requestData(0)
    }

    override fun onListener(view: View) {
        when (view.id) {
            getLayoutView().toSetInfo.id -> {
                startActivity(Intent(activity, ArchiveActivity::class.java))
            }
            getLayoutView().cerRl.id -> {

                UserManager2.getCommonData()?.cert?.let {
                    if (UserManager2.getCommonData()?.cert?.r == AuthenStatus.PERSION_OK.type) {
                        //UNCERTIFIED("未认证"),PRE_DRAFT("待付款"), PRE_PASS("已认证")
                        when (it.x) {
                            AuthenStatus.NEW_MATCHMAKER_NO.type -> PartnerSplashActivity.start(activity)
                            AuthenStatus.NEW_MATCHMAKER_OK.type -> IncomeActivity.start(activity, CommonType.CERTIFICATION_INCOME.type)
                            AuthenStatus.NEW_MATCHMAKER_WAIT.type -> startActivity(Intent(activity, PartherPendingPayActivity::class.java))
                        }
                    } else {
                        val dialog = SmediaDialog(activity)
                        dialog.setTitle("请先完成实人认证")
                        dialog.setDesc("确保实人认证信息与新媒人认证信息一致")
                        dialog.OnClickListener = View.OnClickListener {
                            val intent = Intent(activity, ScanIdCardActivity::class.java)
                            intent.putExtra("type", "实人认证")
                            startActivity(intent)
                        }
                        dialog.show()
                    }
                }
            }
            getLayoutView().facRl.id -> {
                when ( UserManager2.getCommonData()?.cert?.f) {
                    AuthenStatus.SERVER_DEALER_NO.type -> FacilitatorSplashActivity.start(activity)
                    AuthenStatus.SERVER_DEALER_ING.type -> PayFranchiseFeeActivity.start(activity, 666)
                    AuthenStatus.SERVER_DEALER_OK.type -> IncomeActivity.start(activity, CommonType.FACILITATOR_INCOME.type)
                    AuthenStatus.SERVER_DEALER_FAIL.type -> FacilitatorFailActivity.start(activity, -1)
                }
            }
            getLayoutView().whatFac.id -> {
                WebActivity.start(activity, OpenType.Fac.name, facUrl, UserManager2.getCommonData()?.cert?.f.toString())
            }

            getLayoutView().tipFac.id -> {
                WebActivity.start(activity, OpenType.Fac.name, facUrl, UserManager2.getCommonData()?.cert?.f.toString())
            }
            getLayoutView().whatCer.id -> {
                WebActivity.start(activity, OpenType.Cer.name, cerUrl, UserManager2.getCommonData()?.cert?.x.toString())
            }
            getLayoutView().tipCer.id -> {
                WebActivity.start(activity, OpenType.Cer.name, cerUrl, UserManager2.getCommonData()?.cert?.x.toString())
            }
        }
    }

    override fun close() {

    }

    /**
     * 获取我的钱包主页信息
     */
    private fun requestData(flag: Int) {

        ApiManager2.post(flag, activity as BaseActivity, hashMapOf(), Constant.INCOME, object : ApiManager2.OnResult<BaseBean<UserInCome>>() {
            override fun onFailed(code: String, message: String) {
                showReTryLayout()
            }

            override fun onCatch(data: BaseBean<UserInCome>) {
            }

            override fun onSuccess(data: BaseBean<UserInCome>) {
                hideReTryLayout()
                data.message?.let {

                    getLayoutView().redMony.text = ((it.rp?:"0.00") + "元")
                    getLayoutView().cerMoney.text = ((it.x?:"0.00") + "元")
                    getLayoutView().facMoney.text = ((it.f?:"0.00") + "元")

                    facUrl = it.f_link

                    cerUrl = it.x_link

                    if (it.cert_f == AuthenStatus.SERVER_DEALER_OK.type.toString()) {
                        getLayoutView().facDesc.visibility = View.INVISIBLE
                        getLayoutView().facShow.text = "查看详情"
                    } else {
                        getLayoutView().facDesc.visibility = View.VISIBLE
                        getLayoutView().facShow.text = "前往认证"
                    }

                    if (it.cert_x == AuthenStatus.NEW_MATCHMAKER_OK.type.toString()) {
                        getLayoutView().cerDesc.visibility = View.INVISIBLE
                        getLayoutView().cerShow.text = "查看详情"
                    } else {
                        getLayoutView().cerDesc.visibility = View.VISIBLE
                        getLayoutView().cerShow.text = "前往认证"
                    }

                    if (it.cert_x == AuthenStatus.NEW_MATCHMAKER_OK.type.toString() && it.cert_f == AuthenStatus.SERVER_DEALER_OK.type.toString()) {
                        cerLl.visibility = View.GONE
                    } else {
                        cerLl.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            requestData(0)
        }
    }

    override fun onResume() {
        super.onResume()
        progress = UserManager2.getMemberEntrance()?.degree?:0
        if (progress < 1) {
            getLayoutView().toSetInfo.visibility = View.VISIBLE
        } else {
            getLayoutView().toSetInfo.visibility = View.GONE
            getLayoutView().rightIcon.visibility = View.GONE
        }
    }
}
