package com.cocosh.shmstore.enterpriseCertification.ui.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationActiveContrat
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntCertificationActiveLoader

/**
 * Created by lmg on 2018/3/26.
 */
class EntCertificationActiveShowPresenter(var mActivity: BaseActivity, var mView: EntCertificationActiveContrat.IShowView) : EntCertificationActiveContrat.IShowPresenter {
    override fun getShowData(flag: Int) {
        loader.getShowData(flag)
    }

    val loader = EntCertificationActiveLoader(mActivity, mView)
    override fun start() {

    }

}