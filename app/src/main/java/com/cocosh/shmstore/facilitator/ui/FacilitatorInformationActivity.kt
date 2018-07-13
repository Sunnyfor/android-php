package com.cocosh.shmstore.facilitator.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.facilitator.ui.contrat.FacilitatorContrat
import com.cocosh.shmstore.facilitator.ui.model.FacilitatorInfoModel
import com.cocosh.shmstore.facilitator.ui.presenter.FacilitatorPresenter
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_facilitator_info.*

/**
 *
 * Created by lmg on 2018/4/2.
 */
class FacilitatorInformationActivity : BaseActivity(), FacilitatorContrat.IView {
    override fun reTryGetData() {
        presenter.getShowData(1)
    }

    var openType = -1 //555  是预览页  其他正常显示页
    val presenter = FacilitatorPresenter(this, this)
    override fun setShowData(result: BaseModel<FacilitatorInfoModel>) {
        if (result.success && result.code == 200) {
            updateView(result.entity!!)
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun setLayout(): Int = R.layout.activity_facilitator_info

    override fun initView() {
        titleManager.defaultTitle("服务商认证")
        openType = intent.getIntExtra("OPEN_TYPE", -1)
        if (openType == 555) {
            llBtn.visibility = View.VISIBLE
            btnSure.setOnClickListener(this)
            isvStatus.visibility = View.GONE
            isvTime.visibility = View.GONE
            //展示数据，可重新更改
            val map = SmApplication.getApp().getData<HashMap<String, String>>(DataCode.FACILITATOR_KEY_MAP, false)
            val mFacilitatorInfoModel = FacilitatorInfoModel(map!!["areaCode"].toString(),
                    map["areaName"].toString(),
                    "",
                    map["companyBankCard"].toString(),
                    map["companyBankName"].toString(),
                    map["legalRepresentative"].toString(),
                    map["corpFname"].toString(),
                    map["companyPhone"].toString(),
                    "",
                    "",
                    map["licenceImg"].toString(),
                    map["domicile"].toString(),
                    map["startTime"].toString(),
                    map["endTime"].toString(),
                    map["legalRepresentative"].toString(),
                    map["corpFname"].toString(),
                    map["corpTax"].toString(),
                    map["foundingTime"].toString(),
                    map["registeredType"].toString(),
                    map["registeredCapital"].toString(),
                    map["money"].toString(),
                    "",
                    "",
                    "",
                    "",
                    ""
            )
            updateView(mFacilitatorInfoModel)
        } else {
            presenter.getShowData(1)
            llBtn.visibility = View.GONE
            isvStatus.visibility = View.VISIBLE
            isvTime.visibility = View.VISIBLE
        }

        btnCancel.setOnClickListener(this)
    }

    private fun updateView(data: FacilitatorInfoModel) {
        //认证信息
        when (data.status) {
            AuthenStatus.UNCERTIFIED.type -> {

            }
            AuthenStatus.PRE_PASS.type -> {
                isvStatus.setNoIconValue("已认证")
            }
            AuthenStatus.PRE_DRAFT.type -> {

            }
            AuthenStatus.PRE_AUTH.type -> {

            }
        }
        isvTime.setNoIconValue(data.authTime)
        isvShouMeiNumber.setNoIconValue(UserManager.getLogin()?.smCode) //首媒号
        isvEntAccount.setNoIconValue(UserManager.getPhone()) //认证账号
        isvEntArea.setNoIconValue(data.areaName) //认证地区
        isvEntMoney.setNoIconValue(data.money + "元") //认证费用
        //基础信息
        isvFname.setNoIconValue(data.licenseName) //名称
        isvFNumber.setNoIconValue(data.licenseNo)//统一社会信用代码
        isvFLayerName.setNoIconValue(data.licenseLegalName) //法定代表人
        if (data.licenseEndTime.isNullOrEmpty()) {
            isvFTime.setNoIconValue(data.licenseBegTime) //营业期限
        } else {
            isvFTime.setNoIconValue(data.licenseBegTime + "至" + data.licenseEndTime) //营业期限
        }
        isvFType.setNoIconValue(data.licenseType)//类型
        isvFaddress.setNoIconValue(data.licenseAddress)//住所
        isvFMoney.setNoIconValue(data.licenseWealth)//注册资本
        isvFStartTime.setNoIconValue(data.licenseStartTime)//成立时间
        isvImg.setNoIconValue("已提交") //营业执照
        isvAccount.setNoIconValue(data.companyBankCard) //对公账号
        isvBankName.setNoIconValue(data.companyBankName) //银行名称
        bank.setNoIconValue(data.companyPhone) //手机号
    }

    override fun onListener(view: View) {
        if (view.id == btnSure.id) {
            showDialog()
        }

        if (view.id == btnCancel.id) {
            finish()
        }
    }

    private fun showDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您确定重新填写？")
        dialog.setDesc("重新填写将清空填写内容")
        dialog.show()
        dialog.OnClickListener = View.OnClickListener {
            FacilitatorSplashActivity.start(this@FacilitatorInformationActivity)
        }
    }

    companion object {
        fun start(mContext: Context, openType: Int) {
            val intent = Intent(mContext, FacilitatorInformationActivity::class.java)
            intent.putExtra("OPEN_TYPE", openType)
            mContext.startActivity(intent)
        }
    }
}