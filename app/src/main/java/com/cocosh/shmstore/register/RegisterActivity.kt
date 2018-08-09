package com.cocosh.shmstore.register

import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CompoundButton
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.model.ValueByKey
import com.cocosh.shmstore.register.presenter.RegisterPresenter
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.web.WebViewActivity
import kotlinx.android.synthetic.main.activity_register.*


/**
 * 注册页面
 * Created by zhangye on 2018/1/24.
 */
class RegisterActivity : BaseActivity(), RegisterContract.IView {
    override fun reTryGetData() {

    }

    private var phoneOk = false
    private var codeOk = false
    private var isArgeen = false
    private var passwordOk = false
    private var surePasswordOk = false

    private val presenter = RegisterPresenter(this, this)
    private var phone = ""

    override fun setLayout(): Int = R.layout.activity_register

    override fun initView() {

        titleManager.defaultTitle("注册")

        edtPhone.requestFocus()

        edtPhone.postDelayed({
            showKeyboard(edtPhone)
        }, 500)


        val style = SpannableStringBuilder(tvDesc.text)
        style.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View?) {
                intentAgreement()
//                startActivityForResult(Intent(this@RegisterActivity, ServiceTermActivity::class.java), IntentCode.IS_TERM)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }

        }
                , 7, tvDesc.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)  //设置点击位置


        style.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)),
                7, tvDesc.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)  //设置位置颜色


        tvDesc.movementMethod = LinkMovementMethod.getInstance()  //设置点击事件
        tvDesc.highlightColor = Color.TRANSPARENT
        tvDesc.text = style


        /**
         * 电话文本逻辑
         */
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

        /**
         * 验证码文本逻辑
         */
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
         * 密码文本逻辑
         */
        edtPassWord.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordOk = s?.length ?: 0 >= 6
                isOk()
            }

        })

        /**
         * 确认密码文本逻辑
         */
        editSurePassWord.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                surePasswordOk = s?.length ?: 0 >= 6
                isOk()
            }

        })

        /**
         * 验证码按钮事件
         */
        btnCode.setCallListener(View.OnClickListener {
            phone = edtPhone.text.toString()
            presenter.sendCode(phone)
        })


        /**
         * 协议按钮
         */
        tvChecked.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            isArgeen = isChecked
            isOk()
        })


        btnNext.setOnClickListener(this)

    }

    override fun onListener(view: View) {
        when (view.id) {
        //下一步校验验证码
            btnNext.id -> {

                if (!isArgeen) {
                    ToastUtil.show("须同意注册协议后再进行注册")
                    return
                }
                if (edtPassWord.text.toString() == editSurePassWord.text.toString()) {
                    //调用注册请求
                    presenter.register(edtPhone.text.toString(), edtPassWord.text.toString(), edtCode.text.toString())
                } else {
                    ToastUtil.show(getString(R.string.input_pwd_error))
                }
            }
        }
    }

    /**
     * 校验提交条件
     */
    fun isOk() {
        if (phoneOk && codeOk && passwordOk && surePasswordOk) {
            btnNext.isClickable = true
            btnNext.setBackgroundResource(R.color.red)
        } else {
            btnNext.isClickable = false
            btnNext.setBackgroundResource(R.color.grayBtn)
        }
    }


    override fun onCodeResult(data: BaseBean<SMS>) {
        btnCode.action() //验证码发送成功执行计时
    }

    override fun onRegister(result: BaseBean<Login2>) {
        setResult(IntentCode.FINISH)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //注册成功关闭本页面
        if (requestCode == IntentCode.IS_REGIST && resultCode == IntentCode.IS_REGIST) {
            finish()
            return
        }
        //修改协议按钮状态
        if (requestCode == IntentCode.IS_TERM && resultCode == IntentCode.IS_TERM) {
            isArgeen = true
            tvChecked.setChecked(true)
            isOk()
        }
    }


    fun intentAgreement() {
        val params = hashMapOf<String, String>()
        params["dictionaryKey"] = "yonghuzhucexieyi"
        ApiManager.get(this, params, Constant.GET_SHARE_URL, object : ApiManager.OnResult<BaseModel<ValueByKey>>() {
            override fun onSuccess(data: BaseModel<ValueByKey>) {
                if (data.success) {
                    val intent = Intent(this@RegisterActivity, WebViewActivity::class.java)
                    intent.putExtra("title", "注册协议")
                    intent.putExtra("url", data.entity?.dictionaryValue)
                    intent.putExtra("showButton", true)
                    startActivityForResult(intent, IntentCode.IS_TERM)
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<ValueByKey>) {
            }
        })

    }
}
