package com.cocosh.shmstore.mine.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.ResetPass
import com.cocosh.shmstore.utils.DigestUtils
import com.cocosh.shmstore.utils.PermissionCode
import com.cocosh.shmstore.utils.PermissionUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_old_modify_pass.*


/**
 * 通过旧密码修改登录密码
 * Created by zhangye on 2018/4/17.
 */
class OldModifyPasswordActivity : BaseActivity() {

    var isSure = false
    private lateinit var permissionUtil: PermissionUtil

    override fun setLayout(): Int = R.layout.activity_old_modify_pass
    override fun initView() {
        titleManager.defaultTitle("修改登录密码")
        permissionUtil = PermissionUtil(this)

        checkbox.isAuto = false
        checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                edtPassword.setSelection(edtPassword.text.length)
                checkbox.text = getText(R.string.iconShow)
            } else {
                edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                edtPassword.setSelection(edtPassword.text.length)
                checkbox.text = getText(R.string.iconHiden)
            }
        })

        //确定按钮
        btnSure.setOnClickListener(this)
        //忘记密码
        tvPassForget.setOnClickListener(this)
        //联系客服
        btnCall.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                if (edtPassword.text.length < 6) {
                    tvDesc1.text = "您输入的密码不符合规定"
                    return
                }
                tvDesc1.text = ""
                checkedOldPassword()
            }
            tvPassForget.id -> {
                //通过手机修改密码
                    val dialog = SmediaDialog(this)
                    dialog.setTitle("即将发送验证码到")
                    dialog.OnClickListener = View.OnClickListener {
                        startActivity(Intent(this, ResetPassCodeActivity::class.java))
                        finish()
                    }
                    dialog.setDesc(UserManager.getCryptogramPhone()?:"")
                    dialog.show()
            }
            btnCall.id -> {
                val dialog = SmediaDialog(this)
                dialog.setTitle("首媒客服：01078334322")
                dialog.setPositiveText("拨打")
                dialog.OnClickListener = View.OnClickListener {
                    if (permissionUtil.callPermission()) {
                        callPhone()
                    }
                }
                dialog.show()
            }
        }
    }

    override fun reTryGetData() {
    }

    private fun checkedOldPassword() {
            val params = hashMapOf<String, String>()
            params["oldpwd"] = DigestUtils.md5(edtPassword.text.toString())
            ApiManager2.post(this, params, Constant.OLDCHECK, object : ApiManager2.OnResult<BaseBean<ResetPass>>() {
                override fun onSuccess(data: BaseBean<ResetPass>) {
                    val intent = Intent(this@OldModifyPasswordActivity, SettingPasswordActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailed(code: String, message: String) {
                    //密码错误显示忘记密码
                    if (code == "400"){
                        tvPassForget.visibility = View.VISIBLE
                    }else{
                        tvPassForget.visibility = View.GONE
                    }
                }

                override fun onCatch(data: BaseBean<ResetPass>) {

                }

//                override fun onSuccess(data: BaseModel<String>) {
//                    if (data.success) {
//
//                    } else {
//                        if (data.code == 3044) { //密码错误显示忘记密码
//                            tvPassForget.visibility = View.VISIBLE
//                        } else {
//                            tvPassForget.visibility = View.GONE
//                        }
//
//                        if (data.code == 3043) { //密码错误次数上限 显示联系客服
//                            btnSure.visibility = View.GONE
//                            btnCall.visibility = View.VISIBLE
//                        } else {
//                            btnCall.visibility = View.GONE
//                            btnSure.visibility = View.VISIBLE
//                        }
//
//                        tvDesc1.text = data.message
//                    }
//                }
//
//                override fun onFailed(e: Throwable) {
//
//                }
//
//                override fun onCatch(data: BaseModel<String>) {
//
//                }

            })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCode.PHONE.type) {
            if (permissionUtil.checkPermission(permissions)) {
                callPhone()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:01078334322"))
        startActivity(intent)
    }
}