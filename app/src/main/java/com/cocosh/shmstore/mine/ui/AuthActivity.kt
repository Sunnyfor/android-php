package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.EnterpriseActiveActivity
import com.cocosh.shmstore.enterpriseCertification.ui.EnterpriseCertificationActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorFailActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorInformationActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorSplashActivity
import com.cocosh.shmstore.facilitator.ui.PayFranchiseFeeActivity
import com.cocosh.shmstore.home.model.CommentData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.model.MemberEntrance
import com.cocosh.shmstore.model.CommonData
import com.cocosh.shmstore.newCertification.ui.NewCertificationActivity
import com.cocosh.shmstore.newCertification.ui.PartherPendingPayActivity
import com.cocosh.shmstore.newCertification.ui.PartnerSplashActivity
import com.cocosh.shmstore.person.PersonRsultActivity
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_author.*
import kotlinx.android.synthetic.main.layout_top_head.view.*
import java.util.*

/**
 *
 * Created by cjl on 2018/2/1.
 */
class AuthActivity : BaseActivity() {
    override fun reTryGetData() {
        getState()
    }

    var newMatchmakerStatus = 0  //新媒人状态
    var personStatus = 0 //个人状态
    var serverDealerStatus = 0 //服务商状态
    var entStatus = 0
    override fun setLayout(): Int = R.layout.activity_author

    override fun initView() {
        titleManager.immersionTitle()
        titleManager.defaultTitle("", "", 0, R.color.white, null)
        SmApplication.getApp().getData<String>(DataCode.SET_PWD_KEY, true)//删除设置密码实人认证的情况
        top_new_title.ivHead.setOnClickListener(this)
        llPerson.setOnClickListener(this)
        llPartner.setOnClickListener(this)
        llMedia.setOnClickListener(this)
        llCompany.setOnClickListener(this)
        top_new_title.tvNo.text = (getString(R.string.no) + UserManager2.getLogin()?.code)

    }

    override fun onResume() {
        super.onResume()

        val memberEntrance = UserManager2.getMemberEntrance()
        memberEntrance?.let {
            top_new_title.tvName.text = it.nickname
            if (it.avatar != "") {
                UserManager.loadBg(it.avatar, top_new_title.ivBg) //加载背景图
                GlideUtils.loadHead(this, it.avatar, top_new_title.ivHead)
            }
        }
        getState()
    }

    //加载认证状态
    private fun getState() {
        UserManager2.loadCommonData(1, this, object : ApiManager2.OnResult<BaseBean<CommonData>>() {
            override fun onSuccess(data: BaseBean<CommonData>) {
                data.message?.let {
                    UserManager2.setCommonData(it)

                    personStatus = it.cert.r //个人认证
                    when (personStatus) {
                        AuthenStatus.PERSION_NO.type -> {
                            tvPersonState.text = "未认证"
                            ivPerson.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.shenfen))
                        }
                        AuthenStatus.PERSION_OK.type -> {
                            tvPersonState.text = "已认证"
                            ivPerson.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.shenfen_chenggong))
                        }
                    }


                    newMatchmakerStatus = it.cert.x //新媒人认证
                    when (newMatchmakerStatus) {
                        AuthenStatus.NEW_MATCHMAKER_NO.type -> {
                            tvPartnerState.text = "未认证"
                            ivPartner.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.xinmeiren))
                        }
                        AuthenStatus.NEW_MATCHMAKER_OK.type -> {
                            tvPartnerState.text = "已认证"
                            ivPartner.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.xinmeiren_chenggong))
                        }
                        AuthenStatus.NEW_MATCHMAKER_WAIT.type -> {
                            tvPartnerState.text = "待付款"
                            ivPartner.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.xinmeiren))
                        }
                    }

                    serverDealerStatus = it.cert.f //服务商认证
                    when (serverDealerStatus) {
                        AuthenStatus.SERVER_DEALER_NO.type -> {
                            tvMediaState.text = "未认证"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                        AuthenStatus.SERVER_DEALER_OK.type -> {
                            tvMediaState.text = "已认证"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang_ok))
                        }
