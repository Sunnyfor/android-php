package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.activity_single_edit.*


/**
 * 单输入框页面
 * Created by zhangye on 2018/4/11.
 */
class SingleEditActivity : BaseActivity() {

    var value: String? = null
    var title: String? = null
    var edititable: Editable? = null
    override fun reTryGetData() {
    }

    override fun setLayout(): Int = R.layout.activity_single_edit

    override fun initView() {

        title = intent.getStringExtra("title")
        title?.let {
            titleManager.rightText(it, "保存", View.OnClickListener {
                if (title == "公司名称") {
                    edititable = edtCompany.text
                }
                if (title == "昵称") {
                    edititable = edtName.text
                }
                edititable.toString().let {
                    if (it.isNotEmpty()) {
                        update(title!!, it)
                    } else {
                        ToastUtil.show("内容不能为空")
                    }
                }
            }).setRightColor(R.color.red)
        }


        if (title == "公司名称") {
            tvName.visibility = View.VISIBLE
            edtName.visibility = View.GONE
            edtCompany.visibility = View.VISIBLE
            edtCompany.hint = "请输入企业名称"
        }
        if (title == "昵称") {
            edtName.hint = "请输入昵称"
            edtName.visibility = View.VISIBLE
            edtCompany.visibility = View.GONE
        }

        value = intent.getStringExtra("value")

        if (value != null) {
            if (title == "公司名称") {
                edtCompany.setText(value)
            }
            if (title == "昵称") {
                edtName.setText(value)

            }
        }
        edtName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

    }

    override fun onListener(view: View) {
    }


    private fun update(type: String, value: String) {

        if (value == this.value) {
            finish()
            return
        }

        val params = hashMapOf<String, String>()
        when (type) {
            "昵称" -> params["nickname"] = value
            "公司名称" -> params["company"] = value
        }

        UserManager2.updateMemberEntrance(this, params, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                val intent = Intent()
                intent.putExtra("title", title)
                intent.putExtra("value", value)

                val memberEntrance2 = UserManager2.getMemberEntrance()
                when (type) {
                    "昵称" -> memberEntrance2?.nickname = value
                    "公司名称" -> memberEntrance2?.company = value
                }
                UserManager2.setMemberEntrance(memberEntrance2)
                setResult(IntentCode.IS_INPUT, intent)
                finish()
            }


            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}