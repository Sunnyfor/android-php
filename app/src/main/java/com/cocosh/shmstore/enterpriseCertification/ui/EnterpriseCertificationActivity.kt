package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.InviteCodeModel
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntCertificationPresenter
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_enterprise_certification.*

/**
 * 企业主认证入口
 * Created by zhangye on 2018/3/23.
 */
class EnterpriseCertificationActivity : BaseActivity(), EntCertificationContrat.IView {
    override fun reTryGetData() {
        presenter.getCodeData(1)
    }

    var presenter = EntCertificationPresenter(this, this)
    override fun setCodeData(result: BaseBean<InviteCodeModel>) {

        result.message?.let {
            edtCode.keyListener = null
            edtCode.setText(it.code)
        }
//        if (!TextUtils.isEmpty(result.entity?.invitedCode)) {
//            edtCode.keyListener = null
//            edtCode.setText(result.entity?.invitedCode)
//            if (!TextUtils.isEmpty(result.entity?.entName)) {
//                edtName.setText(result.entity?.entName)
//            }
//        }
    }

    override fun setData(result: BaseBean<String>) {
        startActivity(Intent(this, EnterpriseCertiSuccessActivity::class.java))
        finish()
    }

    override fun setLayout(): Int = R.layout.activity_enterprise_certification

    override fun initView() {
        presenter.getCodeData(1)
        titleManager.defaultTitle("企业主认证")
        btAuthentication.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btAuthentication.id -> {

                if (edtName.text.isEmpty()) {
                    ToastUtil.show("请填写公司名称！")
                    return
                }

//                if (edtCode.text.isEmpty()) {
//                    ToastUtil.show("请填写邀请码！")
//                    return
//                }
                presenter.pushData(edtName.text.toString(), edtCode.text.toString())
            }
        }
    }

    private fun showErrorDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("认证失败")
        dialog.setDesc("该企业已被认证请重新填写！")
        dialog.singleButton()
        dialog.show()
    }

    private fun showMessage(message: String?) {
        val dialog = SmediaDialog(this)
        dialog.setTitle(message)
        dialog.singleButton()
        dialog.show()
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, EnterpriseCertificationActivity::class.java))
        }
    }
}