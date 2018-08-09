package com.cocosh.shmstore.enterpriseCertification.ui.contrat

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveShowData

/**
 * Created by lmg on 2018/3/26.
 */
interface EntCertificationActiveContrat {
    interface IView : IBaseView {
        fun setInfoData(result: BaseBean<EntActiveInfoModel>)
        fun setResultData(result: BaseBean<String>)
    }

    interface IShowView : IBaseView {
        fun setShowData(result: BaseModel<EntActiveShowData>)
    }

    interface IPresenter : IBasePresenter {
        fun getInfoData(flag: Int)
        fun pushData()
    }

    interface IShowPresenter : IBasePresenter {
        fun getShowData(flag:Int)
    }
}