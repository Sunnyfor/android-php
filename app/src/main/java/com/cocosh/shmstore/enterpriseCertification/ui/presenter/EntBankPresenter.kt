package com.cocosh.shmstore.enterpriseCertification.ui.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationActiveContrat
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationContrat
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntCertificationActiveLoader
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntCertificationLoader
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntLicenseLoader

/**
 * Created by lmg on 2018/3/26.
 */
class EntBankPresenter(var mActivity: BaseActivity, var mView: EntCertificationContrat.IBankView) : EntCertificationContrat.IBankPresenter {
    override fun pushData(bankCardImg: String, bankAccountNumber: String, accountOpeningBank: String, bankAccountName: String, mobilePhoneNumber: String, bankCardType: String) {
        loader.pushBankData(bankCardImg, bankAccountNumber, accountOpeningBank, bankAccountName, mobilePhoneNumber, bankCardType)
    }

    val loader = EntCertificationLoader(mActivity, mView)
    override fun start() {

    }

}