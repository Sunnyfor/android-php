package com.cocosh.shmstore.enterpriseCertification.ui.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.LicenseShowBean
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.LogUtil

/**
 *
 * Created by lmg on 2018/3/26.
 */
class EntLicenseLoader(var mActivity: BaseActivity, var mView: IBaseView) {
    /**
     * 获取概要信息
     */
    fun pushData(map: HashMap<String, String>) {
        ApiManager2.post(mActivity, map, Constant.ENT_CERT_LICENCE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<String>) {
                (mView as EntLicenseContrat.IView).setResultData(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    //获取七牛token
    fun updatePhoto() {
        (mView as EntLicenseContrat.IView).updatePhoto()
    }

    /**
     * 获取营业执照回显信息
     */
    fun getShowData(flag: Int) {
        ApiManager.get(flag, mActivity, HashMap(), Constant.ENTERPRISE_INFO_LICENSE_SHOW, object : ApiManager.OnResult<BaseModel<LicenseShowBean>>() {
            override fun onSuccess(data: BaseModel<LicenseShowBean>) {
                (mView as EntLicenseContrat.IShowView).setShowData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<LicenseShowBean>) {
                LogUtil.d(data.toString())
            }
        })
    }
}