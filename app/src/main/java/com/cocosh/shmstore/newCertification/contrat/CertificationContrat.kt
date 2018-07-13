package com.cocosh.shmstore.newCertification.contrat

import com.baidu.ocr.sdk.model.IDCardResult
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView

/**
 * Created by cjl on 2018/2/5.
 */
interface CertificationContrat {
    interface IView:IBaseView{
        fun idCardResult(idCardSide:String,result: IDCardResult?)
        fun tokenResult(token: String)
    }
    interface IPresenter : IBasePresenter {
        fun recIDCard(idCardSide: String, filePath: String)
        fun tokenRequest()
    }
}