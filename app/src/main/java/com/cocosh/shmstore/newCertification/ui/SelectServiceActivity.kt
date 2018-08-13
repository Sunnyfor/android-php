package com.cocosh.shmstore.newCertification.ui

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.Ethnic
import com.cocosh.shmstore.mine.ui.AuthActivity
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

    private var prov = 0
    private var city = 0
    private var isClick = false
    private var operatorId = "0"
    override fun setLayout(): Int = R.layout.activity_select_service

    override fun initView() {
        titleManager.defaultTitle("选择服务商")
        prov = intent.getIntExtra("prov",0)
        city = intent.getIntExtra("city",0)
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
            params["prov"] = prov.toString()
            params["city"] = city.toString()

        ApiManager2.post(1,this, params, Constant.SERVICE_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Ethnic>>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<ArrayList<Ethnic>>) {
                data.message?.let {
                    val adapter = SelectServiceAdapter(it)
                    adapter.setOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClick(v: View, index: Int) {
                            adapter.index = index
                            operatorId = it[index].id
                            adapter.notifyDataSetChanged()
                            isClick = true
                            btnSure.setBackgroundResource(R.color.red)
                        }
                    })
                    recyclerView.adapter = adapter
                }
            }



            override fun onCatch(data: BaseBean<ArrayList<Ethnic>>) {

            }

        })
    }


    private fun commit() {
        val map = HashMap<String, String>()
        map["svcid"] = operatorId
        ApiManager2.post(this, map, Constant.NEW_CERT_DO, object : ApiManager2.OnResult<BaseBean<ApplyPartner>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<ApplyPartner>) {

            }

            override fun onSuccess(data: BaseBean<ApplyPartner>) {
                val intent = Intent(this@SelectServiceActivity,AuthActivity::class.java)
                intent.putExtra("type","x")
                startActivity(intent)
            }
        })
    }

}