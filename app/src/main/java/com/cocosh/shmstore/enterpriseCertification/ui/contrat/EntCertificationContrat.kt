package com.cocosh.shmstore.enterpriseCertification.ui.contrat

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.model.BankShowBean
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.InviteCodeModel

/**
 *
 * Created by lmg on 2018/3/26.
 */
interface EntCertificationContrat {
    interface IView : IBaseView {
        fun setData(result: BaseBean<String>)
        fun setCodeData(result: BaseBean<InviteCodeModel>)
    }

    interface IPresenter : IBasePresenter {
        fun pushData(companyName: String, inviteCode: String)
        fun getCodeData(flag: Int)
    }

    interface IBankView : IBaseView {
        fun setData(data: BaseBean<String>)
    }

    interface IBankPresenter : IBasePresenter {
        fun pushData(bank: String, account: String, tel: String, linker: String)
    }

    interface IBankShowView : IBaseView {
        fun setData(data: BaseBean<BankShowBean>)
    }

    interface IBankShowPresenter : IBasePresenter {
        fun getData(flag:Int)
    }


}