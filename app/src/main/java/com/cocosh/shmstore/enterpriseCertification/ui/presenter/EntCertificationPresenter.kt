package com.cocosh.shmstore.enterpriseCertification.ui.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationContrat
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntCertificationLoader

/**
 * Created by lmg on 2018/3/26.
 */
class EntCertificationPresenter(var mActivity: BaseActivity, var mView: EntCertificationContrat.IView) : EntCertificationContrat.IPresenter {
    override fun getCodeData(flag: Int) {
        loader.getCodeData(flag)
    }

    val loader = EntCertificationLoader(mActivity, mView)
    override fun start() {

    }

    override fun pushData( companyName: String, inviteCode: String) {
        loader.pushData(companyName, inviteCode)
    }
}