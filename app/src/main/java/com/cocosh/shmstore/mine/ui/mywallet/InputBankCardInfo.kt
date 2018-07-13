package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.BankTypeModel
import com.cocosh.shmstore.mine.presenter.BankTypePresenter
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.PickerViewUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_input_bankcard_info.*

/**
 * Created by lmg on 2018/4/17.
 */
class InputBankCardInfo : BaseActivity(), MineContrat.IBankTypeView {
    private lateinit var pickerViewUtils: PickerViewUtils
    var isNameOk = false
    var isNumberOk = false
    var idx = ""
    var mPresenter = BankTypePresenter(this, this)
    override fun bankType(result: BaseModel<ArrayList<BankTypeModel>>) {
        if (result.success && result.code == 200) {
            pickerViewUtils.showBankType(object : PickerViewUtils.OnResultListener {
                override fun onResult(data: BankTypeModel) {
                    edtBankType.setText(data.bankName + " 储蓄卡")
                    idx = data.idBankBaseInfo ?: ""
                }
            }, result.entity!!)
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun bankInfoCheck(result: BaseModel<String>) {
        if (result.success && result.code == 200) {
            if (result.entity == null) {
                showError(result.message ?: "错误")
            } else {
                BindBankCardMessage.start(this, result.entity!!)
            }
        } else {
            showError(result.message ?: "错误")
        }
    }

    override fun setLayout(): Int = R.layout.activity_input_bankcard_info

    override fun initView() {
        titleManager.defaultTitle("填写银行卡信息")
        pickerViewUtils = PickerViewUtils(this)
        edtBankType.setOnClickListener(this)
        next.setOnClickListener(this)

        edtBankType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!edtBankType.text.isNullOrEmpty()) {
                    isNameOk = true
                    if (isNumberOk) {
                        next.setBackgroundResource(R.color.red)
                    }
                    return

                }
                isNameOk = false
                next.setBackgroundResource(R.color.grayBtn)
            }
        })
        edtPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!edtPhone.text.isNullOrEmpty() && edtPhone.text.length == 11) {
                    isNumberOk = true
                    if (isNameOk) {
                        next.setBackgroundResource(R.color.red)
                    }
                    return

                }
                isNumberOk = false
                next.setBackgroundResource(R.color.grayBtn)
            }
        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            edtBankType.id -> {
                mPresenter.requestBankTypeData(0)
            }
            next.id -> {
                if (isNameOk && isNumberOk) {
                    //存储
                    var map = SmApplication.getApp().getData<HashMap<String, String>>(DataCode.ADDBANK_KEY_MAP, true)
                    map!!["idxBankBaseInfo"] = idx
                    map!!["cardUserPhone"] = edtPhone.text.toString()
                    SmApplication.getApp().setData(DataCode.ADDBANK_KEY_MAP, map)
                    mPresenter.requestBankInfoCheck(0, map["cardNumber"] ?: "", map["cardUserName"]
                            ?: "", edtPhone.text.toString())
                    return
                }
            }
        }
    }

    override fun reTryGetData() {
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, InputBankCardInfo::class.java))
        }
    }

    fun showError(error: String) {
        val mDialog = SmediaDialog(this)
        mDialog.singleButton()
        mDialog.setTitle(error)
        mDialog.show()
    }
}