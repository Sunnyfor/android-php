package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import com.cocosh.shmstore.R
import com.cocosh.shmstore.about.AboutActivity
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.PayPassworType
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * 设置页面
 * Created by zhangye on 2018/4/8.
 */
class SettingActivity : BaseActivity() {
    private var fileUtlis = FileUtlis()
    private var catchSize = fileUtlis.getFileSize()
    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_setting

    override fun initView() {
        titleManager.defaultTitle("设置")
        btnLoginOut.setOnClickListener(this)
        isvAbout.setOnClickListener(this)
        resetPayPwd.setOnClickListener(this)
        isvCache.setOnClickListener(this)
        isvResetLoginPass.setOnClickListener(this)
        isvNewMessage.setOnClickListener(this)
        rlHead.setOnClickListener(this)
        isvCache.setNoIconValue(catchSize)
        isvPhoto.setNoIconValue(UserManager2.getLogin()?.phone)

        UserManager2.getMemberEntrance()?.let {
            GlideUtils.loadHead(this, it.avatar, ivHead)
            tvName.text = it.nickname

        }
        tvNo.text = (getString(R.string.no) + UserManager2.getLogin()?.code)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnLoginOut.id -> {
                val dialog = SmediaDialog(this)
                dialog.setTitle("是否要退出当前账号")
                dialog.OnClickListener = OnClickListener {
                    logout()
                }
                dialog.show()
            }
            isvAbout.id -> {
                startActivityForResult(Intent(this, AboutActivity::class.java), IntentCode.FINISH)
            }
            resetPayPwd.id -> {
                //判断是否设置支付密码
                if (UserManager2.getCommonData()?.paypass == 1) {
                    startActivity(Intent(this, ResetPayPasswordActivity::class.java))
                } else {
                    showEntDialog()
                }
            }
            isvResetLoginPass.id -> {
                startActivity(Intent(this, ResetPasswordActivity::class.java))
            }
            isvCache.id -> {
                if (catchSize == "0B") {
                    return
                }
                val dialog = SmediaDialog(this)
                dialog.setTitle("您确定要清除缓存吗?")
                dialog.OnClickListener = OnClickListener {
                    launch(UI) {
                        showLoading()
                        fileUtlis.deleteAllFile()
                        delay(1000)
                        hideLoading()
                        ToastUtil.show("清理完成!")
                        catchSize = fileUtlis.getFileSize()
                        isvCache.setNoIconValue(catchSize)
                    }
                }
                dialog.show()
            }
            isvNewMessage.id -> {
                startActivity(Intent(this, NewMessageNotifyActivity::class.java))
            }
            rlHead.id -> {
                startActivity(Intent(this, ArchiveActivity::class.java))
            }
        }
    }

    private fun showEntDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您未设置过支付密码，设置前将验证您的身份，即将发送验证码到" + UserManager2.getCryptogramPhone())
        dialog.OnClickListener = OnClickListener {
            SmApplication.getApp().activityName = this::class.java as Class<BaseActivity>?
            CheckPayPwdMessage.start(this@SettingActivity, SMSType.INIT_PAYPASS)
        }
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.FINISH && resultCode == IntentCode.FINISH) {
            finish()
        }
    }

    override fun onDestroy() {
        SmApplication.getApp().activityName = null
        super.onDestroy()
    }

    private fun logout() {
        val params = HashMap<String, String>()
        params["sid"] = UserManager2.getLogin()?.sid ?: ""
        ApiManager2.post(this, params, Constant.LOGOUT, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                UserManager2.setEmptyUser()
                finish()
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}