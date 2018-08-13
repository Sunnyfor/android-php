package com.cocosh.shmstore.newCertification.contrat

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.newCertification.model.AddressServiceModel
import com.cocosh.shmstore.newCertification.model.ApplyPartner

/**
 *
 * Created by cjl on 2018/2/6.
 */
interface AddressContrat {
    interface IView : IBaseView {
        fun addressResult(prov:Int,result: BaseBean<ArrayList<AddressServiceModel>>?)
        fun commitAddressResult(result: BaseBean<ApplyPartner>)
        fun addressFacResult(prov: Int,result: BaseBean<ArrayList<AddressServiceModel>>)
    }

    interface IPresenter : IBasePresenter {
        fun requestAddress(flag: Int, code: Int)
        fun commitAddress(code: String)
        fun requestFacilitatorAddress(flag: Int, code: Int)
    }
}