package com.cocosh.shmstore.login.ui.activity

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.HomeActivity
import com.cocosh.shmstore.login.BindingPhoneContract
import com.cocosh.shmstore.login.BindingPhonePresenter
import com.cocosh.shmstore.login.model.Login
import com.cocosh.shmstore.login.model.OtherLogin
import com.cocosh.shmstore.utils.*
import kotlinx.android.synthetic.main.activity_binding_phone.*

/**
 * 第三方绑定手机号
 * Created by zhangye on 2018/1/26.
 */
class BindingPhoneActivity : BaseActivity(), BindingPhoneContract.IView {
    override fun reTryGetData() {

    }

    private var phoneOk = false
    private var codeOk = false
    private var phone = ""
    private val mPresenter = BindingPhonePresenter(this, this)
    private var openId: String? = null
    private var type: String? = null

    override fun setLayout(): Int = R.layout.activity_binding_phone

    override fun initView() {
        titleManager.defaultTitle("绑定手机号")

        UserManager.getLogin()?.let {
            openId = it.openId
            type = it.type
        }


        edtPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 11) {
                    phoneOk = false
                    btnCode.setClick(false)
                } else {
                    phoneOk = true
                    btnCode.setClick(true)
                }
                isOk()
            }

        })

        edtCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                codeOk = s?.length == 6
                isOk()
            }
        })


        /**
         * 验证码按钮事件
         */
        btnCode.setCallListener(View.OnClickListener {
            phone = edtPhone.text.toString()

            if (openId != null && type != null) {
                mPresenter.sendCode(type!!, openId!!, phone)
            }
        })

        btnNext.setOnClickListener(this)

    }

    override fun onListener(view: View) {
        when (view.id) {
            btnNext.id -> {
                if (phoneOk && codeOk) {
                    phone = edtPhone.text.toString()
                    if(openId != null && type != null){
                        mPresenter.checkCode(edtCode.text.toString(), openId!!, phone, type!!)
                    }

                } else {
                    LogUtil.i("不满足请求条件")
                }
            }
        }
    }


    /**
     * 校验提交条件
     */
    fun isOk() {
        if (phoneOk && codeOk) {
            btnNext.setBackgroundResource(R.color.red)
        } else {
            btnNext.setBackgroundResource(R.color.grayBtn)
        }
    }


    override fun onCodeResult(result: BaseModel<String>) {
        if (result.success) {
            btnCode.action() //验证码发送成功执行计时
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun onCheckedCodeResult(result: BaseModel<Login>) {
        if (result.success) {
            /**
             * 存储用户信息
             */
            result.entity?.let {
                val userId = it.userId
                if (userId == null) {
                    it.type = type
                    it.openId = openId
                    UserManager.setLogin(it)
                    //手机号码未注册过首媒 执行设置密码注册操作
                    val intent  =Intent(this, SettingLoginPsdActivity::class.java)
                    intent.putExtra("phone",phone)
                    startActivityForResult(intent, IntentCode.IS_REGIST)
                } else {
                    //手机号码注册过首媒直接存储信息
                    UserManager.setLogin(it)
                    startActivity(Intent(this@BindingPhoneActivity, HomeActivity::class.java))
                    setResult(IntentCode.IS_REGIST)
                    finish()
                }
            }
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_REGIST && resultCode == IntentCode.IS_REGIST) {
            setResult(IntentCode.IS_REGIST)
            finish()
        }
    }
}