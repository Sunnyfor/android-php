package com.cocosh.shmstore.facilitator.ui.contrat

import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.facilitator.ui.model.FacilitatorInfoModel

/**
 * Created by lmg on 2018/4/2.
 */
interface FacilitatorContrat {
    interface IView : IBaseView {
        fun setShowData(result: BaseModel<FacilitatorInfoModel>)
    }

    interface ICommitView : IBaseView {
        fun setResultData(result: BaseModel<String>)
        fun setShowData(result: BaseModel<FacilitatorInfoModel>)

    }

    interface IPresenter : IBasePresenter {
        fun getShowData(flag: Int)
    }

    interface ICommitPresenter : IBasePresenter {
        fun getShowData(flag:Int)
        fun commitData(map: HashMap<String, String>)
    }
}