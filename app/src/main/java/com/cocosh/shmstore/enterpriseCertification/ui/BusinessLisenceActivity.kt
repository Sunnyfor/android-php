package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.baiduScan.ScanLicenseActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.LicenseShowBean
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntLicenseShowPresenter
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_business_lisence.*

/**
 *
 * Created by lmg on 2018/3/28.
 */
class BusinessLisenceActivity : BaseActivity(), EntLicenseContrat.IShowView {
    override fun reTryGetData() {
        presenter.getShowData(1)
    }

    var presenter = EntLicenseShowPresenter(this, this)
    private var openType = -1

    override fun setLayout(): Int = R.layout.activity_business_lisence
    override fun setShowData(result: BaseModel<LicenseShowBean>) {
        if (result.success && result.code == 200) {
            initData(result.entity!!)
        } else {
            ToastUtil.show("网络故障")
        }
    }

    private fun initData(data: LicenseShowBean) {
        tv_name.text = data.corpFname
        tv_code.text = data.corpTax
        tv_layer_name.text = data.legalRepresentative
        tv_useful_time.text = data.bizValidityPeriod
        tv_address.text = data.domicile
        tv_money.text = data.registeredCapital
        tv_create_time.text = data.foundingTime
        tv_type.text = data.registeredType
    }

    override fun initView() {
        titleManager.defaultTitle("营业执照")
        updateView()
        presenter.getShowData(1)
        btnChange.setOnClickListener(this)
    }

    private fun updateView() {
//        openType = intent.getIntExtra(AuthenStatus.ENT_OPEN_TYPE.type, -1)
        if (openType == 111) {
            //未激活_可以重新填写
            btnChange.visibility = View.VISIBLE
        } else {
            //激活，激活失败，审核
            btnChange.visibility = View.GONE
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnChange.id -> {
                //弹窗
                showChangeDialog()
            }
        }
    }

    companion object {
        fun start(context: Context, entOpenType: Int) {
//            context.startActivity(Intent(context, BusinessLisenceActivity::class.java).putExtra(AuthenStatus.ENT_OPEN_TYPE.type, entOpenType))
        }
    }

    private fun showChangeDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("重新填写营业执照")
        dialog.show()
        dialog.OnClickListener = View.OnClickListener {
            //营业执照填写页
            startActivity(Intent(this@BusinessLisenceActivity, ScanLicenseActivity::class.java))
        }
    }
}