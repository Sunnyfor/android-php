package com.cocosh.shmstore.enterpriseCertification.ui.contrat

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.LicenseShowBean

/**
 * Created by lmg on 2018/3/26.
 */
interface EntLicenseContrat {
    interface IView : IBaseView {
        fun setResultData(result: BaseBean<String>)
        fun updatePhoto()
    }

    interface IShowView : IBaseView {
        fun setShowData(result: BaseModel<LicenseShowBean>)
    }

    interface IPresenter : IBasePresenter {
        fun pushData(map: HashMap<String, String>)
        fun getUpdateResult()
    }

    interface IShowPresenter : IBasePresenter {
        fun getShowData(flag:Int)
    }
}