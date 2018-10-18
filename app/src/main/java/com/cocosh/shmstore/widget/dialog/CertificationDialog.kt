package com.cocosh.shmstore.widget.dialog

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.dialog_certification_main.*

/**
 * 首媒默认样式的对话框
 * Created by zhangye on 2018/1/27.
 */
class CertificationDialog : SmediaDialog {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)

    init {
        setContentView(R.layout.dialog_certification_main)
    }


    override fun setDesc(desc: String) {
        tv_desc.text = Html.fromHtml(desc)
    }

    fun show(invitee:Login2.Invitee){
        if (invitee.code != null) {
            val type = invitee.type

            if (type == "x") {
                if (UserManager2.getCommonData()?.cert?.x == AuthenStatus.NEW_MATCHMAKER_OK.type){
                    return
                }
                //新媒人
                setDesc("您接受了<br>${invitee.inviter}<br>的<font color='#D8253B'>新媒人认证</font>邀请")
            } else {
                if (UserManager2.getCommonData()?.cert?.b == AuthenStatus.BUSINESS_OK.type || UserManager2.getCommonData()?.cert?.b == AuthenStatus.BUSINESS_WAIT.type){
                    return
                }
                //服务商
                setDesc("您接受了<br>${invitee.inviter}<br>的<font color='#D8253B'>企业主认证</font>邀请")
            }

            btn_certification.setOnClickListener {
                if (type == "x") {
                    //新媒人
                    context.startActivity(Intent(context, AuthActivity::class.java).putExtra("type","NEW_MATCHMAKER"))
                } else {
                    //服务商
                    context.startActivity(Intent(context, AuthActivity::class.java).putExtra("type", "BUSINESS"))
                }
                dismiss()
            }

            tvCancel.setOnClickListener {
                dismiss()
            }

            show()
        }
    }
}
