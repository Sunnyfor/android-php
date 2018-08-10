package com.cocosh.shmstore.enterpriseCertification.ui.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.data.EntLicenseLoader

/**
 * Created by lmg on 2018/3/26.
 */
class EntLicensePresenter(var mActivity: BaseActivity, var mView: EntLicenseContrat.IView) : EntLicenseContrat.IPresenter {
    override fun getUpdateResult() {
        loader.updatePhoto()
    }

    override fun pushData(map: HashMap<String, String>) {
        loader.pushData(map)
    }

    val loader = EntLicenseLoader(mActivity, mView)
    override fun start() {

    }

}