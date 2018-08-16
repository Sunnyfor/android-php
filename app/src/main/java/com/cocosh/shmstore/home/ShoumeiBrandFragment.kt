package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.home.adapter.ShouMeiBrandAdapter
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.home.model.SMThemeData
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
class ShoumeiBrandFragment : BaseFragment(), ObserverListener {
    var currentPage = 1
    var list = arrayListOf<SMCompanyThemeData>()
    var lastTimeStamp: String? = ""
    var followType: String? = ""
    var blackType: String? = ""
    var baseId: String? = ""
    var themePageUrl: String? = ""
    lateinit var adapter: ShouMeiBrandAdapter
    override fun setLayout(): Int = R.layout.fragment_shoumei_follow

    override fun observerUpData(type: Int, data: Any, content: Any, dataExtra: Any) {
        if (type == 1) {
            list.let {
                val index = it.indexOfFirst {
                    it.posts.id == data as String
                }
                if (index != -1) {
//                    it[index].commentsNumber = content.toString()
                    adapter.notifyItemChanged(index)
                }
            }
        }

        if (type == 3) {
            list.let {
                list.forEach {
                    if (it.bbs.id == data as String) {
                        it.bbs.follow = content as String
                    }
                }
            }
            adapter.notifyDataSetChanged()
            followType = content as String
        }
    }

    override fun initView() {
        val bundle = arguments//从activity传过来的Bundle
        val type = bundle?.getString("TYPE", "全部")
        getLayoutView().vRecyclerView.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ShouMeiBrandAdapter(list)
        getLayoutView().vRecyclerView.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                //更改状态
                val oldNumber = list[index].posts.views?.toInt()
                val number = (oldNumber)?:0 + 1
                list[index].posts.views = number.toString()

                adapter.notifyItemChanged(index)
//                list[index].isRead = "1"
//                readAccount(list[index].posts.id ?: "")
                //跳转内容详情页
                ShoumeiDetailActivity.start(activity, list[index].posts.url?:"", list[index].posts.id?:"")
                //浏览数
                list[index].posts.views = (list[index].posts.views?:"0".toInt()+1).toString()
                adapter.notifyItemChanged(index)
                ObserverManager.getInstance().notifyObserver(4, list[index].posts.id
                        ?: "", "", "")
            }
        })

        getLayoutView().vRecyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {

            override fun onUpdate(page: Int) {
                currentPage = page
                getLayoutView().vRecyclerView.isRefreshing = false
            }

            override fun onLoadMore(page: Int) {
                var currentPage = page
                if (type == "全部") {
                    getThemeList(false, currentPage, lastTimeStamp
                            ?: "", (activity as ShouMeiBrandActivity).baseId ?: "", "0")
                } else {
                    getThemeList(false, currentPage, lastTimeStamp
                            ?: "", (activity as ShouMeiBrandActivity).baseId ?: "", "1")
                }
            }
        }
        if (type == "全部") {
            getThemeList(true, currentPage, "", (activity as ShouMeiBrandActivity).baseId
                    ?: "", "0")
        } else {
            getThemeList(true, currentPage, "", (activity as ShouMeiBrandActivity).baseId
                    ?: "", "1")
        }

        ObserverManager.getInstance().add(this)
    }

    override fun reTryGetData() {

    }

    override fun onListener(view: View) {

    }

    override fun close() {

    }

    private fun getThemeList(boolean: Boolean, currentPage: Int, timeStamp: String, idCompanyHomeBaseInfo: String, isEssence: String) {
        getBaseActivity().isShowLoading = true
        val params = HashMap<String, String>()
        params["currentPage"] = currentPage.toString()
        params["showCount"] = "20"
        params["timeStamp"] = timeStamp
        params["idCompanyHomeBaseInfo"] = idCompanyHomeBaseInfo
        params["isEssence"] = isEssence
        ApiManager.get(0, activity as BaseActivity, params, Constant.SM_THEME_LIST, object : ApiManager.OnResult<BaseModel<SMThemeData>>() {
            override fun onSuccess(data: BaseModel<SMThemeData>) {
                getBaseActivity().isShowLoading = false
                getLayoutView().vRecyclerView.isRefreshing = false
                if (data.success) {
                    if (boolean) {
                        (activity as ShouMeiBrandActivity).setData(data.entity?.resCompanyHomeBrandExclusiveVO?.forumHeadImg
                                ?: ""
                                , data.entity?.resCompanyHomeBrandExclusiveVO?.forumName ?: ""
                                , data.entity?.resCompanyHomeBrandExclusiveVO?.followCount ?: ""
                                , data.entity?.resCompanyHomeBrandExclusiveVO?.announcement ?: ""
                                , data.entity?.resCompanyHomeBrandExclusiveVO?.followStatus ?: "")
                        list.clear()
                        getLayoutView().vRecyclerView.update(data.entity?.themeInfoVOS)
                        followType = data.entity?.resCompanyHomeBrandExclusiveVO?.followStatus
                        blackType = data.entity?.resCompanyHomeBrandExclusiveVO?.isBlack
                    } else {
                        getLayoutView().vRecyclerView.loadMore(data.entity?.themeInfoVOS)
                    }
                    lastTimeStamp = data.entity?.timeStamp
                    list.addAll(data.entity?.themeInfoVOS ?: arrayListOf())
                    adapter.notifyDataSetChanged()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                getBaseActivity().isShowLoading = false
            }

            override fun onCatch(data: BaseModel<SMThemeData>) {
            }

        })
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
