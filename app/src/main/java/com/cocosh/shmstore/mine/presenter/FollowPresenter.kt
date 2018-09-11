package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class FollowPresenter(var mActivity: BaseActivity, var mView: MineContrat.IFollowView) : MineContrat.IFollowPresenter {
    override fun requestFollowData(flag: Int, currentPage: String, showCount: String) {
        loader.requestFollowData(flag, currentPage, showCount)
    }

    override fun requestCancelFollowData(idCompanyInfoBase: String) {
        loader.requestCancelFollowData(idCompanyInfoBase)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}