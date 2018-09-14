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
import com.cocosh.shmstore.base.BaseBean
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
 *
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

    override fun bankListDraw(result: BaseBean<BankDrawListModel>) {
        hideReTryLayout()
        listDatas.clear()
        if (result.message != null && result.message?.bankcard?.size ?: 0 > 0) {
            listDatas.addAll(result.message?.bankcard!!)
            ivPic.visibility = View.VISIBLE
            GlideUtils.loadDefault(this, result.message?.bankcard!![0].bank_logo, ivPic)
            tvName.text = result.message?.bankcard!![0].bank_name
            text_treaty.text = ("单笔提现金额不低于${result.message?.amt?.amt_min}元，最高${result.message?.amt?.amt_max}元，每笔提现将扣除${result.message?.amt?.fee}元手续费")
            checkBankId = result.message?.bankcard!![0].id ?: ""
            maxMoney = result.message?.amt?.amt_max.toString()
            minMoney = result.message?.amt?.amt_min.toString()
        } else {
            ivPic.visibility = View.GONE
            tvName.text = "请绑定银行卡"
        }
    }

    override fun myWalletDraw(result: BaseBean<WithDrawResultModel>) {
        if (result.message == null){
            mDialog?.dismiss()
            return
        }
        mDialog?.getResult(result)
    }

    override fun entWalletDrawData(result: BaseBean<WithDrawResultModel>) {
        if (result.message == null){
            mDialog?.dismiss()
            return
        }
        mDialog?.getResult(result)
    }

    override fun runningNum(result: BaseBean<String>) {
        runningNum = result.message
    }


    override fun corporateAccountData(result: BaseBean<CorporateAccountModel>) {

        edtBankAccount.setText(result.message?.acct?.account)
        etName.text = result.message?.acct?.name
        edtBankName.setText(result.message?.acct?.bank)
        maxMoney = result.message?.amt?.amt_max.toString()
        minMoney = result.message?.amt?.amt_min.toString()
        text_treaty.text = ("单笔提现金额不低于${minMoney}元，最高${maxMoney}元，每笔提现将扣除2元手续费")

    }


    override fun initView() {
        titleManager.defaultTitle("提现")
        pickerViewUtils = PickerViewUtils(this)
        TYPE_WITHDRAW = intent.getStringExtra("TYPE_WITHDRAW")
        money = intent.getStringExtra("profit") ?: "0"
        if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {
            addBank.visibility = View.GONE
            enterPriseBank.visibility = View.VISIBLE
            mPresenter.requestCorporateAccountData(1)
        } else {
            mPresenter.requestRunningNum(0)
        }
        val mFilters = arrayOf<InputFilter>(CashierInputFilter())
        tvMoney.filters = mFilters

        showHint.text = ("可提现金额" + money + "元")
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
                GlideUtils.loadDefault(this@WithDrawActivity, listDatas[index - 1].bank_logo, ivPic)
                tvName.text = listDatas[index - 1].bank_name
                checkBankId = listDatas[index - 1].id ?: ""
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
                    showHint.text = ("可提现金额" + money + "元")
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
                    showHint.text = ("可提现金额" + money + "元")
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
                    if (UserManager2.getCommonData()?.paypass == 1) {
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
                    if (UserManager2.getCommonData()?.paypass != 1) {
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

    private fun showEntDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您未设置过支付密码，设置前将验证您的身份，即将发送验证码到" + UserManager2.getCryptogramPhone())
        dialog.OnClickListener = View.OnClickListener {
            SmApplication.getApp().isDelete = false
            SmApplication.getApp().activityName = this@WithDrawActivity.javaClass
            CheckPayPwdMessage.start(this@WithDrawActivity, SMSType.INIT_PAYPASS)
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
                    val bank = listDatas.find { it.id == checkBankId }

                    //提现成功 跳转提现结果页
                    if (TYPE_WITHDRAW == Constant.TYPE_ENTERPRISE) {
                        WithDrawResult.start(this@WithDrawActivity, TYPE_WITHDRAW,
                                tvMoney.text.toString(),
                                "2.00",
                                bank?.card_no ?: "",
                                data?.time ?: "",
                                data?.intime ?: "",
                                data?.no ?: "",
                                data?.status ?: "",
                                bank?.bank_name ?: "",
                                bank?.realname ?: "")
                    } else {
                        WithDrawResult.start(this@WithDrawActivity, TYPE_WITHDRAW,
                                tvMoney.text.toString(),
                                "2.00",
                                bank?.card_no ?: "",
                                data?.time ?: "",
                                data?.intime ?: "",
                                data?.no ?: "",
                                data?.status ?: "",
                                bank?.bank_name ?: "",
                                bank?.realname ?: "")
                    }

                } else {
                    //提现失败 跳转提现结果页

                }
            }
        })
    }

    companion object {
        fun start(mContext: Context, type: String, money: String) {
            mContext.startActivity(Intent(mContext, WithDrawActivity::class.java).putExtra("TYPE_WITHDRAW", type).putExtra("profit", money))
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