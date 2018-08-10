package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.BankShowBean
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntBankShowPresenter
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_public_account_show.*
import java.util.regex.Pattern

/**
 * Created by lmg on 2018/3/14.
 * 对公账户
 */
class CorporateAccountShowActivty : BaseActivity(), EntCertificationContrat.IBankShowView {
    override fun reTryGetData() {
        presenter.getData(1)
    }

    var presenter = EntBankShowPresenter(this, this)

    override fun setData(data: BaseBean<BankShowBean>) {
//            initData(data.message)
    }

    override fun setLayout(): Int = R.layout.activity_public_account_show

    override fun initView() {
        titleManager.defaultTitle("企业对公账户")
        val filter2 = InputFilter { source, _, _, _, _, _ ->
            val speChat = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“'。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())
            if (matcher.find()) "" else null;
        }
        //保留布局内的过滤条件
        val mFilters = arrayOfNulls<InputFilter>(tvName.filters.size)
        tvName.filters.forEachIndexed<InputFilter?> { i: Int, inputFilter: InputFilter? ->
            if (inputFilter != null) {
                mFilters[i] = inputFilter
            }
        }
        mFilters[tvName.filters.size - 2] = filter2
        tvName.filters = mFilters

//        presenter.getData(1)
        val openType = intent.getIntExtra("type", -1)
        if (openType == 111) {
            btnChange.visibility = View.VISIBLE
        } else {
            btnChange.visibility = View.GONE
        }
        initListener()

        SmApplication.getApp().getData<EntActiveInfoModel>(DataCode.ENT_AUTHER_DATA,true)?.apply {
            tvName.setText(base.name)
            tvLayerName.setText(base.legal)
            edtBankAccount.setText(base.account)
            edtBankName.setText(base.bank)
            edtPhoneNumber.setText(base.tel)
            edtContacts.setText(base.linker)
        }
    }

    private fun initListener() {
        btnChange.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnChange.id -> {
                //重新填写
                showChangeDialog()
            }
            else -> {
            }
        }
    }

    companion object {
        fun start(context: Context, openType: Int,infoModel: EntActiveInfoModel?) {
            SmApplication.getApp().setData(DataCode.ENT_AUTHER_DATA,infoModel)
            context.startActivity(Intent(context, CorporateAccountShowActivty::class.java).putExtra("type", openType))
        }
    }

    private fun showChangeDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("重新填写对公账户信息")
        dialog.show()
        dialog.OnClickListener = View.OnClickListener {
            //对公账户填写页
            startActivity(Intent(this@CorporateAccountShowActivty, CorporateAccountActivty::class.java))
        }
    }
}