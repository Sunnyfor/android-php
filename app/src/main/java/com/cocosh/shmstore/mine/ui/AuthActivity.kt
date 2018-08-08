package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.EnterpriseActiveActivity
import com.cocosh.shmstore.enterpriseCertification.ui.EnterpriseCertificationActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorFailActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorInformationActivity
import com.cocosh.shmstore.facilitator.ui.FacilitatorSplashActivity
import com.cocosh.shmstore.facilitator.ui.PayFranchiseFeeActivity
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.model.MemberEntrance
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
        getState(1)
    }

    var partnerStatus = ""
    var personStatus = ""
    var cityOpertorsStatus = ""
    var entStatus = ""
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

    var flag = 1
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

//        getState(flag)
    }

    private fun getState(flag: Int) {
        val map = HashMap<String, String>()
        //UNCERTIFIED("未认证"),PRE_DRAFT("待付款"), PRE_PASS("已认证")
        ApiManager.get(flag, this, map, Constant.CHECK_AUTH, object : ApiManager.OnResult<BaseModel<MemberEntrance>>() {
            override fun onSuccess(data: BaseModel<MemberEntrance>) {
                data.entity?.let {
                    UserManager.setMemberEntrance(it)
                    partnerStatus = it.partnerStatus ?: AuthenStatus.UNCERTIFIED.type  //新媒人认证
                    when (partnerStatus) {
                        AuthenStatus.UNCERTIFIED.type -> {
                            tvPartnerState.text = "未认证"
                            ivPartner.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.xinmeiren))
                        }
                        AuthenStatus.PRE_PASS.type -> {
                            tvPartnerState.text = "已认证"
                            ivPartner.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.xinmeiren_chenggong))
                        }
                        AuthenStatus.PRE_DRAFT.type -> {
                            tvPartnerState.text = "待付款"
                            ivPartner.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.xinmeiren))
                        }
                        AuthenStatus.PRE_AUTH.type -> {
                            tvPartnerState.text = "认证中"
                            ivPartner.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.xinmeiren))
                        }
                    }

                    personStatus = it.personStatus ?: AuthenStatus.UNCERTIFIED.type  //个人认证
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

                    cityOpertorsStatus = it.cityOpertorsStatus ?: AuthenStatus.UNCERTIFIED.type//服务商认证
                    when (cityOpertorsStatus) {
                        AuthenStatus.UNCERTIFIED.type -> {
                            tvMediaState.text = "未认证"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                        AuthenStatus.PRE_PASS.type -> {
                            tvMediaState.text = "已认证"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang_ok))
                        }
                        AuthenStatus.PRE_DRAFT.type -> {
                            tvMediaState.text = "待付款"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                        AuthenStatus.PRE_AUTH.type -> {
                            tvMediaState.text = "认证中"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                        AuthenStatus.AUTH_FAILED.type -> {
                            tvMediaState.text = "认证失败"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                        AuthenStatus.REJECT.type -> {
                            tvMediaState.text = "认证失败"
                            ivMedia.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.fuwushang))
                        }
                    }
                    entStatus = it.entStatus ?: AuthenStatus.NOT_ACTIVE.type //企业主
                    when (entStatus) {
                        AuthenStatus.NOT_ACTIVE.type -> {
                            tvCompanyState.text = "待激活"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_no))
                        }
                        AuthenStatus.AUDIT.type -> {
                            tvCompanyState.text = "审核中"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_no))
                        }
                        AuthenStatus.ALREADY_ACTIVATED.type -> {
                            tvCompanyState.text = "已激活"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_ok))
                        }
                        AuthenStatus.AUTH_FAILED.type, AuthenStatus.REJECT.type -> {
                            tvCompanyState.text = "激活失败"
                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_no))
                        }
//                        AuthenStatus.REJECT.type -> {
//                            tvCompanyState.text = "审核驳回"
//                            ivCompany.setImageDrawable(ContextCompat.getDrawable(this@AuthActivity, R.drawable.enterprise_no))
//                        }
                    }
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<MemberEntrance>) {
            }

        })
        this.flag = 0
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

        personStatus = AuthenStatus.PERSION_NO.type

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
        //UNCERTIFIED("未认证"),PRE_DRAFT("认证中"), PRE_PASS("已认证")  PRE_AUTH:认证中
        cityOpertorsStatus = AuthenStatus.UNCERTIFIED.type
        when (cityOpertorsStatus) {
            AuthenStatus.UNCERTIFIED.type -> FacilitatorSplashActivity.start(this)
            AuthenStatus.PRE_DRAFT.type -> PayFranchiseFeeActivity.start(this, 666)
            AuthenStatus.PRE_PASS.type -> FacilitatorInformationActivity.start(this, -1)
//            AuthenStatus.PRE_AUTH.type -> PayFranchiseFeeActivity.start(this, 666)
            AuthenStatus.AUTH_FAILED.type -> FacilitatorFailActivity.start(this, -1)
//            AuthenStatus.REJECT.type -> FacilitatorFailActivity.start(this, -1)
        }
    }


    /**
     * 企业主认证
     */
    private fun llcompany() {
        if (entStatus == AuthenStatus.UNCERTIFIED.type) {
            startActivity(Intent(this, EnterpriseCertificationActivity::class.java))
        } else {
            EnterpriseActiveActivity.start(this)
        }
    }


    /**
     * 新媒人认证
     */
    private fun llparnter() {
        partnerStatus = AuthenStatus.UNCERTIFIED.type
//        if (personStatus == AuthenStatus.PERSION_OK.type) {
            //UNCERTIFIED("未认证"),PRE_DRAFT("待付款"), PRE_PASS("已认证")
            when (partnerStatus) {
                AuthenStatus.UNCERTIFIED.type -> PartnerSplashActivity.start(this)
                AuthenStatus.PRE_DRAFT.type -> startActivity(Intent(this, PartherPendingPayActivity::class.java))
                AuthenStatus.PRE_PASS.type -> NewCertificationActivity.start(this)
                AuthenStatus.PRE_AUTH.type -> startActivity(Intent(this, PartherPendingPayActivity::class.java))
            }
//        } else {
//            val dialog = SmediaDialog(this)
//            dialog.setTitle("请先完成实人认证")
//            dialog.setDesc("确保实人认证信息与新媒人认证信息一致")
//
//            dialog.OnClickListener = View.OnClickListener {
//                val intent = Intent(this, ScanIdCardActivity::class.java)
//                intent.putExtra("type", "实人认证")
//                startActivity(intent)
//            }
//            dialog.show()
//        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }

    //更新状态
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getState(flag)
    }
}