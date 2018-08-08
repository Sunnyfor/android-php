package com.cocosh.shmstore.mine.ui

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.model.PayPassworType
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_reset_paypwd.*

/**
 * 重置密码
 * Created by zhangye on 2018/4/17.
 */
class ResetPayPasswordActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_reset_paypwd

    override fun initView() {
        titleManager.defaultTitle("重置登录密码")
        isvPhoto.setOnClickListener(this)
        isvOld.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            isvPhoto.id -> {
                //通过手机修改密码
                val dialog = SmediaDialog(this)
                dialog.setTitle("即将发送验证码到")
                dialog.setDesc(UserManager2.getCryptogramPhone() ?: "")
                dialog.OnClickListener = View.OnClickListener {
                    //跳转
                    SmApplication.getApp().activityName = this@ResetPayPasswordActivity::class.java as Class<BaseActivity>?
                    CheckPayPwdMessage.start(this@ResetPayPasswordActivity, SMSType.RESET_PAYPASS)
                }
                dialog.show()
            }

            isvOld.id -> {
                //通过旧密码修改密码
                SmApplication.getApp().activityName = this@ResetPayPasswordActivity::class.java as Class<BaseActivity>?
                ReInputPayPwdActivity.start(this, "oldPwd", "")
            }
        }
    }

    override fun reTryGetData() {

    }

    override fun onDestroy() {
        SmApplication.getApp().activityName = null
        super.onDestroy()
    }
}