package com.cocosh.shmstore.facilitator.ui

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
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.baiduScan.ScanLicenseActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.term.ServiceTermActivity
import com.cocosh.shmstore.utils.IntentCode
import kotlinx.android.synthetic.main.partner_splash_activity.*

/**
 * Created by cjl on 2018/2/1.
 */
class FacilitatorSplashActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    private var isArgeen = false
    override fun setLayout(): Int = R.layout.activity_facilitator_splash

    override fun initView() {
        titleManager.defaultTitle("服务商认证", "", 0, 0, null)
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
    }

    private fun setPartText() {
        val style = SpannableStringBuilder(resources.getString(R.string.facilitator_title))
//        style.setSpan(TextClick1(),34,43 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        style.setSpan(TextClick(this), 5, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        style.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 5, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

//        style.setSpan(ForegroundColorSpan(resources.getColor(R.color.new_red)), 34, 43, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //这个一定要记得设置，不然点击不生效

        tvLicense.movementMethod = LinkMovementMethod.getInstance()
//
        tvLicense.text = style
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.btn_ok -> {
                if (isArgeen) {
                    ScanLicenseActivity.loadActivity(this, 222)
                }
            }
            R.id.text_treaty2 -> startActivityForResult(Intent(this, ServiceTermActivity::class.java).putExtra("OPEN_TYPE", Constant.FACILITOTAAR_SERVICE_RULE), IntentCode.IS_TERM)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, FacilitatorSplashActivity::class.java))
        }
    }

    class TextClick(val activity: Activity) : ClickableSpan() {
        override fun onClick(widget: View) {
//            NuclearAgreementActivity.start(widget?.activity, "service")
//            SaleH5Activity.start(widget.activity,SaleH5Activity.OPERATOR,null)

            activity.startActivityForResult(Intent(widget.context, ServiceTermActivity::class.java), IntentCode.IS_TERM)
        }

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
            cbDesc.setChecked(true)
            isOk()
        }
    }

    /**
     * 校验提交条件
     */
    fun isOk() {
        if (isArgeen) {
            btn_ok.setBackgroundResource(R.color.red)
        } else {
            btn_ok.setBackgroundResource(R.color.grayBtn)
        }
    }
}