package com.cocosh.shmstore.facilitator.ui.contrat

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel

/**
 * Created by lmg on 2018/4/2.
 */
interface FacilitatorContrat {
    interface IView : IBaseView {
        fun setShowData(result: BaseBean<EntActiveInfoModel>)
    }

    interface ICommitView : IBaseView {
        fun setResultData(result: BaseBean<String>)
        fun setShowData(result: BaseBean<EntActiveInfoModel>)
    }

    interface IPresenter : IBasePresenter {
        fun getShowData(flag: Int)
    }

    interface ICommitPresenter : IBasePresenter {
        fun getShowData(flag:Int)
        fun commitData(map: HashMap<String, String>)
    }
}