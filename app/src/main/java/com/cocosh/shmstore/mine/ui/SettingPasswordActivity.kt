package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_setting_password.*


/**
 * 设置新密码页面
 * Created by zhangye on 2018/4/17.
 */
class SettingPasswordActivity : BaseActivity() {

    var type = 0

    override fun setLayout(): Int = R.layout.activity_setting_password

    override fun initView() {
        titleManager.defaultTitle("修改登录密码")
        rlPsd.setOnClickListener(this)
        rlPsdTwo.setOnClickListener(this)
        btnSure.setOnClickListener(this)

        type = intent.getIntExtra("type", 0)
    }

    override fun onClick(view: View) {
        when (view.id) {
            rlPsd.id -> {
                if (itvHidePsd.visibility == View.VISIBLE) {
                    itvShowPsd.visibility = View.VISIBLE
                    itvHidePsd.visibility = View.INVISIBLE
                    edtPasswordOne.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    itvHidePsd.visibility = View.VISIBLE
                    itvShowPsd.visibility = View.INVISIBLE
                    edtPasswordOne.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }

            rlPsdTwo.id -> {
                if (itvHidePsdTwo.visibility == View.VISIBLE) {
                    itvShowPsdTwo.visibility = View.VISIBLE
                    itvHidePsdTwo.visibility = View.INVISIBLE
                    edtPasswordTwo.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    itvHidePsdTwo.visibility = View.VISIBLE
                    itvShowPsdTwo.visibility = View.INVISIBLE
                    edtPasswordTwo.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }

            btnSure.id -> {
                if (edtPasswordOne.text.isEmpty() || edtPasswordOne.text.length < 6) {
                    tvDesc2.text = ""
                    tvDesc1.text = "您输入的密码不符合规定"
                    return
                }

                if (edtPasswordTwo.text.isEmpty() || edtPasswordTwo.text.length < 6) {
                    tvDesc1.text = ""
                    tvDesc2.text = "您输入的密码不符合规定"
                    return
                }


                if (edtPasswordOne.text.toString() != edtPasswordTwo.text.toString()) {
                    ToastUtil.show("两次密码不一致")
                    tvDesc1.text = ""
                    tvDesc2.text = ("您两次输入的密码不一致，请点击 ${getString(R.string.iconHiden)}显示密码核对后再次设置")
                    return
                }

                tvDesc1.text = ""
                tvDesc2.text = ""

                if (type == 0) {
                    oldMotifyPass() //通过旧密码修改
                }

                if (type == 1) {
                    smsMotifyPass() //通过短信修改
                }

            }
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    fun showMotifyDialog() {
        val smediaDialog = SmediaDialog(this)
        smediaDialog.setTitle("密码修改成功")
        smediaDialog.singleButton()
        smediaDialog.OnClickListener = View.OnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        smediaDialog.show()
    }

    /**
     * 短信修改密码弹窗
     */
    private fun smsMotifyPass() {
        UserManager.getPhone()?.let {
            SmediaDialog(this).showSmsMotifyPassword(
                    it,
                    edtPasswordTwo.text.toString(),
                    intent.getStringExtra("token"),
                    object : ApiManager.OnResult<BaseModel<String>>() {
                        override fun onSuccess(data: BaseModel<String>) {
                            if (data.success) {
                                showMotifyDialog()
                            } else {
                                tvDesc1.text = ""
                                tvDesc2.text = data.message
                            }
                        }

                        override fun onFailed(e: Throwable) {
                        }

                        override fun onCatch(data: BaseModel<String>) {
                        }

                    })
        }
    }

    private fun oldMotifyPass() {
        UserManager.getPhone()?.let {
            val params = HashMap<String, String>()
            params["userName"] = it
            params["newUserPwd"] = edtPasswordOne.text.toString()
            params["newUserPwdTwo"] = edtPasswordTwo.text.toString()

            ApiManager.post(this, params, Constant.MOTIFY_PASSWORD, object : ApiManager.OnResult<BaseModel<String>>() {
                override fun onSuccess(data: BaseModel<String>) {
                    if (data.success) {
                        showMotifyDialog()
                    } else {
                        tvDesc1.text = ""
                        tvDesc2.text = data.message
                    }
                }

                override fun onFailed(e: Throwable) {

                }

                override fun onCatch(data: BaseModel<String>) {

                }

            })

        }
    }


    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, SettingPasswordActivity::class.java))
        }
    }
}