package com.cocosh.shmstore.person

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.person.model.PersonResult
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import kotlinx.android.synthetic.main.activity_person_result.*


/**
 * 个人认证页面
 * Created by zhangye on 2018/4/2.
 */
class PersonRsultActivity : BaseActivity() {
    override fun reTryGetData() {
        initData()
    }

    override fun setLayout(): Int = R.layout.activity_person_result

    override fun initView() {
        titleManager.defaultTitle("实人认证")
        LogUtil.i("token:" + UserManager.getUserToken())

        initData()
    }

    private fun initData() {
        ApiManager.get(1, this, hashMapOf(), Constant.IDCARDDETAIL, object : ApiManager.OnResult<BaseModel<PersonResult>>() {
            override fun onSuccess(data: BaseModel<PersonResult>) {
                if (data.success) {
                    data.entity?.let {
                        isv_time.setNoIconValue(it.applyTime)
                        isv_code.setNoIconValue(it.smCode)
                        isv_account.setNoIconValue(it.userName)
                        isv_name.setNoIconValue(it.realName)
                        isv_sex.setNoIconValue(it.sex)
                        isv_idcard.setNoIconValue(it.idNo)
                        isv_address.setNoIconValue(it.cardAddress)
                        isv_validityPeriodTime.setNoIconValue(it.validityPeriodStartTime + "-" + it.validityPeriodEndTime)
                        isv_agency.setNoIconValue(it.issuingAgency)
                    }
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<PersonResult>) {

            }
        })
    }

    override fun onListener(view: View) {

    }


}