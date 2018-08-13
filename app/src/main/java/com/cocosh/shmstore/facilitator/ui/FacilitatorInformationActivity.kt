package com.cocosh.shmstore.facilitator.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.facilitator.ui.contrat.FacilitatorContrat
import com.cocosh.shmstore.facilitator.ui.presenter.FacilitatorPresenter
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.UserManager2
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
    override fun setShowData(result: BaseBean<EntActiveInfoModel>) {
        result.message?.let {
            updateView(it)
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
            SmApplication.getApp().getData<HashMap<String, String>>(DataCode.FACILITATOR_KEY_MAP, false)?.let {
                updateView(it)
            }

        } else {
            presenter.getShowData(1)
            llBtn.visibility = View.GONE
            isvStatus.visibility = View.VISIBLE
            isvTime.visibility = View.VISIBLE
        }

        btnCancel.setOnClickListener(this)
    }

    private fun updateView(data: EntActiveInfoModel) {
        //认证信息
        when (data.cert.status) {
            AuthenStatus.SERVER_DEALER_NO.type -> {

            }
            AuthenStatus.SERVER_DEALER_OK.type -> {
                isvStatus.setNoIconValue("已认证")
            }
            AuthenStatus.SERVER_DEALER_FAIL.type -> {

            }
            AuthenStatus.SERVER_DEALER_ING.type -> {

            }
        }
        isvTime.setNoIconValue(data.cert.time)
        isvShouMeiNumber.setNoIconValue(UserManager2.getLogin()?.code) //首媒号
        isvEntAccount.setNoIconValue(UserManager2.getCryptogramPhone()) //认证账号
        isvEntArea.setNoIconValue(data.base.addr) //认证地区
        isvEntMoney.setNoIconValue(data.cert.fee + "元") //认证费用
        //基础信息
        isvFname.setNoIconValue(data.base.name) //名称
        isvFNumber.setNoIconValue(data.base.uscc)//统一社会信用代码
        isvFLayerName.setNoIconValue(data.base.legal) //法定代表人
        if (data.base.end_time.isEmpty()) {
            isvFTime.setNoIconValue(data.base.beg_time) //营业期限
        } else {
            isvFTime.setNoIconValue(data.base.beg_time + "至" + data.base.end_time) //营业期限
        }
        isvFType.setNoIconValue(data.base.kind)//类型
        isvFaddress.setNoIconValue(data.base.addr)//住所
        isvFMoney.setNoIconValue(data.base.capital)//注册资本
        isvFStartTime.setNoIconValue(data.base.found)//成立时间
        isvImg.setNoIconValue("已提交") //营业执照
        isvAccount.setNoIconValue(data.base.account) //对公账号
        isvBankName.setNoIconValue(data.base.bank) //银行名称
        bank.setNoIconValue(data.base.tel) //手机号
    }


    private fun updateView(data: HashMap<String,String>) {

//        isvTime.setNoIconValue(data[""])
        isvShouMeiNumber.setNoIconValue(UserManager2.getLogin()?.code) //首媒号
        isvEntAccount.setNoIconValue(UserManager2.getCryptogramPhone()) //认证账号
        isvEntArea.setNoIconValue(data["addr"]) //认证地区
        isvEntMoney.setNoIconValue(data["money"] + "元") //认证费用
        //基础信息
        isvFname.setNoIconValue(data["name"]) //名称
        isvFNumber.setNoIconValue(data["uscc"])//统一社会信用代码
        isvFLayerName.setNoIconValue(data["legal"]) //法定代表人
        if (data["end_time"].isNullOrEmpty()) {
            isvFTime.setNoIconValue(data["beg_time"]) //营业期限
        } else {
            isvFTime.setNoIconValue(data["beg_time"] + "至" + data["end_time"]) //营业期限
        }
        isvFType.setNoIconValue(data["kind"])//类型
        isvFaddress.setNoIconValue(data["addr"])//住所
        isvFMoney.setNoIconValue(data["capital"])//注册资本
        isvFStartTime.setNoIconValue(data["found"])//成立时间
        isvImg.setNoIconValue("已提交") //营业执照
        isvAccount.setNoIconValue(data["account"]) //对公账号
        isvBankName.setNoIconValue(data["bank"]) //银行名称
        bank.setNoIconValue(data["tel"]) //手机号
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