package com.cocosh.shmstore.newCertification.presenter

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newCertification.contrat.CertificationContrat
import com.cocosh.shmstore.newCertification.data.CertifitionLoader

/**
 * Created by cjl on 2018/2/5.
 */
class CertificationPresenter(activity: BaseActivity, loginView: CertificationContrat.IView): CertificationContrat.IPresenter {
    override fun tokenRequest() {
        certifitionLoader.tokenRequest()
    }

    private var certifitionLoader= CertifitionLoader(activity, loginView)
    override fun start() {
    }

    override fun recIDCard(idCardSide: String, filePath: String) {
                 certifitionLoader.recIDCard(idCardSide, filePath)
    }
}