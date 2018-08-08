package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.AddBankModel
import com.cocosh.shmstore.mine.presenter.AddBankPresenter
import com.cocosh.shmstore.mine.presenter.SendMessagePresenter
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_message_check.*
import java.util.*


/**
 * Created by lmg on 2018/4/17.
 */
class BindBankCardMessage : BaseActivity(), MineContrat.ISendMessageView, MineContrat.IAddBankView {
    var mPresenter = SendMessagePresenter(this, this)
    var mSavePresenter = AddBankPresenter(this, this)
    private var time: TimeCount? = null
    var isSet = false
    var idx: String? = null
    var map: HashMap<String, String>? = null
    override fun setLayout(): Int = R.layout.activity_message_check
    override fun addBank(result: BaseModel<AddBankModel>) {
        if (result.success && result.code == 200) {
            ToastUtil.showIcon(this@BindBankCardMessage.resources.getString(R.string.iconComplet), "绑定成功")
            if (SmApplication.getApp().activityName != null) {
                startActivity(Intent(this, SmApplication.getApp().activityName))
                finish()
                return
            }
            BankCardMangerActivity.start(this@BindBankCardMessage)
            finish()
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun authCode(result: BaseBean<String>) {
    }

    override fun sendMessageData(result: BaseBean<SMS>) {
        //发送验证码
//            hideReTryLayout()
            time?.start()
    }

    override fun initView() {
        titleManager.defaultTitle("手机验证")
        map = SmApplication.getApp().getData<HashMap<String, String>>(DataCode.ADDBANK_KEY_MAP, false)
        mPresenter.requestSendMessageData(map?.get("cardUserPhone")!!, SMSType.BANK)
        desc.text = "绑定银行卡需要短信确认，验证码已发送至手机:\n" + map?.get("cardUserPhone")!! + "，请按提示操作"
        idx = intent.getStringExtra("idx")
        time = TimeCount(60000, 1000)
        btnMessage.setOnClickListener(this)
        next.setOnClickListener(this)
        edtCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtCode.text.length == 6) {
                    isSet = true
                    next.setBackgroundResource(R.color.red)
                    return
                }
                isSet = false
                next.setBackgroundResource(R.color.grayBtn)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnMessage.id -> {
                mPresenter.requestSendMessageData(map?.get("cardUserPhone")!!, SMSType.BANK)
            }
            next.id -> {
                if (isSet) {
                    mSavePresenter.requestAddBankData(idx ?: "", edtCode.text.toString())
                }
            }
        }
    }

    override fun reTryGetData() {
        mPresenter.requestSendMessageData(map?.get("cardUserPhone")!!, SMSType.BANK)
    }

    internal inner class TimeCount(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            btnMessage.setBackgroundResource(R.drawable.shape_rectangle_round_gray)
            btnMessage.isClickable = false
            btnMessage.text = "填写验证码(" + millisUntilFinished / 1000 + "s)"
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
        fun start(mContext: Context, idx: String) {
            mContext.startActivity(Intent(mContext, BindBankCardMessage::class.java).putExtra("idx", idx))
        }
    }
}