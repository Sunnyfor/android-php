package com.cocosh.shmstore.enterpriseCertification.ui.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationActiveContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveShowData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.LogUtil

/**
 *认证信息
 * Created by lmg on 2018/3/26.
 */
class EntCertificationActiveLoader(private var mActivity: BaseActivity, var mView: IBaseView) {
    /**
     * 获取概要信息
     */
    fun getInfoData(flag: Int) {
        val map = HashMap<String, String>()
        ApiManager2.get(flag, mActivity, map, Constant.ENT_CERT_RESULT, object : ApiManager2.OnResult<BaseBean<EntActiveInfoModel>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<EntActiveInfoModel>) {
                (mView as EntCertificationActiveContrat.IView).setInfoData(data)
            }



            override fun onCatch(data: BaseBean<EntActiveInfoModel>) {

            }
        })
    }

    /**
     * 激活信息
     */
    fun pushData() {
        val map = HashMap<String, String>()
        ApiManager2.post(0, mActivity, map, Constant.ENT_CERT_ACTIVE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (mView as EntCertificationActiveContrat.IView).setResultData(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取概要信息
     */
    fun getShowData(flag: Int) {
        val map = HashMap<String, String>()
        ApiManager.get(flag, mActivity, map, Constant.ENTERPRISE_ACTIVE_INFO, object : ApiManager.OnResult<BaseModel<EntActiveShowData>>() {
            override fun onSuccess(data: BaseModel<EntActiveShowData>) {
                (mView as EntCertificationActiveContrat.IShowView).setShowData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<EntActiveShowData>) {
                LogUtil.d(data.toString())
            }
        })
    }
}