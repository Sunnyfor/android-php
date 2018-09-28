package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.baidu.ocr.sdk.model.IDCardParams
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanLicenseActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationActiveContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntCertificationActivePresenter
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_enter_prise_active.*

/**
 * 企业主激活页面
 * Created by zhangye on 2018/3/23.
 */
class EnterpriseActiveActivity : BaseActivity(), EntCertificationActiveContrat.IView {
    override fun reTryGetData() {
        presenter.getInfoData(1)
    }

    private var infoModel: EntActiveInfoModel? = null
    val presenter = EntCertificationActivePresenter(this, this)
    override fun setLayout(): Int = R.layout.activity_enter_prise_active
    override fun setInfoData(result: BaseBean<EntActiveInfoModel>) {
        infoModel = result.message
        updateView()
    }

    override fun setResultData(result: BaseBean<String>) {
        presenter.getInfoData(1)
    }

    private fun updateView() {
        if (infoModel?.cert?.status == AuthenStatus.BUSINESS_ACTIVE.type) {
//            if (infoModel?.entIdentityStatus == "0") {
//                tv_identity.text = "待完善"
//            } else {
//                tv_identity.text = "已完善"
//            }

            if (infoModel?.base?.uscc == null || infoModel?.base?.uscc == "") {
                tv_license.text = "待完善"
            } else {
                tv_license.text = "已完善"
            }

            if (infoModel?.base?.bank == null || infoModel?.base?.bank == "") {
                tv_bankcard.text = "待完善"
            } else {
                tv_bankcard.text = "已完善"
            }
        } else {
//            tv_identity.text = "查看"
            tv_bankcard.text = "查看"
            tv_license.text = "查看"
        }

        //认证信息改变UI
        when (infoModel?.cert?.status) {
            AuthenStatus.BUSINESS_ACTIVE.type -> {
                btActive.visibility = View.VISIBLE
                btn_again.visibility = View.GONE
                tv_success.visibility = View.GONE
                tv_notice.visibility = View.GONE
//                rlEnt.visibility = View.GONE
                tv_status_indicate.visibility = View.VISIBLE
                tv_status_title.visibility = View.GONE
                tv_status_desc.visibility = View.GONE
            }
            AuthenStatus.BUSINESS_EXAMINE.type -> {
                btActive.visibility = View.GONE
                btn_again.visibility = View.GONE
                tv_notice.visibility = View.GONE
                tv_success.visibility = View.GONE
//                rlEnt.visibility = View.VISIBLE
                tv_status_title.setText(R.string.ent_prise_ing)
                tv_review.text = ("审核情况会通过您提交的手机号：${infoModel?.base?.tel}\n" +
                        "短信通知您！如有疑问请拨打\n" +
                        "客服热线：400-966-1168 ")
                tv_review.visibility = View.VISIBLE
                tv_status_indicate.visibility = View.GONE
                tv_status_title.visibility = View.VISIBLE
                tv_status_desc.visibility = View.GONE
            }
            AuthenStatus.BUSINESS_OK.type -> {
                btActive.visibility = View.GONE
                btn_again.visibility = View.GONE
                tv_success.visibility = View.VISIBLE
                tv_notice.visibility = View.GONE
//                rlEnt.visibility = View.VISIBLE
                rlTitle.visibility = View.GONE
            }
            AuthenStatus.BUSINESS_FAIL.type,AuthenStatus.BUSINESS_REJECT.type -> {
                btActive.visibility = View.GONE
                tv_success.visibility = View.GONE
                btn_again.text = "修改认证信息"
                btn_again.visibility = View.VISIBLE
                tv_notice.visibility = View.VISIBLE
                if (TextUtils.isEmpty(infoModel?.cert?.reason)) {
                    tv_notice.text = ("如有疑问致电：400-966-1168")
                } else {
                    tv_notice.text = ("失败原因:\n" + infoModel?.cert?.reason + "\n" +
                            "如有疑问致电：400-966-1168")
                }
//                rlEnt.visibility = View.VISIBLE
                tv_status_title.setText(R.string.ent_error)
                tv_status_desc.text = "小红娘平台审核未通过"
                tv_status_indicate.visibility = View.GONE
                tv_status_title.visibility = View.VISIBLE
                tv_status_desc.visibility = View.VISIBLE
            }
//            AuthenStatus.REJECT.type -> {
//                btActive.visibility = View.GONE
//                btn_again.text = "重新填写"
//                tv_success.visibility = View.GONE
//                btn_again.visibility = View.VISIBLE
//                tv_notice.visibility = View.VISIBLE
//                tv_notice.text = ("请查看是否为以下情况:\n" +
//                        " ①贵公司其他员工已完成企业主认证；\n" +
//                        " ②企业资料填写有误；\n" +
//                        " ③其他原因。 \n" +
//                        "如有疑问致电首媒：400-966-1168")
//                rlEnt.visibility = View.VISIBLE
//                tv_status_title.setText(R.string.ent_error)
//                tv_status_desc.text = "该企业（${infoModel?.authReason}）已被认证且激活"
//                tv_status_indicate.visibility = View.GONE
//                tv_status_title.visibility = View.VISIBLE
//                tv_status_desc.visibility = View.VISIBLE
//            }
        }
    }

