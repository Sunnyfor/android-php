package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.ResetPass
import com.cocosh.shmstore.sms.data.SMSLoader
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.activity_reset_pass_code.*
import java.util.*
import kotlin.collections.HashMap


/**
 *
 * Created by zhangye on 2018/5/11.
 */
class ResetPassCodeActivity : BaseActivity() {


    private lateinit var codeStr: StringBuilder
    private val passList = ArrayList<EditText>()
    private var phone = UserManager2.getLogin()?.phone
    private var smsLoader = SMSLoader(this)
    private var smsKey = ""
//    override fun onCodeResult(result: BaseModel<String>) {
//        if (result.success) {
//            tbSend.action()
//        } else {
//            ToastUtil.show(result.message)
//        }
//        tbSend.setClick(true)
//    }
//
//    override fun onForgetPassResult(result: BaseModel<String>) {
//        if (result.success) {
//            val it = Intent(this, SettingPasswordActivity::class.java)
//            it.putExtra("type", 1)
//            it.putExtra("token", result.entity)
//            startActivity(it)
//        } else {
//            ToastUtil.show(result.message)
//        }
//    }

    override fun setLayout(): Int = R.layout.activity_reset_pass_code

    override fun initView() {
        phone?.let {
            tvPhone.text = it.replace(it.substring(3, 7), "****")
        }

        passList.add(pwd_1)
        passList.add(pwd_2)
        passList.add(pwd_3)
        passList.add(pwd_4)
        passList.add(pwd_5)
        passList.add(pwd_6)

        passList.forEach {
            it.setOnTouchListener { view: View, event: MotionEvent ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    showKeyboard(view)
                }
                return@setOnTouchListener true
            }
        }

        pwd_1.requestFocus()


        pwd_1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    pwd_2.requestFocus()
                }
            }

        })

        pwd_2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    pwd_3.requestFocus()
                }
            }

        })

        pwd_3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    pwd_4.requestFocus()
                }
            }

        })


        pwd_4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    pwd_5.requestFocus()
                }
            }

        })

        pwd_5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    pwd_6.requestFocus()
                }
            }

        })

        pwd_6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    return
                }

                codeStr = StringBuilder()
                passList.forEach {
                    codeStr.append(it.text)
                }
                if (codeStr.length == 6) {
                    phone?.let {
                        checkSMSCode(codeStr.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

        })


        pwd_2.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (pwd_2.text.isEmpty()) {
                    pwd_1.requestFocus()
                    pwd_1.setText("")
                }
            }
            false
        }



        pwd_3.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (pwd_3.text.isEmpty()) {
                    pwd_2.requestFocus()
                    pwd_2.setText("")
                }
            }
            false
        }



        pwd_4.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (pwd_4.text.isEmpty()) {
                    pwd_3.requestFocus()
                    pwd_3.setText("")
                }
            }
            false
        }



        pwd_5.setOnKeyListener { _, keyCode, event ->

            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (pwd_5.text.isEmpty()) {
                    pwd_4.requestFocus()
                    pwd_4.setText("")
                }
            }
            false
        }


        pwd_6.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (pwd_6.text.isEmpty()) {
                    pwd_5.requestFocus()
                    pwd_5.setText("")
                }
            }
            false
        }


        titleManager.defaultTitle("修改登录密码")


        phone?.let {
            sendSMS()
        }

        tbSend.setCallListener(View.OnClickListener {
            //发送验证码
            phone?.let {
                sendSMS()
            }
        })

    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    private fun sendSMS() {
        smsLoader.sendCode(phone
                ?: "", SMSType.RESET_PASS, object : ApiManager2.OnResult<BaseBean<SMS>>() {
            override fun onSuccess(data: BaseBean<SMS>) {
                smsKey = data.message?.smskey ?: ""
                onCodeResult()
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<SMS>) {

            }
        })

    }

    private fun checkSMSCode(smscode: String) {
        val params = HashMap<String, String>()
        params["smscode"] = smscode
        params["smskey"] = smsKey
        ApiManager2.post(this, params, Constant.SMSCHECK, object : ApiManager2.OnResult<BaseBean<ResetPass>>() {
            override fun onSuccess(data: BaseBean<ResetPass>) {
                onCheckCodeResult(data)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ResetPass>) {
            }

        })
    }


    fun onCodeResult() {
        tbSend.action()
        tbSend.setClick(true)
    }

    fun onCheckCodeResult(result: BaseBean<ResetPass>) {
        val it = Intent(this, SettingPasswordActivity::class.java)
        SmApplication.getApp().setData(DataCode.RESETPASS, result.message)
        it.putExtra("type", 1)
        startActivity(it)
    }
}