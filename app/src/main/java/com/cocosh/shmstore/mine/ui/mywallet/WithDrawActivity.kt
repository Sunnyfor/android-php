package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.*
import com.cocosh.shmstore.mine.presenter.MyWalletDrawPresenter
import com.cocosh.shmstore.mine.ui.CheckPayPwdMessage
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BankListDialog
import com.cocosh.shmstore.widget.dialog.SercurityDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_withdraw.*

/**
 * Created by lmg on 2018/4/18.
 */
class WithDrawActivity : BaseActivity(), MineContrat.IMyWalletDrawView {
    private var TYPE_WITHDRAW = ""//type_my我的钱包提现 "type_enterprise"企业体现
    private lateinit var pickerViewUtils: PickerViewUtils
    lateinit var mOnItemClickListener: BankListDialog.OnItemClickListener
    var mPresenter = MyWalletDrawPresenter(this, this)
    var listDatas = arrayListOf<BankModel>()
    var money: String? = "0"
    var checkBankId = ""
    var isClick = false
    var runningNum: String? = ""
    var maxMoney: String? = "0"
    var minMoney: String? = "0"
    override fun setLayout(): Int = R.layout.activity_withdraw

    override fun bankListDraw(result: BaseModel<BankDrawListModel>) {
        if (result.success && result.code == 200) {
            hideReTryLayout()
            listDatas.clear()
            if (result.entity != null && result.entity?.list?.size ?: 0 > 0) {
                listDatas.addAll(result.entity?.list!!)
                ivPic.visibility = View.VISIBLE
                GlideUtils.loadDefault(this, result.entity?.list!![0].bank_log, ivPic)
                tvName.text = result.entity?.list!![0].bank_name
                text_treaty.text = result.entity?.ruleType?.des
                checkBankId = result.entity?.list!![0].bank_kind ?: ""
                maxMoney = result.entity?.ruleType?.maxMoney ?: "0"
                minMoney = result.entity?.ruleType?.minMoney ?: "0"
            } else {
                ivPic.visibility = View.GONE
                tvName.text = "请绑定银行卡"
            }
        } else {
            showReTryLayout()
            ToastUtil.show(result.message)
            ivPic.visibility = View.GONE
            tvName.text = "请绑定银行卡"
        }
    }

    override fun myWalletDraw(result: BaseModel<WithDrawResultModel>) {
        mDialog?.getResult(result)
    }

    override fun entWalletDrawData(result: BaseModel<WithDrawResultModel>) {
        mDialog?.getResult(result)
    }

    override fun runningNum(result: BaseModel<String>) {
        if (result.success && result.code == 200) {
            runningNum = result.entity
        } else {
            ToastUtil.show(result.message)
        }
    }


    override fun corporateAccountData(result: BaseModel<CorporateAccountModel>) =
            if (result.success && result.code == 200) {
                edtBankAccount.setText(result.entity?.bankNumber)
                etName.text = result.entity?.accountName
                edtBankName.setText(result.entity?.openingBankName)
                text_treaty.text = result.entity?.ruleType?.des
                maxMoney = result.entity?.ruleType?.maxMoney ?: "0"
                minMoney = result.entity?.ruleType?.minMoney ?: "0"
            } else {
                ToastUtil.show(result.message)
            }


