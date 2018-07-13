package com.cocosh.shmstore.mine.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.data.MineLoader

/**
 * Created by lmg on 2018/3/26.
 */
class CollectionPresenter(var mActivity: BaseActivity, var mView: MineContrat.ICollectionView) : MineContrat.ICollectionPresenter {
    override fun requestCollectionData(flag: Int, currentPage: String, showCount: String, timeStamp: String) {
        loader.requestCollectionData(flag, currentPage, showCount, timeStamp)
    }

    val loader = MineLoader(mActivity, mView)
    override fun start() {

    }
}