package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class AccountStatusPresenter(var mActivity: BaseActivity, var mView: MineContrat.IAccountStatusView) : MineContrat.IAccountStatusPresenter {
    override fun requestAccountStatusData() {
        loader.requestAccountStatus()
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}