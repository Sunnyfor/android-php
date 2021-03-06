package com.cocosh.shmstore.home

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_report.*

/**
 * 举报页面
 * Created by zhangye on 2018/6/12.
 */
class ReportActivity: BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_report

    override fun initView() {
        titleManager.defaultTitle("我有话要说")
        btnNext.setOnClickListener {
            commit()
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {

    }

    private fun commit() {
        if (edtDesc.text.trim().isEmpty()){
            ToastUtil.show("请填写内容")
            return
        }

        val params = hashMapOf<String, String>()
        params["conn_id"] = intent.getStringExtra("comment_id")?:""
        params["type"] = intent.getStringExtra("type")?:""
        params["content"] = edtDesc.text.toString()
        ApiManager2.post(this, params, Constant.EHOME_REPORT, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                finish()
                ToastUtil.show("提交成功")
            }


            override fun onCatch(data: BaseBean<String>) {
            }
        })

    }
}