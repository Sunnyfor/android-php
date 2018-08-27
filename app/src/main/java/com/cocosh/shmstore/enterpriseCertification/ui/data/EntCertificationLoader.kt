package com.cocosh.shmstore.enterpriseCertification.ui.data

import android.text.TextUtils
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.BankShowBean
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.InviteCodeModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.LogUtil

/**
 * Created by lmg on 2018/3/26.
 */
class EntCertificationLoader(var mActivity: BaseActivity, var mView: IBaseView) {
    /**
     * 提交企业主信息
     * id：企业id
     * companyName:公司名
     * invite_code:邀请码
     */
    fun pushData(companyName: String, inviteCode: String) {
        val map = HashMap<String, String>()
        map["company"] = companyName
        if (!TextUtils.isEmpty(inviteCode)) {
            map["invitee_code"] = inviteCode
        }
        ApiManager2.post(mActivity, map, Constant.ENT_CERT_DO, object : ApiManager2.OnResult<BaseBean<String>>() {

            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (mView as EntCertificationContrat.IView).setData(data)
            }

            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取邀请码信息
     */
    fun getCodeData(flag: Int) {
        val map = HashMap<String, String>()
        ApiManager2.get(flag, mActivity, map, Constant.ENT_CERT_INVITEE, object : ApiManager2.OnResult<BaseBean<InviteCodeModel>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<InviteCodeModel>) {
                (mView as EntCertificationContrat.IView).setCodeData(data)
            }

            override fun onCatch(data: BaseBean<InviteCodeModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 提交对公账号信息
     */
    fun pushBankData(bank: String, account: String, tel: String, linker: String) {
        val map = HashMap<String, String>()
        map["bank"] = bank
        map["account"] = account
        map["tel"] = tel
        map["linker"] = linker
        ApiManager2.post(mActivity, map, Constant.ENT_CERT_ACCT, object : ApiManager2.OnResult<BaseBean<String>>() {

            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (mView as EntCertificationContrat.IBankView).setData(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取对公账号信息
     */
    fun getBankData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, mActivity, map, Constant.ENTERPRISE_INFO_BANKCARD_SHOW, object : ApiManager.OnResult<BaseBean<BankShowBean>>() {
            override fun onSuccess(data: BaseBean<BankShowBean>) {
                (mView as EntCertificationContrat.IBankShowView).setData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseBean<BankShowBean>) {
                LogUtil.d(data.toString())
            }
        })
    }
}