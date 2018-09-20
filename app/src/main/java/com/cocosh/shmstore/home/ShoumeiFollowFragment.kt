package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.ShouMeiVAdapter
import com.cocosh.shmstore.home.model.SMCompanyData
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.observer.ObserverListener
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.fragment_shoumei_follow.view.*

/**
 *
 * Created by lmg on 2018/5/30.
 */
class ShoumeiFollowFragment : BaseFragment(), ObserverListener {
    var currentPage = 1
    var companyList = arrayListOf<SMCompanyData>()
    var companyThemeList = arrayListOf<SMCompanyThemeData>()
    lateinit var adapter: ShouMeiVAdapter
    var isInit = false

    override fun setLayout(): Int = R.layout.fragment_shoumei_follow

    override fun observerUpData(type: Int, data: Any, content: Any, dataExtra: Any) {
        if (type == 1) {
            companyThemeList.let {
                val index = it.indexOfFirst {
                    it.posts.id == data as String
                }
                if (index != -1) {
                    it[index].posts.sum += (content as Int)
                    adapter.notifyItemChanged(index + 1)
                }
            }
        }

        if (type == 3) {
            notifyFollowStatus(data as String, content as String)
        }

        if (type == 4) {
            companyThemeList.let {
                val index = it.indexOfFirst {
                    it.posts.id == data as String
                }
                if (index != -1) {
                    it[index].posts.views = (it[index].posts.views?:"0".toInt() + 1).toString()
                    adapter.notifyItemChanged(index + 1)
                }
            }
        }
    }

    override fun initView() {
        getLayoutView().vRecyclerView.recyclerView.layoutManager = LinearLayoutManager(activity)
        getLayoutView().vRecyclerView.recyclerView.setHasFixedSize(false)
        adapter = ShouMeiVAdapter(-1, companyList, activity, companyThemeList)
        getLayoutView().vRecyclerView.recyclerView.adapter = adapter

        getLayoutView().vRecyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {

            override fun onUpdate(page: Int) {
                currentPage = page
                getCompanyList()
                getThemeList(true)
            }

            override fun onLoadMore(page: Int) {
                currentPage = (companyThemeList.last().bbs.eid?:"0").toInt()
                getThemeList(false)
            }
        }

        adapter.setOnFollowClick(object : ShouMeiVAdapter.OnFollowClick {
            override fun read(type: Int?, themeCompanyIndex: Int?) {
//                readAccount(companyThemeList[themeCompanyIndex ?: 0].posts.id?:"")
            }

            override fun follow(type: Int?, themeCompanyIndex: Int?) {
                if (type == 1) {
                    if (companyThemeList[themeCompanyIndex!!].bbs.follow == "0") {
                        cancelOrConfirm(companyThemeList[themeCompanyIndex].bbs.eid?:"", "1")
                        return
                    }
                    cancelOrConfirm(companyThemeList[themeCompanyIndex].bbs.follow?:"", "0")
                } else {
                    if (companyThemeList[themeCompanyIndex!!].bbs.follow == "0") {
                        cancelOrConfirm(companyThemeList[themeCompanyIndex].bbs.eid?:"", "1")
                        return
                    }
                    cancelOrConfirm(companyThemeList[themeCompanyIndex].bbs.eid?:"", "0")
                }
            }
        })
        getThemeList(true)
        getCompanyList()
        (activity as BaseActivity).showLoading()
        isInit = true
        ObserverManager.getInstance().add(this)
    }


    override fun reTryGetData() {
        if (isInit) {
            getThemeList(true)
            getCompanyList()
        }
    }

    override fun onListener(view: View) {

    }

    override fun close() {

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isInit) {
            getThemeList(true)
            getCompanyList()
        }
    }

    private fun getCompanyList() {
        getBaseActivity().isShowLoading = true
        ApiManager2.post(0, getBaseActivity(), hashMapOf(), Constant.EHOME_FOLLOW_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<SMCompanyData>>>() {
            override fun onFailed(code: String, message: String) {
                companyList.clear()
                adapter.hNotify()
            }

            override fun onSuccess(data: BaseBean<ArrayList<SMCompanyData>>) {
                getBaseActivity().isShowLoading = false
                companyList.clear()
                companyList.addAll(data.message ?: arrayListOf())
                adapter.hNotify()
            }

            override fun onCatch(data: BaseBean<ArrayList<SMCompanyData>>) {
            }
        })
    }

    private fun getThemeList(boolean: Boolean) {
        getBaseActivity().isShowLoading = true
        val params = HashMap<String, String>()
        if (currentPage != 1) {
            params["eid"] = currentPage.toString()
        }
        params["num"] = "20"
        ApiManager2.post(0, getBaseActivity(), params, Constant.EHOME_FOLLOW_POSTS, object : ApiManager2.OnResult<BaseBean<ArrayList<SMCompanyThemeData>>>() {
            override fun onFailed(code: String, message: String) {
//                getBaseActivity().isShowLoading = false
                getLayoutView().vRecyclerView.isRefreshing = false
            }

            override fun onSuccess(data: BaseBean<ArrayList<SMCompanyThemeData>>) {
//                getBaseActivity().isShowLoading = false
                getLayoutView().vRecyclerView.isRefreshing = false
                if (boolean) {
                    companyThemeList.clear()
                    getLayoutView().vRecyclerView.update(data.message)
                } else {
                    getLayoutView().vRecyclerView.loadMore(data.message)
                }
                companyThemeList.addAll(data.message ?: arrayListOf())
                adapter.notifyDataSetChanged()
            }


            override fun onCatch(data: BaseBean<ArrayList<SMCompanyThemeData>>) {
            }

        })
    }

    private fun cancelOrConfirm(idCompanyHomeBaseInfo: String, isFollow: String) {
        getBaseActivity().isShowLoading = true
        val params = HashMap<String, String>()
        params["eid"] = idCompanyHomeBaseInfo
        params["op"] = if (isFollow == "1") "follow" else "cancel" //动作类型 (必填,'cancel'-取消关注,'follow'-关注)
        ApiManager2.post(activity as BaseActivity, params, Constant.EHOME_FOLLOW_OPERATE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
                getBaseActivity().isShowLoading = false
            }

            override fun onSuccess(data: BaseBean<String>) {
                getBaseActivity().isShowLoading = false
                notifyFollowStatus(idCompanyHomeBaseInfo, isFollow)

            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }

    fun notifyFollowStatus(id: String, status: String) {
        companyList.find { it.eid  == id }?.follow = status

        companyThemeList.find { it.bbs.eid == id }?.bbs?.follow = status

        adapter.notifyDataSetChanged()
        adapter.hNotify()
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManager.getInstance().remove(this)
    }

    private fun readAccount(idCompanyHomeTheme: String) {
//        val params = HashMap<String, String>()
//        params["idCompanyHomeTheme"] = idCompanyHomeTheme
//        ApiManager.post(activity as BaseActivity, params, Constant.SM_READ_ACCOUNT, object : ApiManager.OnResult<BaseModel<String>>() {
//            override fun onSuccess(data: BaseModel<String>) {
//                getBaseActivity().isShowLoading = false
//                if (data.success && data.code == 200) {
//
//                } else {
//                    ToastUtil.show(data.message)
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//                getBaseActivity().isShowLoading = false
//            }
//
//            override fun onCatch(data: BaseModel<String>) {
//            }
//
//        })
    }
}
