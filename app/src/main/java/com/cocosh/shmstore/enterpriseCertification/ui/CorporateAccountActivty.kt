package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntBankPresenter
import com.cocosh.shmstore.facilitator.ui.PayFranchiseFeeActivity
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_public_account.*
import java.util.regex.Pattern

/**
 * Created by lmg on 2018/3/14.
 * 对公账户
 */
class CorporateAccountActivty : BaseActivity(), EntCertificationContrat.IBankView, TextWatcher {
    override fun reTryGetData() {

    }

    var isClick = false
    var openType = -1
    var presenter = EntBankPresenter(this, this)
    override fun setData(data: BaseBean<String>) {
            EnterpriseActiveActivity.start(this)
    }

    override fun afterTextChanged(s: Editable?) {
        isClick = false
        if (TextUtils.isEmpty(edtPhoneNumber.text)) {
            isClick = false
            btnInput.setBackgroundResource(R.color.grayBtn)
            return
        }

        if (TextUtils.isEmpty(edtBankAccount.text)) {
            isClick = false
            btnInput.setBackgroundResource(R.color.grayBtn)
            return
        }
        if (TextUtils.isEmpty(edtBankName.text)) {
            isClick = false
            btnInput.setBackgroundResource(R.color.grayBtn)
            return
        }
        if (edtPhoneNumber.text.length == 11) {
            isClick = true
            btnInput.setBackgroundResource(R.color.red)
        } else {
            isClick = false
            btnInput.setBackgroundResource(R.color.grayBtn)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun setLayout(): Int = R.layout.activity_public_account

    override fun initView() {
        val filter2 = InputFilter { source, _, _, _, _, _ ->
            val speChat = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“'。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())
            if (matcher.find()) "" else null;
        }
        //保留布局内的过滤条件
        val mFilters = arrayOfNulls<InputFilter>(tvName.filters.size)
        tvName.filters.forEachIndexed<InputFilter?> { i: Int, inputFilter: InputFilter? ->
            if (inputFilter != null) {
                mFilters[i] = inputFilter
            }
        }
        mFilters[tvName.filters.size - 2] = filter2
        tvName.filters = mFilters

        titleManager.defaultTitle("填写企业对公账户")
        openType = intent.getIntExtra("ACCOUNT_TYPE", -1)
        if (openType == 444) {
            tvName.setText(SmApplication.getApp().getData<HashMap<String, String>>(DataCode.FACILITATOR_KEY_MAP, false)!!["corpFname"])
            tvLayerName.setText(SmApplication.getApp().getData<HashMap<String, String>>(DataCode.FACILITATOR_KEY_MAP, false)!!["legalRepresentative"])
        } else {
            tvName.setText(SmApplication.getApp().getData<String>(DataCode.COMPANY_NAME, false))
            tvLayerName.setText(SmApplication.getApp().getData<String>(DataCode.LAYER_NAME, false))
        }
        initListener()
    }

    private fun initListener() {
        edtBankAccount.addTextChangedListener(this)
        edtBankName.addTextChangedListener(this)
        edtPhoneNumber.addTextChangedListener(this)
        btnInput.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnInput.id -> {
                if (!isClick) {
                    return
                }
                if (openType == 444) {
                    //提交信息
                    if (!TextUtils.isEmpty(edtPhoneNumber.text)
                            && edtPhoneNumber.text.length == 11
                            && !TextUtils.isEmpty(edtBankAccount.text)
                            && !TextUtils.isEmpty(edtBankName.text)) {
                        val map = SmApplication.getApp().getData<HashMap<String, String>>(DataCode.FACILITATOR_KEY_MAP, false) as HashMap<String, String>
                        map.put("companyBankName", edtBankName.text.toString())
                        map.put("companyBankCard", edtBankAccount.text.toString())
                        map.put("companyPhone", edtPhoneNumber.text.toString())
                        SmApplication.getApp().setData(DataCode.FACILITATOR_KEY_MAP, map)
                        //下一页 金额确认页
                        startActivity(Intent(this@CorporateAccountActivty, PayFranchiseFeeActivity::class.java))
                    }
                } else {
                    //提交信息
                    if (!TextUtils.isEmpty(edtPhoneNumber.text)
                            && edtPhoneNumber.text.length == 11
                            && !TextUtils.isEmpty(edtBankAccount.text)
                            && !TextUtils.isEmpty(edtBankName.text)) {
                        presenter.pushData("", edtBankAccount.text.toString(), edtBankName.text.toString(), "", edtPhoneNumber.text.toString(), "")
                    }
                }
            }
            else -> {
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CorporateAccountActivty::class.java))
        }

        fun start(context: Context, openType: Int) {
            context.startActivity(Intent(context, CorporateAccountActivty::class.java).putExtra("ACCOUNT_TYPE", openType))
        }
    }
}