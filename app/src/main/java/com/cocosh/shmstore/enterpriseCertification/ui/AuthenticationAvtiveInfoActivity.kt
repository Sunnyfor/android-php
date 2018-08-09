package com.cocosh.shmstore.enterpriseCertification.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntCertificationActiveContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveShowData
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntCertificationActiveShowPresenter
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_ent_active_info.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lmg on 2018/4/3.
 */
class AuthenticationAvtiveInfoActivity : BaseActivity(), EntCertificationActiveContrat.IShowView {
    override fun reTryGetData() {
        presenter.getShowData(1)
    }

    val presenter = EntCertificationActiveShowPresenter(this, this)
    override fun setShowData(result: BaseModel<EntActiveShowData>) {
        if (result.success && result.code == 200) {
            updateView(result.entity)
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun setLayout(): Int = R.layout.activity_ent_active_info

    override fun initView() {
        titleManager.defaultTitle("认证信息")
        presenter.getShowData(1)
    }

    private fun updateView(data: EntActiveShowData?) {
//        if (data?.status == AuthenStatus.BUSINESS_EXAMINE.type) {
//            isvEntStatus.setNoIconValue("审核中")
//        } else if (data?.status == AuthenStatus.ALREADY_ACTIVATED.type) {
//            isvEntStatus.setNoIconValue("已认证")
//        } else {
//            isvEntStatus.setNoIconValue("审核失败")
//        }
        isvEntTime.setNoIconValue(data?.authTime)
        isvEntNo.setNoIconValue(data?.smCode)
        isvEntAccount.setNoIconValue(data?.phone)
    }

    override fun onListener(view: View) {
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    fun getDateTimeFromMillisecond(millisecond: String?): String {
        var time = ""
        try {
            val longTime = millisecond?.toLong()
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = Date(longTime!!)
            time = simpleDateFormat.format(date)
        } catch (e: Exception) {
        }
        return time
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, AuthenticationAvtiveInfoActivity::class.java))
        }
    }
}