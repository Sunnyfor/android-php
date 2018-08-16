package com.cocosh.shmstore.home

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiSearchAdapter
import com.cocosh.shmstore.home.model.SMCompanyData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.activity_shoumei_search.*

/**
 * 首媒之家 搜索页面
 * Created by lmg on 2018/6/4.
 */
class ShouMeiSearchActivity : BaseActivity() {
    var currentPage = 1
    var companyList = arrayListOf<SMCompanyData>()
    private lateinit var adapter: ShouMeiSearchAdapter
    var companyName: String? = ""
    override fun setLayout(): Int = R.layout.activity_shoumei_search


    override fun initView() {
        titleManager.defaultTitle("搜索")
        recyclerView.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.w45).toInt(), resources.getDimension(R.dimen.w45).toInt()))
        adapter = ShouMeiSearchAdapter(this, companyList)
        recyclerView.recyclerView.adapter = adapter
        adapter.setOnSubBtnClickListener(object : ShouMeiSearchAdapter.OnSubBtnClickListener {
            override fun followClick(position: Int) {
                if (companyList[position].follow == "0") {
                    cancelOrConfirm(companyList[position].eid, "1", position)
                    return
                }
                cancelOrConfirm(companyList[position].eid, "0", position)
            }
        })
        tvClear.setOnClickListener(this)
        etcontent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        etcontent.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!etcontent.text.isNullOrEmpty()) {
                    companyName = etcontent.text.toString()
                    getCompanyList(companyName ?: "")
                    return@setOnEditorActionListener true
                }
            }
            return@setOnEditorActionListener false
        }

        recyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                getCompanyList(companyName ?: "")
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                getCompanyList(companyName ?: "")
            }
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            tvClear.id -> {
                companyList.clear()
                adapter.notifyDataSetChanged()
                etcontent.text = null
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, ShouMeiSearchActivity::class.java))
        }
    }

    private fun getCompanyList(companyName: String) {
        if (companyName.isEmpty()){
            return
        }
        isShowLoading = true
        val params = HashMap<String, String>()
        params["words"] = companyName
        if (currentPage != 1) {
            params["eid"] = currentPage.toString()
        }
        params["num"] = "20"

        ApiManager2.post(0, this, params, Constant.EHOME_SEARCH, object : ApiManager2.OnResult<BaseBean<ArrayList<SMCompanyData>>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<ArrayList<SMCompanyData>>) {
                recyclerView.isRefreshing = false
                isShowLoading = false
                    if (currentPage == 1) {
                        companyList.clear()
                        recyclerView.update(data.message)
                    } else {
                        recyclerView.loadMore(data.message)
                    }
                    companyList.addAll(data.message ?: arrayListOf())
                    adapter.notifyDataSetChanged()
            }

            override fun onCatch(data: BaseBean<ArrayList<SMCompanyData>>) {
            }
        })
    }

    private fun cancelOrConfirm(idCompanyHomeBaseInfo: String, isFollow: String, position: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["eid"] = idCompanyHomeBaseInfo
        params["op"] = if (isFollow == "1") "follow" else "cancel" //动作类型 (必填,'cancel'-取消关注,'follow'-关注)
        ApiManager2.post(this, params, Constant.EHOME_FOLLOW_OPERATE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
                isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<String>) {
                isShowLoading = false
                companyList[position].follow = isFollow
                adapter.notifyItemChanged(position)
                ObserverManager.getInstance().notifyObserver(3, idCompanyHomeBaseInfo, isFollow, "")
            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }
}