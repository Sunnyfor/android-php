package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.BusinessAuthenInfoModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_business_autheninfo.*


/**
 * 企业主身份认证信息
 * Created by zhangye on 2018/3/29.
 */
class BusinessAuthenInfoActivity : BaseActivity() {
    override fun reTryGetData() {
        initData()
    }

    var openType = -1//888表示不可以更改 其他可以重填

    override fun setLayout(): Int = R.layout.activity_business_autheninfo

    override fun initView() {
        titleManager.defaultTitle("身份验证")
        openType = intent.getIntExtra("OPEN_TYPE", -1)
        if (openType == 888) {
            btnSure.visibility = View.GONE
        } else {
            btnSure.visibility = View.VISIBLE
        }
        btnSure.setOnClickListener(this)
        initData()
    }

    private fun initData() {
        ApiManager.get(1, this, hashMapOf(), Constant.GET_IDENTITY, object : ApiManager.OnResult<BaseModel<BusinessAuthenInfoModel>>() {
            override fun onSuccess(data: BaseModel<BusinessAuthenInfoModel>) {
                if (data.success) {
                    //代办人数据填充
                    if (data.entity?.entrepreneurAgency != null) {
                        llayoutD.visibility = View.VISIBLE
                        isvDname.setNoIconValue(data.entity?.entrepreneurAgency?.realName)
                        isvDsex.setNoIconValue(data.entity?.entrepreneurAgency?.sex)
                        isvDnation.setNoIconValue(data.entity?.entrepreneurAgency?.ethnic)
                        isvDidNumber.setNoIconValue(data.entity?.entrepreneurAgency?.idNo)
                        isvDaddress.setNoIconValue(data.entity?.entrepreneurAgency?.cardAddress)
                        isvDtime.setNoIconValue(data.entity?.entrepreneurAgency?.validityPeriodStartTime + " 至 " + data.entity?.entrepreneurAgency?.validityPeriodEndTime)
                        isvDdepartment.setNoIconValue(data.entity?.entrepreneurAgency?.issuingAgency)
                    }
                    //法人代表数据填充
                    isvFname.setNoIconValue(data.entity?.entrepreneurLegal?.realName)
                    isvFsex.setNoIconValue(data.entity?.entrepreneurLegal?.sex)
                    isvFnation.setNoIconValue(data.entity?.entrepreneurLegal?.ethnic)
                    isvFidNumber.setNoIconValue(data.entity?.entrepreneurLegal?.idNo)
                    isvFaddress.setNoIconValue(data.entity?.entrepreneurLegal?.cardAddress)
                    isvFtime.setNoIconValue(data.entity?.entrepreneurLegal?.validityPeriodStartTime + " 至 " + data.entity?.entrepreneurLegal?.validityPeriodEndTime)
                    isvFdepartment.setNoIconValue(data.entity?.entrepreneurLegal?.issuingAgency)


                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<BusinessAuthenInfoModel>) {

            }

        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                val dialo = SmediaDialog(this)
                dialo.setTitle("重新填写身份证验证")
                dialo.OnClickListener = View.OnClickListener {
                    startActivity(Intent(this, AuthenticationMainActivity::class.java))
                    finish()
                }
                dialo.show()
            }
        }
    }

    companion object {
        fun start(mContext: Context, type: Int) {
            mContext.startActivity(Intent(mContext, BusinessAuthenInfoActivity::class.java).putExtra("OPEN_TYPE", type))
        }
    }

}