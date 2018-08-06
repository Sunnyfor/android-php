package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiVAdapter
import com.cocosh.shmstore.home.model.SMCompanyData
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.observer.ObserverListener
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.fragment_shoumei_follow.view.*

/**
 * Created by lmg on 2018/5/30.
 */
class ShoumeiFindFragment : BaseFragment(), ObserverListener {
    var currentPage = 1
    var companyList = arrayListOf<SMCompanyData>()
    var companyThemeList = arrayListOf<SMCompanyThemeData.SubCompanyTheme>()
    lateinit var adapter: ShouMeiVAdapter
    override fun setLayout(): Int = R.layout.fragment_shoumei_follow
    var isInit = false
    var lastTimeStamp: String? = ""

    override fun observerUpData(type: Int, data: Any, content: Any, dataExtra: Any) {
        if (type == 1) {
            companyThemeList.let {
                var index = it.indexOfFirst {
                    it.idCompanyHomeTheme == data as String
                }
                if (index != -1) {
                    it[index].commentsNumber = content.toString()
                    adapter?.notifyItemChanged(index + 1)
                }
            }
        }
        if (type == 3) {
            notifyFollowStatus(data as String, content as String)
        }

        if (type == 4) {
            companyThemeList.let {
                var index = it.indexOfFirst {
                    it.idCompanyHomeTheme == data as String
                }
                if (index != -1) {
                    it[index].readNumber = (it[index].readNumber ?: "0".toInt()+1).toString()
                    adapter?.notifyItemChanged(index + 1)
                }
            }
        }
    }

    override fun initView() {
        getLayoutView().vRecyclerView.recyclerView.layoutManager = LinearLayoutManager(activity)
        getLayoutView().vRecyclerView.recyclerView.setHasFixedSize(false)
        adapter = ShouMeiVAdapter(-2, companyList, activity, companyThemeList)
        getLayoutView().vRecyclerView.recyclerView.adapter = adapter

        getLayoutView().vRecyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {

            override fun onUpdate(page: Int) {
                currentPage = page
                getThemeList(true, currentPage, "", "0")
                getCompanyList("", "0", "")
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                getThemeList(false, currentPage, lastTimeStamp ?: "", "0")
            }
        }

        adapter.setOnFollowClick(object : ShouMeiVAdapter.OnFollowClick {
            override fun read(type: Int?, themeCompanyIndex: Int?) {
                readAccount(companyThemeList[themeCompanyIndex ?: 0].idCompanyHomeTheme ?: "")
            }

            override fun follow(type: Int?, themeCompanyIndex: Int?) {
                if (type == 1) {
                    if (companyList[themeCompanyIndex!!].followStatus == "0") {
                        cancelOrConfirm(companyList[themeCompanyIndex].idCompanyHomeBaseInfo
                                ?: "", "1")
                        return
                    }
                    cancelOrConfirm(companyList[themeCompanyIndex].idCompanyHomeBaseInfo
                            ?: "", "0")
                } else {
                    if (companyThemeList[themeCompanyIndex!!].resCompanyHomeInfoVO?.followStatus == "0") {
                        cancelOrConfirm(companyThemeList[themeCompanyIndex].resCompanyHomeInfoVO?.idCompanyHomeBaseInfo
                                ?: "", "1")
                        return
                    }
                    cancelOrConfirm(companyThemeList[themeCompanyIndex].resCompanyHomeInfoVO?.idCompanyHomeBaseInfo
                            ?: "", "0")
                }
            }
        })
        getThemeList(true, currentPage, "", "0")
        getCompanyList("", "0", "")
        (activity as BaseActivity).showLoading()
        isInit = true

        ObserverManager.getInstance().add(this)
    }


    override fun reTryGetData() {
        if (isInit) {
            getThemeList(true, currentPage, "", "0")
            getCompanyList("", "0", "")
            (activity as BaseActivity).showLoading()
        }
    }

    override fun onListener(view: View) {

    }

