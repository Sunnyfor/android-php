package com.cocosh.shmstore.enterpriseCertification.ui.data

import android.text.TextUtils
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.BankShowBean
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.InviteCodeModel
import com.cocosh.shmstore.http.ApiManager
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
     * inviteCode:邀请码
     */
    fun pushData(id: Long, companyName: String, inviteCode: String) {
        var map = HashMap<String, String>()
        if (id != 0L) {
            map["id"] = id.toString()
        }
        map["companyName"] = companyName
        if (!TextUtils.isEmpty(inviteCode)) {
            map["invitationCode"] = inviteCode
        }
        ApiManager.post(mActivity, map, Constant.ENTERPRISE_INFO_REGISTER, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                (mView as EntCertificationContrat.IView).setData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取邀请码信息
     */
    fun getCodeData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, mActivity, map, Constant.ENTERPRISE_INVITE_CODE, object : ApiManager.OnResult<BaseModel<InviteCodeModel>>() {
            override fun onSuccess(data: BaseModel<InviteCodeModel>) {
                (mView as EntCertificationContrat.IView).setCodeData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<InviteCodeModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 提交对公账号信息
     */
    fun pushBankData(bankCardImg: String, bankAccountNumber: String, accountOpeningBank: String, bankAccountName: String, mobilePhoneNumber: String, bankCardType: String) {
        var map = HashMap<String, String>()
        map["bankCardImg"] = bankCardImg
        map["bankAccountNumber"] = bankAccountNumber
        map["accountOpeningBank"] = accountOpeningBank
        map["bankAccountName"] = bankAccountName
        map["mobilePhoneNumber"] = mobilePhoneNumber
        map["bankCardType"] = bankCardType
        ApiManager.post(mActivity, map, Constant.ENTERPRISE_INFO_BANKCARD, object : ApiManager.OnResult<BaseModel<EntActiveInfoModel>>() {
            override fun onSuccess(data: BaseModel<EntActiveInfoModel>) {
                (mView as EntCertificationContrat.IBankView).setData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<EntActiveInfoModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取对公账号信息
     */
    fun getBankData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, mActivity, map, Constant.ENTERPRISE_INFO_BANKCARD_SHOW, object : ApiManager.OnResult<BaseModel<BankShowBean>>() {
            override fun onSuccess(data: BaseModel<BankShowBean>) {
                (mView as EntCertificationContrat.IBankShowView).setData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<BankShowBean>) {
                LogUtil.d(data.toString())
            }
        })
    }
}