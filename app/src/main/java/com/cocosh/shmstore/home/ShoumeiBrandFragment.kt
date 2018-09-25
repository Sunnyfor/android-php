package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.*
import com.cocosh.shmstore.home.adapter.ShouMeiBrandAdapter
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.home.model.SMThemeData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.observer.ObserverListener
import com.cocosh.shmstore.widget.observer.ObserverManager
import kotlinx.android.synthetic.main.fragment_shoumei_follow.view.*

/**
 * Created by lmg on 2018/5/30.
 */
class ShoumeiBrandFragment : BaseFragment(), ObserverListener {
    var currentPage = 1
    var list = arrayListOf<SMThemeData>()
    var lastTimeStamp: String? = ""
    var followType: String? = ""
    var silence: String? = ""
    var themePageUrl: String? = ""
    lateinit var adapter: ShouMeiBrandAdapter

    var type: String? = null
    var eid: String? = null
    override fun setLayout(): Int = R.layout.fragment_shoumei_follow

    override fun observerUpData(type: Int, data: Any, content: Any, dataExtra: Any) {
        if (type == 1) {
            list.let {
                val index = it.indexOfFirst {
                    it.id == data as String
                }
                if (index != -1) {
//                    it[index].commentsNumber = content.toString()
                    adapter.notifyItemChanged(index)
                }
            }
        }

//        if (type == 3) {
//            list.let {
//                list.forEach {
//                    if (it.bbs.id == data as String) {
//                        it.bbs.follow = content as String
//                    }
//                }
//            }
//            adapter.notifyDataSetChanged()
//            followType = content as String
//        }
    }

    override fun initView() {
        val bundle = arguments//从activity传过来的Bundle
        type = bundle?.getString("TYPE", "全部")
        eid = bundle?.getString("EID", "")
        followType = bundle?.getString("followType", "0")
        silence = bundle?.getString("silence", "0")

        getLayoutView().vRecyclerView.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ShouMeiBrandAdapter(list)
        getLayoutView().vRecyclerView.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                //更改状态
                val oldNumber = list[index].views?.toInt()
                val number = (oldNumber) ?: 0+1
                list[index].views = number.toString()

                adapter.notifyItemChanged(index)
//                list[index].isRead = "1"
//                readAccount(list[index].posts.id ?: "")
                //跳转内容详情页
                ShoumeiDetailActivity.start(activity, list[index].title ?: "", list[index].url
                        ?: "", eid ?: "", list[index].id ?: "", followType ?: "0", silence ?: "0")
                //浏览数
                list[index].views = (list[index].views ?: "0".toInt()+1).toString()
                adapter.notifyItemChanged(index)
                ObserverManager.getInstance().notifyObserver(4, list[index].id
                        ?: "", "", "")
            }
        })

        getLayoutView().vRecyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {

            override fun onUpdate(page: Int) {
                currentPage = page
                getLayoutView().vRecyclerView.isRefreshing = false
                loadData()
            }

            override fun onLoadMore(page: Int) {
                currentPage = (list.last().id ?: "0").toInt()
                loadData()
            }
        }

        loadData()

        ObserverManager.getInstance().add(this)
    }

    override fun reTryGetData() {

    }

    override fun onListener(view: View) {

    }

    override fun close() {

    }

//    private fun getThemeList(boolean: Boolean, currentPage: Int, timeStamp: String, idCompanyHomeBaseInfo: String, isEssence: String) {
//        getBaseActivity().isShowLoading = true
//        val params = HashMap<String, String>()
//        params["currentPage"] = currentPage.toString()
//        params["showCount"] = "20"
//        params["timeStamp"] = timeStamp
//        params["idCompanyHomeBaseInfo"] = idCompanyHomeBaseInfo
//        params["isEssence"] = isEssence
//        ApiManager.get(0, activity as BaseActivity, params, Constant.SM_THEME_LIST, object : ApiManager.OnResult<BaseModel<SMThemeData>>() {
//            override fun onSuccess(data: BaseModel<SMThemeData>) {
//                getBaseActivity().isShowLoading = false
//                getLayoutView().vRecyclerView.isRefreshing = false
//                if (data.success) {
//                    if (boolean) {
//                        (activity as ShouMeiBrandActivity).setData(data.entity?.resCompanyHomeBrandExclusiveVO?.forumHeadImg
//                                ?: ""
//                                , data.entity?.resCompanyHomeBrandExclusiveVO?.forumName ?: ""
//                                , data.entity?.resCompanyHomeBrandExclusiveVO?.followCount ?: ""
//                                , data.entity?.resCompanyHomeBrandExclusiveVO?.announcement ?: ""
//                                , data.entity?.resCompanyHomeBrandExclusiveVO?.followStatus ?: "")
//                        list.clear()
//                        getLayoutView().vRecyclerView.update(data.entity?.themeInfoVOS)
//                        followType = data.entity?.resCompanyHomeBrandExclusiveVO?.followStatus
//                        silence = data.entity?.resCompanyHomeBrandExclusiveVO?.isBlack
//                    } else {
//                        getLayoutView().vRecyclerView.loadMore(data.entity?.themeInfoVOS)
//                    }
//                    lastTimeStamp = data.entity?.timeStamp
//                    list.addAll(data.entity?.themeInfoVOS ?: arrayListOf())
//                    adapter.notifyDataSetChanged()
//                } else {
//                    ToastUtil.show(data.message)
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//                getBaseActivity().isShowLoading = false
//            }
//
//            override fun onCatch(data: BaseModel<SMThemeData>) {
//            }
//
//        })
//    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManager.getInstance().remove(this)
    }

//    private fun readAccount(idCompanyHomeTheme: String) {
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
//    }

    fun loadData() {
        val params = HashMap<String, String>()
        if (currentPage != 1) {
            params["post_id"] = currentPage.toString()
        }
        params["num"] = "20"
        params["eid"] = eid ?: ""
        params["is_elite"] = if (type == "全部") "0" else "1"
        ApiManager2.post(getBaseActivity(), params, Constant.EHOME_POSTS, object : ApiManager2.OnResult<BaseBean<ArrayList<SMThemeData>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<SMThemeData>>) {
                data.message?.let {
                    if (currentPage == 1) {
                        list.clear()
                        getLayoutView().vRecyclerView.update(data.message)
                    } else {
                        getLayoutView().vRecyclerView.loadMore(data.message)
                    }

                    list.addAll(it)
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onFailed(code: String, message: String) {
                if (currentPage == 1) {
                    list.clear()
                    adapter.notifyDataSetChanged()
                    getLayoutView().vRecyclerView.update(arrayListOf<SMThemeData>())
                } else {
                    getLayoutView().vRecyclerView.loadMore(arrayListOf<SMThemeData>())
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<SMThemeData>>) {

            }

        })
    }
}
