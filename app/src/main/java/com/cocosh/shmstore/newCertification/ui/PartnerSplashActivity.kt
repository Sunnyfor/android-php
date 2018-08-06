package com.cocosh.shmstore.newCertification.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CompoundButton
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newCertification.model.ApplyPartner
import com.cocosh.shmstore.term.ServiceTermActivity
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.OpenType
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.partner_splash_activity.*

/**
 *
 * Created by cjl on 2018/2/1.
 */
class PartnerSplashActivity : BaseActivity() {
    override fun reTryGetData() {
        getCode()
    }

    private var isArgeen = false
    override fun setLayout(): Int = R.layout.partner_splash_activity
    private var inviteCode: String? = null

    override fun initView() {
        titleManager.defaultTitle(getString(R.string.newAuthen), "", 0, 0, null)
        btn_ok.setOnClickListener(this)
        text_treaty2.setOnClickListener(this)
        setPartText()
        /**
         * 协议按钮
         */
        cbDesc.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            isArgeen = isChecked
            isOk()
        })

        getCode()
    }

    private fun setPartText() {
        val style = SpannableStringBuilder(resources.getString(R.string.partner_title))
        style.setSpan(TextClick(this), 5, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        style.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 5, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvLicense.movementMethod = LinkMovementMethod.getInstance()
        tvLicense.text = style
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.btn_ok -> {
                if (isArgeen) {
                    if (inviteCode.isNullOrEmpty()) {
                        if (!edtInviteCode.text.toString().isEmpty()) {
                            authen(edtInviteCode.text.toString())
                        } else {
                            //跳转
                            startActivityForResult(Intent(this, CertificationAddressActivity::class.java), IntentCode.FINISH)
                        }
                    } else {
                        inviteCode?.let {
                            authen(it)
                        }
                    }
                }
            }
            R.id.text_treaty2 -> startActivityForResult(Intent(this, ServiceTermActivity::class.java).putExtra("OPEN_TYPE", OpenType.Cer.name), IntentCode.IS_TERM)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PartnerSplashActivity::class.java))
        }
    }

    class TextClick(val activity: Activity) : ClickableSpan() {
        override fun onClick(widget: View) {
            activity.startActivityForResult(Intent(widget.context, ServiceTermActivity::class.java).putExtra("OPEN_TYPE", OpenType.Cer.name), IntentCode.IS_TERM)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //修改协议按钮状态
        if (requestCode == IntentCode.IS_TERM && resultCode == IntentCode.IS_TERM) {
            isArgeen = true
            cbDesc.setChecked(true)
            isOk()
        }
    }

    /**
     * 校验提交条件
     */
    private fun isOk() {
        if (isArgeen) {
            btn_ok.setBackgroundResource(R.color.red)
        } else {
            btn_ok.setBackgroundResource(R.color.grayBtn)
        }
    }

    /**
     * 获取邀请码
     */
    fun getCode() {
        ApiManager.get(1, this, hashMapOf(), Constant.PARTNER_INVITE_CODE, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success) {
                    data.entity?.let {
                        if (it.isNotEmpty()) {
                            inviteCode = it
                            tvInviteCode.text = inviteCode
                        } else {
                            tvInviteCode.visibility = View.GONE
                            edtInviteCode.visibility = View.VISIBLE
                        }
                    }
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<String>) {

            }

        })
    }

    /**
     * 开始认证
     */
    private fun authen(inviteCode: String) {
        val params = hashMapOf<String, String>()
        params["beInviteCode"] = inviteCode
        ApiManager.post(this, params, Constant.APPLY_PARTNER, object : ApiManager.OnResult<BaseModel<ApplyPartner>>() {
            override fun onSuccess(data: BaseModel<ApplyPartner>) {
                if (data.success) {
                    val intent = Intent(this@PartnerSplashActivity, PartherPendingPayActivity::class.java)
                    startActivity(intent)
                } else {
                    val dialog = SmediaDialog(this@PartnerSplashActivity)
                    dialog.setTitle(data.message)
                    dialog.singleButton()
                    dialog.show()
                }

            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<ApplyPartner>) {

            }

        })
    }
}