package com.cocosh.shmstore.newCertification.ui

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newCertification.adapter.SelectServiceAdapter
import com.cocosh.shmstore.newCertification.model.ApplyPartner
import com.cocosh.shmstore.newCertification.model.SelectServiceModel
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_select_service.*


/**
 * 选择服务商地址
 * Created by zhangye on 2018/4/2.
 */
class SelectServiceActivity : BaseActivity() {
    override fun reTryGetData() {
        getServiceList()
    }

    private var addressCode: String? = null
    private var isClick = false
    private var operatorId = "0"
    override fun setLayout(): Int = R.layout.activity_select_service

    override fun initView() {
        titleManager.defaultTitle("选择服务商")
        addressCode = intent.getStringExtra("addressCode")
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnSure.setOnClickListener(this)
        getServiceList()
    }

    override fun onListener(view: View) {
        when (view.id) {
            btnSure.id -> {
                if (isClick) {
                    commit()
                }
            }
        }
    }

    private fun getServiceList() {
        val params = HashMap<String, String>()
        addressCode?.let {
            params["areaCode"] = it
        }
        ApiManager.get(1,this, params, Constant.PARTNER_SERVICE_LIST, object : ApiManager.OnResult<BaseModel<ArrayList<SelectServiceModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<SelectServiceModel>>) {
                if (data.success) {
                    data.entity?.let {
                        val adapter = SelectServiceAdapter(it)
                        adapter.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(v: View, index: Int) {
                                adapter.index = index
                                operatorId = it[index].operatorId ?: "0"
                                adapter.notifyDataSetChanged()
                                isClick = true
                                btnSure.setBackgroundResource(R.color.red)
                            }
                        })
                        recyclerView.adapter = adapter
                    }

                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<ArrayList<SelectServiceModel>>) {

            }

        })
    }


    private fun commit() {
        val map = HashMap<String, String>()
        addressCode?.let {
            map["addressCode"] = it
        }
        map["operatorId"] = operatorId
        ApiManager.post(this, map, Constant.APPLY_PARTNER, object : ApiManager.OnResult<BaseModel<ApplyPartner>>() {
            override fun onSuccess(data: BaseModel<ApplyPartner>) {
                if (data.success) {
                    startActivity(Intent(this@SelectServiceActivity, PartherPendingPayActivity::class.java))
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())

            }

            override fun onCatch(data: BaseModel<ApplyPartner>) {

            }

        })
    }

}