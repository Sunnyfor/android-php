package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_reset_password.*

/**
 * 重置密码
 * Created by zhangye on 2018/4/17.
 */
class ResetPasswordActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_reset_password

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
                    dialog.OnClickListener = View.OnClickListener {
                        startActivity(Intent(this, ResetPassCodeActivity::class.java))
                    }
                    dialog.setDesc(UserManager.getCryptogramPhone()?:"")
                    dialog.show()
            }

            isvOld.id -> {
                //通过旧密码修改密码
                startActivity(Intent(this, OldModifyPasswordActivity::class.java))
            }
        }
    }

    override fun reTryGetData() {

    }

}