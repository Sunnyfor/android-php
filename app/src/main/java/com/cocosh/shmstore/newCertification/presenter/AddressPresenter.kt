package com.cocosh.shmstore.newCertification.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newCertification.contrat.AddressContrat
import com.cocosh.shmstore.newCertification.data.AddressLoader

/**
 * Created by cjl on 2018/2/6.
 */
class AddressPresenter(activity: BaseActivity, addressView: AddressContrat.IView) : AddressContrat.IPresenter {
    override fun requestFacilitatorAddress(flag: Int, code: Int) {
        loader.requestFacilitatorAddress(flag, code)
    }

    private val loader = AddressLoader(activity, addressView)

    override fun commitAddress(code: String) {
        loader.commitAddress(code)
    }

    override fun requestAddress(flag: Int, code: Int) {
        loader.requestAddress(flag, code)
    }

    override fun start() {
    }
}