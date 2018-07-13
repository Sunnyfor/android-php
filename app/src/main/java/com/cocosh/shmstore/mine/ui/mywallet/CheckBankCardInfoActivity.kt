package com.cocosh.shmstore.mine.ui.mywallet

import android.app.Activity
import android.content.Intent
import android.text.style.ClickableSpan
import android.view.View
import android.widget.CompoundButton
import com.cocosh.shmstore.R
import com.cocosh.shmstore.baiduScan.ScanLicenseActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.term.ServiceTermActivity
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.PickerViewUtils
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_check_bankcard_info.*

/**
 * Created by lmg on 2018/4/16.
 */
class CheckBankCardInfoActivity : BaseActivity() {
    private var isArgeen = false
    private lateinit var pickerViewUtils: PickerViewUtils

    override fun initView() {
        pickerViewUtils = PickerViewUtils(this)
        titleManager.defaultTitle("核对银行卡信息")
        val bankNumber = intent.getStringExtra("bankNumber")
        val bankType = intent.getStringExtra("bankType")
        val bankName = intent.getStringExtra("bankName")
        edtBankType.setText(bankName)
        edtAccount.setText(bankNumber)
        edtBankType.setOnClickListener(this)
        text_treaty2.setOnClickListener(this)
        phoneDialog.setOnClickListener(this)
        nameDialog.setOnClickListener(this)
        /**
         * 协议按钮
         */
        cbDesc.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            isArgeen = isChecked
            isOk()
        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.btn_ok -> {
                if (isArgeen) {
                    ScanLicenseActivity.loadActivity(this, 222)
                }
            }
            R.id.text_treaty2 -> startActivityForResult(Intent(this, ServiceTermActivity::class.java).putExtra("OPEN_TYPE", Constant.ADD_CARD_RULE), IntentCode.IS_TERM)
            edtBankType.id -> {
//                pickerViewUtils.showBankType(object : PickerViewUtils.OnPickerViewResultListener {
//                    override fun onPickerViewResult(value: String) {
//
//                    }
//                })
            }
            phoneDialog.id -> {
                val desc = "银行预留的手机号是办理该银行卡时所填写的手机号码。\n" +
                        "\n" +
                        "没有预留、手机号忘记或者已停用，请联系银行客服更新处理。"
                val title = "手机号说明"
                showDescDialog(desc, title)
            }
            nameDialog.id -> {
                val desc = "为了您的账户资金安全，只能绑定持卡人本人的银行卡。"
                val title = "持卡人说明"
                showDescDialog(desc, title)
            }
        }
    }


    override fun reTryGetData() {
    }

    override fun setLayout(): Int = R.layout.activity_check_bankcard_info

    class TextClick(val activity: Activity) : ClickableSpan() {
        override fun onClick(widget: View) {
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
//            btn_ok.setBackgroundResource(R.color.red)
        } else {
//            btn_ok.setBackgroundResource(R.color.grayBtn)
        }
    }

    private fun showDescDialog(desc: String, title: String) {
        val dialog = SmediaDialog(this)
        dialog.singleButton()
        dialog.setTitle(title)
        dialog.setDesc(desc)
        dialog.setPositiveText("知道了")
        dialog.show()
    }

    fun shoErrorDialog(title: String) {
        val dialog = SmediaDialog(this)
        dialog.singleButton()
        dialog.setTitle(title)
        dialog.show()
    }
}