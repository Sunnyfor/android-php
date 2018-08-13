package com.cocosh.shmstore.facilitator.ui.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.facilitator.ui.contrat.FacilitatorContrat
import com.cocosh.shmstore.facilitator.ui.model.FacilitatorInfoModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil

/**
 * 服务商接口
 * Created by lmg on 2018/4/2.
 */
class FacilitatorLoader(var mActivity: BaseActivity, var mView: IBaseView) {
    fun getShowData(flag: Int) {
        ApiManager2.get(flag, mActivity, HashMap<String, String>(), Constant.FACILITOTAAR_INFO_SHOW, object : ApiManager2.OnResult<BaseBean<EntActiveInfoModel>>() {

            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<EntActiveInfoModel>) {
                if (mView is FacilitatorContrat.IView) {
                    (mView as FacilitatorContrat.IView).setShowData(data)
                } else {
                    (mView as FacilitatorContrat.ICommitView).setShowData(data)
                }
            }


            override fun onCatch(data: BaseBean<EntActiveInfoModel>) {

            }
        })
    }

    fun commitData(map: HashMap<String, String>) {
        ApiManager2.post(mActivity, map, Constant.SVC_CERT_DO, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<String>) {
                (mView as FacilitatorContrat.ICommitView).setResultData(data)
            }


            override fun onCatch(data: BaseBean<String>) {

            }
        })
    }
}