    override fun close() {

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isInit) {
            getThemeList(true, currentPage, "", "0")
            getCompanyList("", "0", "")
        }
    }

    private fun getCompanyList(timeStamp: String, followType: String, companyName: String) {
        getBaseActivity().isShowLoading = true
        val params = HashMap<String, String>()
        params["showCount"] = "20"
        params["timeStamp"] = timeStamp
        params["followType"] = followType
        params["companyName"] = companyName
        ApiManager.get(0, activity as BaseActivity, params, Constant.SM_COMPANY_LIST, object : ApiManager.OnResult<BaseModel<ArrayList<SMCompanyData>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<SMCompanyData>>) {
                getLayoutView().vRecyclerView.isRefreshing = false
                getBaseActivity().isShowLoading = false
                if (data.success && data.code == 200) {
                    companyList.clear()
                    companyList.addAll(data.entity ?: arrayListOf())
                    adapter.hNotify()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                getLayoutView().vRecyclerView.isRefreshing = false
                getBaseActivity().isShowLoading = false
            }

            override fun onCatch(data: BaseModel<ArrayList<SMCompanyData>>) {
            }
        })
    }

    private fun getThemeList(boolean: Boolean, currentPage: Int, timeStamp: String, followType: String) {
        getBaseActivity().isShowLoading = true
        val params = HashMap<String, String>()
        params["currentPage"] = currentPage.toString()
        params["showCount"] = "20"
        params["timeStamp"] = timeStamp
        params["followType"] = followType
        ApiManager.get(0, activity as BaseActivity, params, Constant.SM_COMMON_LIST, object : ApiManager.OnResult<BaseModel<SMCompanyThemeData>>() {
            override fun onSuccess(data: BaseModel<SMCompanyThemeData>) {
                getLayoutView().vRecyclerView.isRefreshing = false
                getBaseActivity().isShowLoading = false
                if (data.success) {
                    if (boolean) {
                        companyThemeList.clear()
                        getLayoutView().vRecyclerView.update(data.entity?.themeInfoVOList)
                    } else {
                        getLayoutView().vRecyclerView.loadMore(data.entity?.themeInfoVOList)
                    }
                    lastTimeStamp = data.entity?.timeStamp
                    companyThemeList.addAll(data.entity?.themeInfoVOList ?: arrayListOf())
                    adapter.notifyDataSetChanged()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                getLayoutView().vRecyclerView.isRefreshing = false
                getBaseActivity().isShowLoading = false
            }

            override fun onCatch(data: BaseModel<SMCompanyThemeData>) {
            }

        })
    }

    private fun cancelOrConfirm(idCompanyHomeBaseInfo: String, isFollow: String) {
        getBaseActivity().isShowLoading = true
        val params = HashMap<String, String>()
        params["idCompanyHomeBaseInfo"] = idCompanyHomeBaseInfo
        params["isFollow"] = isFollow
        ApiManager.post(activity as BaseActivity, params, Constant.SM_FOLLOW_OR_CANCEL, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                getBaseActivity().isShowLoading = false
                if (data.success && data.code == 200) {
                    notifyFollowStatus(idCompanyHomeBaseInfo, isFollow)
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                getBaseActivity().isShowLoading = false
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }

    fun notifyFollowStatus(id: String, status: String) {
        companyList.forEach {
            if (it.idCompanyHomeBaseInfo == id) {
                it.followStatus = status
            }
        }
        companyThemeList.forEach {
            if (it.resCompanyHomeInfoVO?.idCompanyHomeBaseInfo == id) {
                it.resCompanyHomeInfoVO?.followStatus = status
            }
        }
        adapter.notifyDataSetChanged()
        adapter.hNotify()
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManager.getInstance().remove(this)
    }

    private fun readAccount(idCompanyHomeTheme: String) {
        val params = HashMap<String, String>()
        params["idCompanyHomeTheme"] = idCompanyHomeTheme
        ApiManager.post(activity as BaseActivity, params, Constant.SM_READ_ACCOUNT, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                getBaseActivity().isShowLoading = false
                if (data.success && data.code == 200) {

                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                getBaseActivity().isShowLoading = false
            }

            override fun onCatch(data: BaseModel<String>) {
            }

        })
    }
}