    override fun initView() {
        titleManager.defaultTitle("企业主认证激活")
        rlID.setOnClickListener(this) //身份认证
        rlBL.setOnClickListener(this) //营业执照
        rlPA.setOnClickListener(this)//对公账户
        rlEnt.setOnClickListener(this)//认证信息
        btActive.setOnClickListener(this)
        btn_again.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            rlID.id -> {
//                if (infoModel?.status == AuthenStatus.NOT_ACTIVE.type) {
//                    if (infoModel?.entIdentityStatus == "0") {
//                        //身份信息填写页
//                        AuthenticationMainActivity.start(this)
//
//                    } else {
//                        //展示数据_可以重新填写
//                        BusinessAuthenInfoActivity.start(this, -1)
//                    }
//                } else {
//                    //展示数据_不可以重新填写
//                    BusinessAuthenInfoActivity.start(this, 888)
//                }
            }
            rlBL.id -> {
                if (infoModel?.cert?.status == AuthenStatus.BUSINESS_ACTIVE.type) {
                    if (infoModel?.base?.uscc != null && infoModel?.base?.uscc != "") {
                        //展示数据_可以重新填写
                        BusinessLisenceActivity.start(this, 111, infoModel)
                    } else {
                        //营业执照填写页
                        startActivity(Intent(this, ScanLicenseActivity::class.java))
                    }
                } else {
                    //展示数据_不可以重新填写
                    BusinessLisenceActivity.start(this, -1, infoModel)
                }
            }
            rlPA.id -> {

                if (infoModel?.base?.uscc == null) {
                    ToastUtil.show("请先认证营业执照")
                    return
                }

                if (infoModel?.cert?.status == AuthenStatus.BUSINESS_ACTIVE.type) {
                    if (infoModel?.base?.bank != null && infoModel?.base?.bank != "") {
                        //展示数据_可以重新填写
                        CorporateAccountShowActivty.start(this, 111, infoModel)
                    } else {
                        //对公账户填写页
                        if (infoModel?.base?.uscc != null && infoModel?.base?.uscc != "") {
                            CorporateAccountActivty.start(this, infoModel)
                        } else {
                            showChangeDialog()
                        }
                    }
                } else {
                    //展示数据_不可以重新填写
                    CorporateAccountShowActivty.start(this, -1, infoModel)
                }
            }
//            rlEnt.comment_id -> {
//                //认证信息
//                AuthenticationAvtiveInfoActivity.start(this)
//            }

            btActive.id -> {
                //激活
//                if (infoModel?.entIdentityStatus == "0") {
//                    ToastUtil.show("请先认证身份")
//                    return
//                }
                if (infoModel?.base?.uscc == null) {
                    ToastUtil.show("请先认证营业执照")
                    return
                }


                if (infoModel?.base?.bank == null) {
                    ToastUtil.show("请先认证对公帐号")
                    return
                }

                presenter.pushData()
            }

            btn_again.id -> {
                delete()
            }
        }
    }


//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        val activeInfo = SmApplication.getApp().getData<EntActiveInfoModel>(DataCode.ACTIVE_INFO, true)
//        if (activeInfo != null) {
//            this.infoModel = activeInfo
//            //刷新UI
//            updateView()
//        }
//        clearData()
//    }


    //清理身份认证存储的数据
    private fun clearData() {
        SmApplication.getApp().removeData(DataCode.AGENT_FRONT_DATA)
        SmApplication.getApp().removeData(DataCode.AGENT_BACK_DATA)
        SmApplication.getApp().removeData(DataCode.AGENT_FRONT_URL)
        SmApplication.getApp().removeData(DataCode.AGENT_BACK_URL)
        SmApplication.getApp().removeData(IDCardParams.ID_CARD_SIDE_FRONT)
        SmApplication.getApp().removeData(IDCardParams.ID_CARD_SIDE_BACK)
        SmApplication.getApp().removeData(DataCode.FRONT_URL)
        SmApplication.getApp().removeData(DataCode.BACK_URL)
    }

    private fun showChangeDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("请先提交营业执照信息")
        dialog.show()
        dialog.OnClickListener = View.OnClickListener {
            //营业执照填写页
            startActivity(Intent(this@EnterpriseActiveActivity, ScanLicenseActivity::class.java))
        }
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, EnterpriseActiveActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getInfoData(1)
    }


    fun delete() {
        ApiManager2.post(this, hashMapOf(), Constant.ENT_CERT_DELETE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                EnterpriseCertificationActivity.start(this@EnterpriseActiveActivity)
                finish()
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<String>) {
            }
        })

    }
}