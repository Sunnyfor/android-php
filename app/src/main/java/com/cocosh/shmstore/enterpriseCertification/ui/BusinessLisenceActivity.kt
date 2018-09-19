package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanLicenseActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.LicenseShowBean
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntLicenseShowPresenter
import com.cocosh.shmstore.utils.DataCode
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

    }

    private fun initData(data: LicenseShowBean) {


    }

    override fun initView() {
        titleManager.defaultTitle("营业执照")
        updateView()
//        presenter.getShowData(1)
        btnChange.setOnClickListener(this)

        SmApplication.getApp().getData<EntActiveInfoModel>(DataCode.ENT_AUTHER_DATA, true)?.let {
            tv_name.text = it.base.name
            tv_code.text = it.base.uscc
            tv_layer_name.text = it.base.legal
            val useTime = StringBuilder()
            if (it.base.beg_time.isNotEmpty()) {
                useTime.append(it.base.beg_time)
            }

            if (it.base.beg_time.isNotEmpty() && it.base.end_time.isNotEmpty()) {
                useTime.append("-")
            }

            if (it.base.end_time.isNotEmpty()) {
                useTime.append(it.base.end_time)
            }

            tv_useful_time.text = (useTime.toString())
            tv_address.text = it.base.addr
            tv_money.text = it.base.capital
            tv_create_time.text = it.base.found
            tv_type.text = it.base.kind
        }

    }

    private fun updateView() {
        openType = intent.getIntExtra("type", -1)
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
        fun start(context: Context, entOpenType: Int, infoModel: EntActiveInfoModel?) {
            SmApplication.getApp().setData(DataCode.ENT_AUTHER_DATA, infoModel)
            context.startActivity(Intent(context, BusinessLisenceActivity::class.java).putExtra("type", entOpenType))
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