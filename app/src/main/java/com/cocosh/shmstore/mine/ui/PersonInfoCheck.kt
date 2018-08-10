package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.ResetPass
import com.cocosh.shmstore.mine.presenter.CheckPersonInfoPresenter
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_check_person_info.*

/**
 *
 * Created by lmg on 2018/5/4.
 */
class PersonInfoCheck : BaseActivity(), MineContrat.ICheckPersonInfoView {
    val TYPE_OPEN = "TYPE_OPEN" //check 检查页，auth 实人认证页
    val TYPE_FINISH = "TYPE_FINISH" //完成后 返回到那个页面
    var isNameOk = false
    var isPhoneOk = false
    var type = ""
    var mPresenter = CheckPersonInfoPresenter(this, this)
    override fun setLayout(): Int = R.layout.activity_check_person_info
    override fun checkPersonInfo(result: BaseBean<ResetPass>) {
            SmApplication.getApp().setData(DataCode.RESET_PAY_PASS,result.message)
            SetPayPwdActivity.start(this)
    }

    override fun initView() {
        titleManager.defaultTitle("设置支付密码")
        type = intent.getStringExtra(TYPE_OPEN)
        if (type == "check") {
            tvTitle.visibility = View.GONE
            nameRl.visibility = View.GONE
            line.visibility = View.GONE
            idCardNumRl.visibility = View.GONE
            desc.visibility = View.VISIBLE
            next.setBackgroundResource(R.color.red)
            next.text = "前往实人认证"
        }
        next.setOnClickListener(this)
        edtBankType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!edtBankType.text.isNullOrEmpty() && edtBankType.text.length >= 2) {
                    isNameOk = true
                    if (isPhoneOk) {
                        next.setBackgroundResource(R.color.red)
                    }
                    return
                }
                isNameOk = false
                next.setBackgroundResource(R.color.grayBtn)
            }
        })
        edtPhone.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!edtPhone.text.isNullOrEmpty() && edtPhone.text.length == 18) {
                    isPhoneOk = true
                    if (isNameOk) {
                        next.setBackgroundResource(R.color.red)
                    }
                    return
                }
                isPhoneOk = false
                next.setBackgroundResource(R.color.grayBtn)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    override fun onListener(view: View) {
        when (view.id) {
            next.id -> {
                if (type == "check") {
                    val intent = Intent(this, ScanIdCardActivity::class.java)
                    intent.putExtra("type", "实人认证")
                    startActivity(intent)
                } else {
                    if (isPhoneOk && isNameOk) {
                        mPresenter.requestCheckPersonInfoData(edtBankType.text.toString(), edtPhone.text.toString())
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context, type: String) {
            mContext.startActivity(Intent(mContext, PersonInfoCheck::class.java).putExtra("TYPE_OPEN", type))
        }
    }
}