    override fun initView() {
        titleManager.defaultTitle("提现")
        pickerViewUtils = PickerViewUtils(this)
        TYPE_WITHDRAW = intent.getStringExtra("TYPE_WITHDRAW")
        money = intent.getStringExtra("money") ?: "0"
        if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {
            addBank.visibility = View.GONE
            enterPriseBank.visibility = View.VISIBLE
            mPresenter.requestCorporateAccountData(1)
        } else {
            mPresenter.requestRunningNum(0)
        }
        val mFilters = arrayOf<InputFilter>(CashierInputFilter())
        tvMoney.filters = mFilters

        showHint.text = "可提现金额" + money + "元"
        addBank.setOnClickListener(this)
        tvAllOut.setOnClickListener(this)
        btnCharge.setOnClickListener(this)
        mOnItemClickListener = object : BankListDialog.OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                if (index == listDatas.size + 1) {
                    SmApplication.getApp().activityName = this@WithDrawActivity::class.java as Class<BaseActivity>?
                    AddBankCardActivity.start(this@WithDrawActivity)
                    return
                }
                GlideUtils.loadDefault(this@WithDrawActivity, listDatas[index - 1]?.bank_log, ivPic)
                tvName.text = listDatas[index - 1]?.bank_name
                checkBankId = listDatas[index - 1]?.id ?: ""
            }
        }

        tvMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (tvMoney.text.isEmpty()) {
                    hint.visibility = View.VISIBLE
                    showHint.setTextColor(resources.getColor(R.color.textGray))
                    showHint.text = "可提现金额" + money + "元"
                    tvAllOut.visibility = View.VISIBLE
                    isClick = false
                    btnCharge.setBackgroundResource(R.color.grayBtn)
                } else {
                    isClick = false
                    hint.visibility = View.INVISIBLE
                    if (tvMoney.text?.toString()?.toDouble()!! > money?.toDouble()!!) {
                        tvAllOut.visibility = View.GONE
                        showHint.text = "输入金额超过可提现金额"
                        showHint.setTextColor(resources.getColor(R.color.red))
                        btnCharge.setBackgroundResource(R.color.grayBtn)
                        return
                    }
                    showHint.setTextColor(resources.getColor(R.color.textGray))
                    showHint.text = "可提现金额" + money + "元"
                    tvAllOut.visibility = View.VISIBLE

                    if (tvMoney.text?.toString()?.toDouble()!! <= minMoney!!.toDouble()) {
                        btnCharge.setBackgroundResource(R.color.grayBtn)
                        return
                    }

                    if (tvMoney.text?.toString()?.toDouble()!! > maxMoney!!.toDouble()) {
                        btnCharge.setBackgroundResource(R.color.grayBtn)
                        return
                    }
                    if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {

                    } else {
                        if (listDatas.size == 0) {
                            return
                        }
                    }


                    isClick = true
                    btnCharge.setBackgroundResource(R.color.red)
                }
            }

        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            addBank.id -> {
                if (listDatas.size == 0) {
                    //判断是否实设置支付密码
                    if (UserManager.getPayPwdStatus() == true) {
                        SmApplication.getApp().activityName = this@WithDrawActivity.javaClass
                        AddBankCardActivity.start(this)
                        return
                    }
                    //实人认证
                    showEntDialog()
                    return
                }
                pickerViewUtils.showBankList(listDatas, mOnItemClickListener)
            }
            btnCharge.id -> {
                if (!isClick) {
                    return
                }
                if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {
                    //判断是否实设置支付密码
                    if (UserManager.getPayPwdStatus() != true) {
                        SmApplication.getApp().activityName = this@WithDrawActivity.javaClass
                        SmApplication.getApp().isDelete = true
                        showEntDialog()
                        return
                    }
                } else {
                    if (checkBankId.isNullOrEmpty()) {
                        ToastUtil.show("请选择银行卡")
                        return
                    }
                }

                SmApplication.getApp().isDelete = true
                showImputPsdDialog()
            }
            tvAllOut.id -> {
                tvMoney.setText(money)
            }
        }
    }

    override fun reTryGetData() {
        if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {
            mPresenter.requestCorporateAccountData(1)
        } else {
            mPresenter.requestBankListData()
        }
    }

    fun showEntDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您未设置过支付密码，设置前将验证您的身份，即将发送验证码到" + UserManager.getCryptogramPhone())
        dialog.OnClickListener = View.OnClickListener {
            SmApplication.getApp().isDelete = false
            SmApplication.getApp().activityName = this@WithDrawActivity.javaClass
            CheckPayPwdMessage.start(this@WithDrawActivity,SMSType.INIT_PAYPASS)
        }
        dialog.show()
    }

    override fun onResume() {
        if (TYPE_WITHDRAW != Constant.TYPE_ENTERPRISE) {
            mPresenter.requestBankListData()
        }
        super.onResume()
    }


    var mDialog: SercurityDialog<WithDrawResultModel>? = null
    fun showImputPsdDialog() {
        mDialog = SercurityDialog<WithDrawResultModel>(this, R.style.SercurityDialogTheme)
        mDialog?.show()
        mDialog?.setOnInputCompleteListener(object : SercurityDialog.InputCompleteListener<WithDrawResultModel> {
            override fun inputComplete(pwd: String) {
                if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {
                    mPresenter.requestEntWalletDrawData(tvMoney.text.toString(), pwd)
                } else {
                    mPresenter.requestMyWalletDrawData(checkBankId, tvMoney.text.toString(),
                            runningNum ?: "", pwd)
                }
            }

            override fun result(boolean: Boolean, data: WithDrawResultModel?) {
                if (boolean) {
                    //提现成功 跳转提现结果页
                    if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {
                        WithDrawResult.start(this@WithDrawActivity, TYPE_WITHDRAW,
                                data?.amount ?: "",
                                data?.charge ?: "",
                                data?.cardNumber ?: "",
                                data?.dealBeginDate ?: "",
                                data?.estimatedaArrivalTime ?: "",
                                data?.runningNum ?: "",
                                data?.resultCode ?: "",
                                data?.bankName ?: "",
                                data?.userBankName ?: "")
                    } else {
                        WithDrawResult.start(this@WithDrawActivity, TYPE_WITHDRAW,
                                data?.amount ?: "",
                                data?.charge ?: "",
                                data?.cardNumber ?: "",
                                data?.dealBeginDate ?: "",
                                data?.estimatedaArrivalTime ?: "",
                                data?.runningNum ?: "",
                                data?.resultCode ?: "",
                                data?.bankName ?: "", "")
                    }

                } else {
                    //提现失败 跳转提现结果页

                }
            }
        })
    }

    companion object {
        fun start(mContext: Context, type: String, money: String) {
            mContext.startActivity(Intent(mContext, WithDrawActivity::class.java).putExtra("TYPE_WITHDRAW", type).putExtra("money", money))
        }
    }

    override fun onBackPressed() {
        mDialog?.clearNum()
        super.onBackPressed()
    }

    override fun onDestroy() {
        SmApplication.getApp().activityName = null
        SmApplication.getApp().isDelete = false
        super.onDestroy()
    }
}