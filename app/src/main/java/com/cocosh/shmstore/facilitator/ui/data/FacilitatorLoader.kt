package com.cocosh.shmstore.facilitator.ui.data

import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.facilitator.ui.contrat.FacilitatorContrat
import com.cocosh.shmstore.facilitator.ui.model.FacilitatorInfoModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil

/**
 * 服务商接口
 * Created by lmg on 2018/4/2.
 */
class FacilitatorLoader(var mActivity: BaseActivity, var mView: IBaseView) {
    fun getShowData(flag: Int) {
        ApiManager.get(flag, mActivity, HashMap<String, String>(), Constant.FACILITOTAAR_INFO_SHOW, object : ApiManager.OnResult<BaseModel<FacilitatorInfoModel>>() {
            override fun onSuccess(data: BaseModel<FacilitatorInfoModel>) {
                if (mView is FacilitatorContrat.IView) {
                    (mView as FacilitatorContrat.IView).setShowData(data)
                } else {
                    (mView as FacilitatorContrat.ICommitView).setShowData(data)
                }
            }

            override fun onFailed(e: Throwable) {
                ToastUtil.show(e.message!!)
            }

            override fun onCatch(data: BaseModel<FacilitatorInfoModel>) {

            }
        })
    }

    fun commitData(map: HashMap<String, String>) {
        ApiManager.post(mActivity, map, Constant.FACILITOTAAR_INFO_SAVE, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                (mView as FacilitatorContrat.ICommitView).setResultData(data)
            }

            override fun onFailed(e: Throwable) {
                ToastUtil.show(e.message!!)
            }

            override fun onCatch(data: BaseModel<String>) {

            }
        })
    }
}