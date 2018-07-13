package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class IsSetPwdPresenter(var mActivity: BaseActivity, var mView: MineContrat.IIsSetPwdView) : MineContrat.IIsSetPwdPresenter {
    override fun requestIsSetPwdData(flag: Int) {
        loader.requestIsSetPwdData(flag)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }

}