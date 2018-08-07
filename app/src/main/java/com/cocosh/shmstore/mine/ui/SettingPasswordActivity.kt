package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.ResetPass
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_setting_password.*


/**
 * 设置新密码页面
 * Created by zhangye on 2018/4/17.
 */
class SettingPasswordActivity : BaseActivity() {

    override fun setLayout(): Int = R.layout.activity_setting_password

    override fun initView() {
        titleManager.defaultTitle("修改登录密码")
        rlPsd.setOnClickListener(this)
        rlPsdTwo.setOnClickListener(this)
        btnSure.setOnClickListener(this)
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

                motifyPasswod()
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
            SmApplication.getApp().removeData(DataCode.RESETPASS)
            startActivity(Intent(this, SettingActivity::class.java))
        }
        smediaDialog.show()
    }


    private fun motifyPasswod() {
        val params = HashMap<String, String>()
        SmApplication.getApp().getData<ResetPass>(DataCode.RESETPASS, false)?.apply {
            params["ticket"] = ticket
            params["ts"] = ts
            params["pwd"] = edtPasswordOne.text.toString()
        }

        ApiManager2.post(this, params, Constant.PASS_SET, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                showMotifyDialog()
                tvDesc1.text = ""
                tvDesc2.text = ""
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }


    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, SettingPasswordActivity::class.java))
        }
    }
}