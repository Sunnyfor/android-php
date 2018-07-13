package com.cocosh.shmstore.facilitator.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.facilitator.ui.contrat.FacilitatorContrat
import com.cocosh.shmstore.facilitator.ui.model.FacilitatorInfoModel
import com.cocosh.shmstore.facilitator.ui.presenter.FacilitatorCommitPresenter
import com.cocosh.shmstore.home.HomeActivity
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.PermissionCode
import com.cocosh.shmstore.utils.PermissionUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_pay_franchise_fee.*


/**
 * Created by lmg on 2018/3/30.
 */
class PayFranchiseFeeActivity : BaseActivity(), FacilitatorContrat.ICommitView {
    override fun reTryGetData() {
        presenter.getShowData(1)
    }

    val presenter = FacilitatorCommitPresenter(this, this)
    var map: HashMap<String, String>? = null
    var openType = -1 //666 代表认证中 其他未认证
    override fun setShowData(result: BaseModel<FacilitatorInfoModel>) {
        if (result.success && result.code == 200) {
            tv_fee.text = "￥" + result.entity?.money
        } else {
            ToastUtil.show(result.message)
        }
    }


    override fun setResultData(result: BaseModel<String>) {
        if (result.success && result.code == 200) {
            startActivity(Intent(this, AuthActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun initView() {
        titleManager.facTitle().setRightOnClickListener(View.OnClickListener {
            showDialog()
        })
        permissionUtil = PermissionUtil(this)
        openType = intent.getIntExtra("OPEN_TYPE", -1)
        if (openType == 666) {
            //认证中
            ll_input.visibility = View.GONE
            ll_show.visibility = View.VISIBLE
            presenter.getShowData(1)
        } else {
            map = SmApplication.getApp().getData<HashMap<String, String>>(DataCode.FACILITATOR_KEY_MAP, false)
            tv_fee.text = "￥" + map!!["money"]
        }
        btn_show.setOnClickListener(this)
        btn_input.setOnClickListener(this)
        btn_loading.setOnClickListener(this)
    }

    override fun setLayout(): Int = R.layout.activity_pay_franchise_fee

    override fun onListener(view: View) {
        when (view.id) {
            btn_show.id -> {
                //跳转 A
                startActivity(Intent(this@PayFranchiseFeeActivity, FacilitatorInformationActivity::class.java).putExtra("OPEN_TYPE", 555))
            }
            btn_input.id -> {
                //跳转 状态主页
                var paramMap = HashMap<String, String>()
                paramMap["areaCode"] = map!!["areaCode"].toString()
                paramMap["areaName"] = map!!["areaName"].toString()
                paramMap["companyBankCard"] = map!!["companyBankCard"].toString()
                paramMap["companyBankName"] = map!!["companyBankName"].toString()
                paramMap["companyLegal"] = map!!["legalRepresentative"].toString()
                paramMap["companyName"] = map!!["corpFname"].toString()
                paramMap["companyPhone"] = map!!["companyPhone"].toString()
                paramMap["licenceImg"] = map!!["licenceImg"].toString()
                paramMap["licenseAddress"] = map!!["domicile"].toString()
                paramMap["licenseBegTime"] = map!!["startTime"].toString()
                paramMap["licenseEndTime"] = map!!["endTime"].toString()
                paramMap["licenseLegalName"] = map!!["legalRepresentative"].toString()
                paramMap["licenseName"] = map!!["corpFname"].toString()
                paramMap["licenseNo"] = map!!["corpTax"].toString()
                paramMap["licenseStartTime"] = map!!["foundingTime"].toString()
                paramMap["licenseType"] = map!!["registeredType"].toString()
                paramMap["licenseWealth"] = map!!["registeredCapital"].toString()
                paramMap["money"] = map!!["money"].toString()
                presenter.commitData(paramMap)
            }
            btn_loading.id -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    companion object {
        fun start(mContext: Context, openType: Int) {
            val intent = Intent(mContext, PayFranchiseFeeActivity::class.java)
            intent.putExtra("OPEN_TYPE", openType)
            mContext.startActivity(intent)
        }
    }

    private lateinit var permissionUtil: PermissionUtil
    private fun showDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("首媒客服电话")
        dialog.setDesc("400-966-1168")
        dialog.setPositiveText("呼叫")
        dialog.OnClickListener = View.OnClickListener {
            if (permissionUtil.callPermission()) {
                callPhone()
            }
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCode.PHONE.type) {
            if (permissionUtil.checkPermission(permissions)) {
                callPhone()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:400-966-1168"))
        startActivity(intent)
    }

}