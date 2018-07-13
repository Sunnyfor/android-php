package com.cocosh.shmstore.enterpriseCertification.ui.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationActiveContrat
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntCertificationActiveLoader

/**
 * Created by lmg on 2018/3/26.
 */
class EntCertificationActivePresenter(var mActivity: BaseActivity, var mView: EntCertificationActiveContrat.IView) : EntCertificationActiveContrat.IPresenter {
    override fun pushData() {
        loader.pushData()
    }

    override fun getInfoData(flag: Int) {
        loader.getInfoData(flag)
    }

    val loader = EntCertificationActiveLoader(mActivity, mView)
    override fun start() {

    }

}