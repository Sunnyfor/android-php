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
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.PayPassworType
import com.cocosh.shmstore.mine.model.RedToWalletModel
import com.cocosh.shmstore.mine.presenter.RedToWalletPresenter
import com.cocosh.shmstore.mine.ui.CheckPayPwdMessage
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.CashierInputFilter
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SercurityDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_out_to_wallet.*


/**
 * Created by lmg on 2018/4/17.
 */
class OutToWalletActivity : BaseActivity(), MineContrat.IRedToWalletView {
    var isClick = false
    var money: String? = "0"
    private var TYEP_OUTTOWALLET: String? = ""
    var runningNum: String? = ""
    var mPresenter = RedToWalletPresenter(this, this)
    override fun setLayout(): Int = R.layout.activity_out_to_wallet

    override fun redToWalletData(result: BaseModel<RedToWalletModel>) {
        mDialog?.getResult(result)
    }

    override fun runningNumData(result: BaseModel<String>) {
        if (result.success && result.code == 200) {
            runningNum = result.entity
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun outToData(result: BaseModel<RedToWalletModel>) {
        mDialog?.getResult(result)
    }


    override fun initView() {
        val mFilters = arrayOf<InputFilter>(CashierInputFilter())
        tvMoney.filters = mFilters

        TYEP_OUTTOWALLET = intent.getStringExtra("TYEP_OUTTOWALLET")
        if (TYEP_OUTTOWALLET == CommonType.FACILITATOR_OUTTOWALLET.type) {
            mPresenter.requestRunningNumTo(1)
            titleManager.defaultTitle("转出至服务商钱包")
        } else if (TYEP_OUTTOWALLET == CommonType.CERTIFICATION_OUTTOWALLET.type) {
            mPresenter.requestRunningNumTo(1)
            titleManager.defaultTitle("转出至钱包")
        } else {
            titleManager.defaultTitle("转出至钱包")
        }
        money = intent.getStringExtra("money") ?: "0"
        showHint.text = "可转出" + money + "元至钱包"
        tvAllOut.setOnClickListener(this)
        btnCharge.setOnClickListener(this)

        tvMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (tvMoney.text.isEmpty()) {
                    hint.visibility = View.VISIBLE
                    tvAllOut.visibility = View.VISIBLE
                    showHint.text = "可转出金额" + money + "元"
                    showHint.setTextColor(resources.getColor(R.color.textGray))
                    isClick = false
                    btnCharge.setBackgroundResource(R.color.grayBtn)
                } else {
                    isClick = false
                    hint.visibility = View.INVISIBLE

                    if (tvMoney.text?.toString()?.toDouble()!! > money?.toDouble()!!) {
                        tvAllOut.visibility = View.GONE
                        showHint.text = "输入金额超过可转出金额"
                        showHint.setTextColor(resources.getColor(R.color.red))
                        btnCharge.setBackgroundResource(R.color.grayBtn)
                        return
                    }

                    if (tvMoney.text?.toString()?.toDouble()!! <= 0) {
                        return
                    }

                    tvAllOut.visibility = View.VISIBLE
                    showHint.text = "可转出金额" + money + "元"
                    showHint.setTextColor(resources.getColor(R.color.textGray))

                    isClick = true
                    btnCharge.setBackgroundResource(R.color.red)
                }
            }

        })
    }

    var mDialog: SercurityDialog<RedToWalletModel>? = null
    fun showImputPsdDialog() {
        mDialog = SercurityDialog<RedToWalletModel>(this, R.style.SercurityDialogTheme)
        mDialog?.show()
        mDialog?.setOnInputCompleteListener(object : SercurityDialog.InputCompleteListener<RedToWalletModel> {
            override fun inputComplete(pwd: String) {
                if (TYEP_OUTTOWALLET == CommonType.FACILITATOR_OUTTOWALLET.type) {
                    mPresenter.requestDrawTo("2", tvMoney.text.toString(), runningNum ?: "", pwd)
                } else if (TYEP_OUTTOWALLET == CommonType.CERTIFICATION_OUTTOWALLET.type) {
                    mPresenter.requestDrawTo("1", tvMoney.text.toString(), runningNum ?: "", pwd)
                } else {
                    mPresenter.requestRedToWalletData(tvMoney.text.toString(), pwd)
                }
            }

            override fun result(boolean: Boolean, resultData: RedToWalletModel?) {
                if (boolean) {
                    //转账成功 跳转转账结果页
                    OutToWalletResult.start(this@OutToWalletActivity, resultData?.money, resultData?.createDate, resultData?.runningNumber, TYEP_OUTTOWALLET
                            ?: "")
                } else {
                    //转账失败
//                    OutToWalletResult.start(this@OutToWalletActivity)
                }
            }
        })
    }

    private var mPassWord = ""
    override fun onListener(view: View) {
        when (view.id) {
            tvAllOut.id -> {
                tvMoney.setText(money)
            }
            btnCharge.id -> {
                if (isClick) {
                    //判断是否设置密码
                    if (UserManager.getPayPwdStatus() == true) {
                        showImputPsdDialog()
                    } else {
                        showEntDialog()
                    }
                }
            }
        }
    }

    override fun reTryGetData() {
        mPresenter.requestRunningNumTo(1)
    }

    fun showEntDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您未设置过支付密码，设置前将验证您的身份，即将发送验证码到" + UserManager.getCryptogramPhone())
        dialog.OnClickListener = View.OnClickListener {
            SmApplication.getApp().activityName = this@OutToWalletActivity.javaClass
            CheckPayPwdMessage.start(this@OutToWalletActivity,SMSType.INIT_PAYPASS)
        }
        dialog.show()
    }

    override fun onBackPressed() {
        mDialog?.clearNum()
        super.onBackPressed()
    }

    companion object {
        fun start(mContext: Context, type: String, money: String) {
            mContext.startActivity(Intent(mContext, OutToWalletActivity::class.java).putExtra("TYEP_OUTTOWALLET", type).putExtra("money", money))
        }
    }
}