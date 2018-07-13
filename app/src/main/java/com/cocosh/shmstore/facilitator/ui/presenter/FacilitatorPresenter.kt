package com.cocosh.shmstore.facilitator.ui.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.facilitator.ui.contrat.FacilitatorContrat
import com.cocosh.shmstore.facilitator.ui.data.FacilitatorLoader

/**
 * Created by lmg on 2018/4/2.
 */
class FacilitatorPresenter(var mActivity: BaseActivity, var mView: FacilitatorContrat.IView) : FacilitatorContrat.IPresenter {
    val loader = FacilitatorLoader(mActivity, mView)
    override fun start() {

    }

    override fun getShowData(flag: Int) {
        loader.getShowData(flag)
    }
}