//                        AuthenStatus.SERVER_DEALER_ING.type -> {
//                            tvMediaState.text = "待付款"
//                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
//                        }
                        AuthenStatus.SERVER_DEALER_ING.type -> {
                            tvMediaState.text = "待付款"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                        AuthenStatus.SERVER_DEALER_FAIL.type -> {
                            tvMediaState.text = "认证失败"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                    }

                    entStatus = it.cert.b
                    when (entStatus) {
                        AuthenStatus.BUSINESS_ACTIVE.type -> {
                            tvCompanyState.text = "待激活"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_no))
                        }
                        AuthenStatus.BUSINESS_EXAMINE.type -> {
                            tvCompanyState.text = "审核中"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_no))
                        }
                        AuthenStatus.BUSINESS_OK.type -> {
                            tvCompanyState.text = "已激活"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_ok))
                        }
                        AuthenStatus.BUSINESS_FAIL.type -> {
                            tvCompanyState.text = "激活失败"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_no))
                        }
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<CommonData>) {
            }

        })
    }

    //
    override fun onListener(view: View) {
        when (view.id) {
            R.id.llPerson -> llpersion()
            R.id.llPartner -> llparnter()
            R.id.llMedia -> llmedia()
            R.id.llCompany -> llcompany()
            top_new_title.ivHead.id -> startActivity(Intent(this, ArchiveActivity::class.java))
        }
    }

    /**
     * 个人认证
     */
    private fun llpersion() {
        if (personStatus == AuthenStatus.PERSION_OK.type) {
            startActivity(Intent(this, PersonRsultActivity::class.java))
        }

        if (personStatus == AuthenStatus.PERSION_NO.type) {
            val it = Intent(this, ScanIdCardActivity::class.java)
            it.putExtra("type", "实人认证")
            startActivity(it)
        }
    }

    /**
     * 服务商认证
     * 认证状态(1-未认证 2-待付款 3-已注销 4-已作废 5-已认证 6 认证失败)
     */
    private fun llmedia() {
        when (serverDealerStatus) {
            AuthenStatus.SERVER_DEALER_NO.type -> FacilitatorSplashActivity.start(this)
            AuthenStatus.SERVER_DEALER_ING.type -> PayFranchiseFeeActivity.start(this, 666)
            AuthenStatus.SERVER_DEALER_FAIL.type -> FacilitatorFailActivity.start(this, -1)
            AuthenStatus.SERVER_DEALER_OK.type -> FacilitatorInformationActivity.start(this, -1)
        }
    }


    /**
     * 企业主认证
     */
    private fun llcompany() {
        if (entStatus == AuthenStatus.BUSINESS_NO.type) {
            startActivity(Intent(this, EnterpriseCertificationActivity::class.java))
        } else {
            EnterpriseActiveActivity.start(this)
        }
    }


    /**
     * 新媒人认证
     */
    private fun llparnter() {
        if (personStatus == AuthenStatus.PERSION_OK.type) {
            when (newMatchmakerStatus) {
                AuthenStatus.NEW_MATCHMAKER_NO.type -> PartnerSplashActivity.start(this)
                AuthenStatus.NEW_MATCHMAKER_WAIT.type -> startActivity(Intent(this, PartherPendingPayActivity::class.java))
                AuthenStatus.NEW_MATCHMAKER_OK.type -> NewCertificationActivity.start(this)
            }
        } else {
            val dialog = SmediaDialog(this)
            dialog.setTitle("请先完成实人认证")
            dialog.setDesc("确保实人认证信息与新媒人认证信息一致")

            dialog.OnClickListener = View.OnClickListener {
                val intent = Intent(this, ScanIdCardActivity::class.java)
                intent.putExtra("type", "实人认证")
                startActivity(intent)
            }
            dialog.show()
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
       intent?.getStringExtra("type")?.let {
           if (it == "x"){
               startActivity(Intent(this, PartherPendingPayActivity::class.java))
           }
       }
    }

}