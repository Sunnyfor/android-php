package com.cocosh.shmstore.mine.ui.authentication

import android.content.Context
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
import com.cocosh.shmstore.mine.adapter.CommonListAdapter
import com.cocosh.shmstore.mine.model.CommonModel
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_comm_list.*

/**
 * Created by lmg on 2018/4/19.
 * 新媒人 服务商人员列表
 */
class CommListActivity : BaseActivity() {
    /**
     * CERTIFICATION_0 拓展企业主
     * CERTIFICATION_1 拓展个人用户
     * CERTIFICATION_0 平台分配个人用户
     * FACILITTOR_0 拓展新媒人
     * FACILITTOR_1 拓展企业主
     * FACILITTOR_2 拓展个人用户
     * FACILITTOR_3 平台分配企业主
     * FACILITTOR_4 平台分配个人用户
     */
    private var typeList = ""
    private var dataType = 0
    private var list = arrayListOf<CommonModel>()
    private lateinit var adapter: CommonListAdapter
    override fun setLayout(): Int = R.layout.activity_comm_list

    override fun initView() {
        typeList = intent.getStringExtra("typeList")
        when (typeList) {
            CommonType.CERTIFICATION_0.type -> {
                titleManager.defaultTitle("拓展企业主")
                getFacList(1, Constant.MYSELF_MATCHMAKER_EXPAND_ENTERPRISE)
            }
            CommonType.CERTIFICATION_1.type -> {
                titleManager.defaultTitle("拓展个人用户")
                getFacList(1, Constant.MYSELF_MATCHMAKER_EXPAND_PERSONAL)
            }
            CommonType.CERTIFICATION_2.type -> {
                titleManager.defaultTitle("平台分配个人用户")
                getFacList(1, Constant.MYSELF_MATCHMAKER_ASSIGN_PERSONAL)
            }
            CommonType.FACILITTOR_0.type -> {
                dataType = 1
                titleManager.defaultTitle("拓展新媒人")
                getFacList(1, Constant.MYSELF_PROVIDER_EXPAND_MATCHMAKER)
            }
            CommonType.FACILITTOR_1.type -> {
                titleManager.defaultTitle("拓展企业主")
                getFacList(1, Constant.MYSELF_PROVIDER_EXPAND_ENTERPRISE)
            }
            CommonType.FACILITTOR_2.type -> {
                titleManager.defaultTitle("拓展个人用户")
                getFacList(1, Constant.MYSELF_PROVIDER_EXPAND_PERSONAL)
            }
            CommonType.FACILITTOR_3.type -> {
                titleManager.defaultTitle("平台分配企业主")
                getFacList(1, Constant.MYSELF_PROVIDER_ASSIGN_ENTERPRISE)
            }
            CommonType.FACILITTOR_4.type -> {
                titleManager.defaultTitle("平台分配个人用户")
                getFacList(1, Constant.MYSELF_PROVIDER_ASSIGN_PERSONAL)
            }
        }

        adapter = CommonListAdapter(list, dataType)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                if (list[index].itemType == "0" && adapter.dataType == 1) {
                    if (list[index].isExpand == true) {
                        list[index].isExpand = false
                        list.removeAll(list[index].new)
                        adapter.notifyItemMoved(index + 1, list[index].new.size)
                        adapter.notifyDataSetChanged()
                    } else {
                        list[index].isExpand = true
                        list.addAll(index + 1, list[index].new)
                        adapter.notifyItemRangeInserted(index + 1, list[index].new.size)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context, type: String) {
            mContext.startActivity(Intent(mContext, CommListActivity::class.java).putExtra("typeList", type))
        }
    }

    private fun getFacList(flag: Int, url: String) {
        val params = HashMap<String, String>()
//        params["listType"] = listType
//        params["isBeDistribution"] = isBeDistribution
        ApiManager2.get(flag, this, params, url, object : ApiManager2.OnResult<BaseBean<ArrayList<CommonModel>>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<ArrayList<CommonModel>>) {
                    if (dataType == 0) {
                        list.addAll(data.message?: arrayListOf())
                    } else {
                        data.message?.let {
                            it.forEach {
                                it.new.forEach {
                                    it.itemType = "1"
                                }
                                it.new.add(0, CommonModel("该新媒人拓展企业主","",false , 2.toString(), arrayListOf()))
                                it.itemType = "0"
                            }
                        }
                        list.addAll(data.message?: arrayListOf())
                    }
//                list.clear()
//                data.message?.let {
//                    list.addAll(it)
                    adapter.notifyDataSetChanged()
//                }

                if (list.isEmpty()){
                    showReTryLayout("暂无数据")
                }
            }


            override fun onCatch(data: BaseBean<ArrayList<CommonModel>>) {
            }
        })
    }

//    private fun getCerList(flag: Int, listType: String, isBeDistribution: String) {
//        val params = HashMap<String, String>()
//        params["listType"] = listType
//        params["isBeDistribution"] = isBeDistribution
//        ApiManager.get(flag, this, params, Constant.PARTNER_MAIN_LIST, object : ApiManager.OnResult<BaseModel<CommonModel>>() {
//            override fun onSuccess(data: BaseModel<CommonModel>) {
//                hideLoading()
//                if (data.success) {
//                    list.addAll(data.entity?.infoList ?: arrayListOf())
//                    adapter.notifyDataSetChanged()
//                } else {
//                    ToastUtil.show(data.message)
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//                hideLoading()
//            }
//
//            override fun onCatch(data: BaseModel<CommonModel>) {
//            }
//        })
//    }
}