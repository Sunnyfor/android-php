package com.cocosh.shmstore.mine.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.IndustryAdapter
import com.cocosh.shmstore.mine.model.IndustryModel
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.activity_industry.*


/**
 *
 * Created by zhangye on 2018/9/4.
 */
class IndustryActivity : BaseActivity() {
    private var datalist = ArrayList<IndustryModel>()

    override fun setLayout(): Int = R.layout.activity_industry

    override fun initView() {
        titleManager.defaultTitle("选择行业")
        loadData()
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }


    private fun loadData() {
        ApiManager2.get(1, this, null, Constant.INDUSTRY, object : ApiManager2.OnResult<BaseBean<ArrayList<IndustryModel>>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<ArrayList<IndustryModel>>) {
                data.message?.let {
                    processData(it)
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<IndustryModel>>) {

            }

        })
    }

    fun processData(list: ArrayList<IndustryModel>) {
        val typeArray = list.filter { it.parent == "0" } as ArrayList<IndustryModel>

        typeArray.forEach {
            val id = it.id
            datalist.add(it)
            datalist.addAll(list.filter { it.parent == id })

        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = IndustryAdapter(datalist, intent.getStringExtra("industry"))
        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(v: View, index: Int) {
                update(adapter,datalist[index],index)
            }
        })


        recyclerView.adapter = adapter
    }

    fun update(adapter: IndustryAdapter,industryModel: IndustryModel,index:Int) {
        industryModel.id?.let {
            val params = hashMapOf<String, String>()
            params["industry"] = it
            UserManager2.updateMemberEntrance(this, params, object : ApiManager2.OnResult<BaseBean<String>>() {
                override fun onSuccess(data: BaseBean<String>) {
                        adapter.updateState(index)
                }

                override fun onFailed(code: String, message: String) {

                }

                override fun onCatch(data: BaseBean<String>) {

                }

            })
        }
    }

}