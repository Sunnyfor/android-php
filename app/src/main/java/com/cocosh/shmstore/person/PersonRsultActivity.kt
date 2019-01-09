package com.cocosh.shmstore.person

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.person.model.PersonResult
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.utils.UserManager2
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

        UserManager2.getLogin()?.let {
            isv_code.setNoIconValue(it.code)
            isv_account.setNoIconValue(it.phone)
        }

        initData()
    }

    private fun initData() {
        ApiManager2.get(1, this, hashMapOf(), Constant.CERT_RESULT, object : ApiManager2.OnResult<BaseBean<PersonResult>>() {

            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<PersonResult>) {
                data.message?.let {
                    isv_time.setNoIconValue(it.cert.time)
                    isv_name.setNoIconValue(it.base.name)
                    isv_sex.setNoIconValue(if (it.base.gender == 0) "女" else "男")
                    isv_idcard.setNoIconValue(it.base.idno)
                    isv_address.setNoIconValue(it.base.addr)
                    isv_validityPeriodTime.setNoIconValue(it.base.beg_time + "-" + it.base.end_time)
                    isv_agency.setNoIconValue(it.base.org)
                }
            }


            override fun onCatch(data: BaseBean<PersonResult>) {

            }
        })
    }

    override fun onListener(view: View) {

    }


}