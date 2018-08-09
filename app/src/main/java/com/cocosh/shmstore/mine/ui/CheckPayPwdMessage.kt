package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.model.PayPassworType
import com.cocosh.shmstore.mine.presenter.SendMessagePresenter
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.activity_message_check_paypwd.*


/**
 *
 * Created by lmg on 2018/4/17.
 */
class CheckPayPwdMessage : BaseActivity(), MineContrat.ISendMessageView {
    var mPresenter = SendMessagePresenter(this, this)
    private var time: TimeCount? = null
    var isSet = false
    var passType = ""
    override fun setLayout(): Int = R.layout.activity_message_check_paypwd

    override fun authCode(result: BaseBean<String>) {
        //校验 验证码

        //判断是否实人认证
        if (UserManager2.getCommonData()?.cert?.r == AuthenStatus.PERSION_OK.type) {
            PersonInfoCheck.start(this@CheckPayPwdMessage, "auth")
            finish()
        } else {
            //实人认证
            PersonInfoCheck.start(this@CheckPayPwdMessage, "check")
            finish()
        }
    }

    override fun sendMessageData(result: BaseBean<SMS>) {
        //发送验证码
        time?.start()

    }

    override fun initView() {
        passType = intent.getStringExtra("type")
        titleManager.defaultTitle("设置支付密码")
        mPresenter.requestSendMessageData(UserManager2.getLogin()?.phone
                ?: "", SMSType.valueOf(passType))//"18611154535"
        time = TimeCount(60000, 1000)
        btnMessage.setOnClickListener(this)
        edtCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtCode.text.length == 6) {
                    val checkType:PayPassworType = when(passType){
                        SMSType.FORGOT_PAYPASS.value -> PayPassworType.FORGOT
                        SMSType.INIT_PAYPASS.value -> PayPassworType.INIT
                        SMSType.RESET_PAYPASS.value -> PayPassworType.RESET
                        else -> {
                            PayPassworType.INIT
                        }
                    }
                    mPresenter.requestAuthCodeData(checkType.type, edtCode.text.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        desc.text = ("我们已发送 验证码 到您的手机\n" + UserManager2.getCryptogramPhone())
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnMessage.id -> {
                mPresenter.requestSendMessageData(UserManager2.getLogin()?.phone
                        ?: "", SMSType.valueOf(passType))
            }
        }
    }

    override fun reTryGetData() {
        mPresenter.requestSendMessageData(UserManager2.getLogin()?.phone
                ?: "", SMSType.valueOf(passType))
    }

    internal inner class TimeCount(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            btnMessage.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
            btnMessage.isClickable = false
            btnMessage.text = ("填写验证码(" + millisUntilFinished / 1000 + "s)")
        }

        override fun onFinish() {
            btnMessage.text = "重新获取验证码"
            btnMessage.isClickable = true
            btnMessage.setBackgroundResource(R.drawable.shape_rectangle_round_red)
        }
    }

    override fun onDestroy() {
        time?.cancel()
        time = null
        super.onDestroy()
    }

    companion object {
        fun start(mContext: Context, type: SMSType) {
            val intent = Intent(mContext, CheckPayPwdMessage::class.java)
            intent.putExtra("type", type.name)
            mContext.startActivity(intent)
        }
    }
}