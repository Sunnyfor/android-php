package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.facilitator.ui.FacilitatorFailActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorSplashActivity
import com.cocosh.shmstore.facilitator.ui.PayFranchiseFeeActivity
import com.cocosh.shmstore.home.model.UserInCome
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.ui.ArchiveActivity
import com.cocosh.shmstore.mine.ui.WebActivity
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.mine.ui.authentication.IncomeActivity
import com.cocosh.shmstore.mine.ui.mywallet.RedAccountActivity
import com.cocosh.shmstore.newCertification.ui.PartherPendingPayActivity
import com.cocosh.shmstore.newCertification.ui.PartnerSplashActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.fragment_income.*
import kotlinx.android.synthetic.main.fragment_income.view.*


/**
 * 收益页面
 */
class IncomeFragment : BaseFragment() {
    var progress: String? = null
    var cerValue: String? = null
    var facValue: String? = null
    var cerUrl: String? = null
    var facUrl: String? = null
    override fun setLayout(): Int = R.layout.fragment_income

    override fun initView() {
        showTitle(getTitleManager().textTitle("收益"))
        progress = UserManager.getArchivalCompletion()
        if (progress?.toDouble() ?: 0.00 < 1) {
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
                if (UserManager.getMemberEntrance()?.personStatus == AuthenStatus.PERSION_OK.type) {
                    //UNCERTIFIED("未认证"),PRE_DRAFT("待付款"), PRE_PASS("已认证")
                    when (cerValue) {
                        AuthenStatus.UNCERTIFIED.type -> PartnerSplashActivity.start(activity)
                        AuthenStatus.PRE_DRAFT.type -> startActivity(Intent(activity, PartherPendingPayActivity::class.java))
                        AuthenStatus.PRE_PASS.type -> IncomeActivity.start(activity, CommonType.CERTIFICATION_INCOME.type)
                        AuthenStatus.PRE_AUTH.type -> startActivity(Intent(activity, PartherPendingPayActivity::class.java))
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
            getLayoutView().facRl.id -> {
                when (facValue) {
                    AuthenStatus.UNCERTIFIED.type -> FacilitatorSplashActivity.start(activity)
                    AuthenStatus.PRE_DRAFT.type -> PayFranchiseFeeActivity.start(activity, 666)
                    AuthenStatus.PRE_PASS.type -> IncomeActivity.start(activity, CommonType.FACILITATOR_INCOME.type)
                    AuthenStatus.AUTH_FAILED.type -> FacilitatorFailActivity.start(activity, -1)
                }
            }
            getLayoutView().whatFac.id -> {
                WebActivity.start(activity, OpenType.Fac.name, facUrl, facValue)
            }

            getLayoutView().tipFac.id -> {
                WebActivity.start(activity, OpenType.Fac.name, facUrl, facValue)
            }
            getLayoutView().whatCer.id -> {
                WebActivity.start(activity, OpenType.Cer.name, cerUrl, cerValue)
            }
            getLayoutView().tipCer.id -> {
                WebActivity.start(activity, OpenType.Cer.name, cerUrl, cerValue)
            }
        }
    }

    override fun close() {

    }

    /**
     * 获取我的钱包主页信息
     */
    fun requestData(flag: Int) {
        if (!NetworkUtils.isNetworkAvaliable(activity)) {
            ToastUtil.show("网络连接错误")
//            baseActivity.showError(0)
            showReTryLayout()
            return
        }

        var map = HashMap<String, String>()
        ApiManager.get(flag, activity as BaseActivity, map, Constant.USER_INCOME, object : ApiManager.OnResult<BaseModel<UserInCome>>() {
            override fun onSuccess(data: BaseModel<UserInCome>) {
                if (data.success && data.code == 200) {
                    hideReTryLayout()
                    getLayoutView().redMony.text = (data.entity?.redPacketMoney ?: "0") + "元"
                    getLayoutView().cerMoney.text = (data.entity?.partnerMoney ?: "0") + "元"
                    getLayoutView().facMoney.text = (data.entity?.cityOperatorsMoney ?: "0") + "元"
                    facValue = data.entity?.cityOperatorsStatu
                    cerValue = data.entity?.partnerState

                    facUrl = data.entity?.cityOperatorsStatementUrl
                    cerUrl = data.entity?.partnerStatementUrl


                    if (facValue == "5") {
                        getLayoutView().facDesc.visibility = View.INVISIBLE
                        getLayoutView().facShow.text = "查看详情"
                    } else {
                        getLayoutView().facDesc.visibility = View.VISIBLE
                        getLayoutView().facShow.text = "前往认证"
                    }

                    if (cerValue == "5") {
                        getLayoutView().cerDesc.visibility = View.INVISIBLE
                        getLayoutView().cerShow.text = "查看详情"
                    } else {
                        getLayoutView().cerDesc.visibility = View.VISIBLE
                        getLayoutView().cerShow.text = "前往认证"
                    }

                    if (facValue == "5" && cerValue == "5") {
                        cerLl.visibility = View.GONE
                    } else {
                        cerLl.visibility = View.VISIBLE
                    }
                } else {
                    ToastUtil.show(data.message)
                    showReTryLayout()
                }
            }

            override fun onCatch(data: BaseModel<UserInCome>) {
                LogUtil.d(data.toString())
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
                showReTryLayout()
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
        progress = UserManager.getArchivalCompletion()
        if (progress?.toDouble() ?: 0.00 < 1) {
            getLayoutView().toSetInfo.visibility = View.VISIBLE
        } else {
            getLayoutView().toSetInfo.visibility = View.GONE
            getLayoutView().rightIcon.visibility = View.GONE
        }
    }
}
