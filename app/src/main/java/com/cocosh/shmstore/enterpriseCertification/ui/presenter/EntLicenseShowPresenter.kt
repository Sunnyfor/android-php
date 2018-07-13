package com.cocosh.shmstore.enterpriseCertification.ui.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntLicenseLoader

/**
 * Created by lmg on 2018/3/26.
 */
class EntLicenseShowPresenter(var mActivity: BaseActivity, var mView: EntLicenseContrat.IShowView) : EntLicenseContrat.IShowPresenter {
    override fun getShowData(flag:Int) {
        loader.getShowData(flag)
    }

    val loader = EntLicenseLoader(mActivity, mView)
    override fun start() {

    }

}