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
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiSearchAdapter
import com.cocosh.shmstore.home.model.SMCompanyData
import com.cocosh.shmstore.http.ApiManager
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
        titleManager.defaultTitle("关注")
        recyclerView.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.w45).toInt(), resources.getDimension(R.dimen.w45).toInt()))
        adapter = ShouMeiSearchAdapter(this, companyList)
        recyclerView.recyclerView.adapter = adapter
        adapter.setOnSubBtnClickListener(object : ShouMeiSearchAdapter.OnSubBtnClickListener {
            override fun followClick(position: Int) {
                if (companyList[position].followStatus == "0") {
                    cancelOrConfirm(companyList[position].idCompanyHomeBaseInfo
                            ?: "", "1", position)
                    return
                }
                cancelOrConfirm(companyList[position].idCompanyHomeBaseInfo
                        ?: "", "0", position)
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
                    getCompanyList(true, currentPage, "", "2", companyName ?: "")
                    return@setOnEditorActionListener true;
                }
            }
            return@setOnEditorActionListener false
        }

        recyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                getCompanyList(true, currentPage, "", "2", companyName ?: "")
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                getCompanyList(false, currentPage, "", "2", companyName ?: "")
            }
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            tvClear.id -> {
                companyList.clear()
                adapter?.notifyDataSetChanged()
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

    private fun getCompanyList(boolean: Boolean, currentPage: Int, timeStamp: String, followType: String, companyName: String) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["currentPage"] = currentPage.toString()
        params["showCount"] = "20"
        params["timeStamp"] = timeStamp
        params["followType"] = followType
        params["companyName"] = companyName
        ApiManager.get(0, this, params, Constant.SM_COMPANY_LIST, object : ApiManager.OnResult<BaseModel<ArrayList<SMCompanyData>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<SMCompanyData>>) {
                recyclerView.isRefreshing = false
                isShowLoading = false
                if (data.success && data.code == 200) {
                    if (boolean) {
                        companyList.clear()
                        recyclerView.update(data.entity)
                    } else {
                        recyclerView.loadMore(data.entity)
                    }
                    companyList.addAll(data.entity ?: arrayListOf())
                    adapter.notifyDataSetChanged()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<ArrayList<SMCompanyData>>) {
            }
        })
    }

    private fun cancelOrConfirm(idCompanyHomeBaseInfo: String, isFollow: String, position: Int) {
        isShowLoading = true
        val params = HashMap<String, String>()
        params["idCompanyHomeBaseInfo"] = idCompanyHomeBaseInfo
        params["isFollow"] = isFollow
        ApiManager.post(this, params, Constant.SM_FOLLOW_OR_CANCEL, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                isShowLoading = false
                if (data.success && data.code == 200) {
                    companyList[position].followStatus = isFollow
                    adapter.notifyItemChanged(position)
                    ObserverManager.getInstance().notifyObserver(3, idCompanyHomeBaseInfo, isFollow, "")
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }
}