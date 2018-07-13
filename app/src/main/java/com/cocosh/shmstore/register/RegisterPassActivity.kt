package com.cocosh.shmstore.register

import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_register_psd.*


/**
 * 注册设置密码页面
 * Created by zhangye on 2018/1/25.
 */
class RegisterPassActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    var flag = false

    override fun setLayout(): Int = R.layout.activity_register_psd

    override fun initView() {
        titleManager.defaultTitle(resources.getString(R.string.settingPass))

        rlPsd.setOnClickListener(this)
        btComplete.setOnClickListener(this)

        etSetPsd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length > 5) {
                        flag = true
                        btComplete.setBackgroundResource(R.color.red)
                    } else {
                        btComplete.setBackgroundResource(R.color.grayBtn)
                        flag = false
                    }
                }
            }

        })
    }

    override fun onListener(view: View) {
        when (view.id) {

            rlPsd.id -> {
                if (itvHidePsd.visibility == View.VISIBLE) {
                    etSetPsd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    itvHidePsd.visibility = View.INVISIBLE
                    itvShowPsd.visibility = View.VISIBLE
                    etSetPsd.setSelection(etSetPsd.text.length)
                } else {
                    etSetPsd.transformationMethod = PasswordTransformationMethod.getInstance()
                    itvHidePsd.visibility = View.VISIBLE
                    itvShowPsd.visibility = View.INVISIBLE
                    etSetPsd.setSelection(etSetPsd.text.length)
                }
            }

            btComplete.id -> {  //提交按钮事件
                if (flag) {
                    val dialog = SmediaDialog(this)
                    dialog.setTitle(resources.getString(R.string.surePass))
                    dialog.setDesc(etSetPsd.text.toString())
                    dialog.OnClickListener = View.OnClickListener {
                        val params = HashMap<String, String>()
                        params["userName"] = intent.getStringExtra("phone")
                        params["userPwd"] = etSetPsd.text.toString()
                        params["token"] = intent.getStringExtra("token")
                        ApiManager.post(this, params, Constant.REGISTER_PHONE, object : ApiManager.OnResult<BaseModel<String>>() {
                            override fun onSuccess(data: BaseModel<String>) {
                                if (data.success) {
                                    setResult(IntentCode.IS_REGIST)
                                    finish()
                                } else {
                                    ToastUtil.show(data.message)
                                }
                            }

                            override fun onFailed(e: Throwable) {

                            }

                            override fun onCatch(data: BaseModel<String>) {

                            }

                        })
                    }
                    dialog.show()
                }
            }
        }
    }
}