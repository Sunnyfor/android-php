package com.cocosh.shmstore.mine.ui.authentication

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.SendBonus
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.SendRedPackageListAdapter
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_send_packages.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


/**
 *
 * Created by lmg on 2018/4/20.
 */
class SendPackageFragment : BaseFragment() {
    var list = arrayListOf<SendBonus>()
    private var isInit = false
    var type: String? = null  //状态（不传代表全部） 0：待付款，2：审核中，5：已投放，3：被驳回
    var currentPage = 1
    var adapter: SendRedPackageListAdapter? = null

    override fun reTryGetData() {
        hideReTryLayout()
        loadData(true)
    }


    override fun setLayout(): Int = R.layout.fragment_send_packages
    override fun initView() {
        getLayoutView().smSwipeRefreshLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        (getLayoutView().smSwipeRefreshLayout.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        adapter = SendRedPackageListAdapter(getBaseActivity(),list)

        getLayoutView().smSwipeRefreshLayout.recyclerView.adapter = adapter
        adapter?.onCancleReleaseListener = object : SendRedPackageListAdapter.OnCancleReleaseListener {
            override fun onCancleRelease(bonus: SendBonus) {
                    (activity as SendPackageActivity).cancleRelease(bonus)
            }
        }
        getLayoutView().smSwipeRefreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                loadData(true)
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                loadData(false)
            }

        }

//        getLayoutView().smSwipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()

    }

    override fun close() {

    }

    override fun onListener(view: View) {

    }


    fun loadData(boolean: Boolean) {
        val params = hashMapOf<String, String>()
        params["page"] = currentPage.toString()
        params["limit"] = getLayoutView().smSwipeRefreshLayout.pageCount.toString()
        type?.let {
            params["status"] = it
        }
        ApiManager2.post(0, getBaseActivity(), params, Constant.MYSELF_SENDRP_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<SendBonus>>>() {
            override fun onFailed(code: String, message: String) {
                if (currentPage == 1){
                    getLayoutView().smSwipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onSuccess(data: BaseBean<ArrayList<SendBonus>>) {

                data.message?.let {
                    getBaseActivity().isShowLoading = false
                    if (boolean) {
                        list.clear()
                        getLayoutView().smSwipeRefreshLayout.update(it)
                    } else {
                        getLayoutView().smSwipeRefreshLayout.loadMore(it)
                    }
                    list.addAll(it)
                    getLayoutView().smSwipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                }

            }


            override fun onCatch(data: BaseBean<ArrayList<SendBonus>>) {
            }

        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            //首次初始化加载数据并删除支付成功后的刷新标记
            if (!isInit) {
                isInit = true
                update()
                SmApplication.getApp().removeData(DataCode.BONUS_PAY)
                return
            }

            //此处用于支付成功后刷新列表
            if (activity is SendPackageActivity) {
                (activity as SendPackageActivity).let {
                    if (type == null && !it.allUpdateOk) {
                        loadData(true)
                        it.allUpdateOk(true)
                    }

                    if (type == "10" && !it.paymentUpdateOk) {
                        loadData(true)
                        it.paymentUpdateOk(true)
                    }
                }
            }

        }
    }


    fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

//    fun notifyDataSetChanged(position: Int) {
//        adapter?.notifyTimeLeave(position)
//    }

    fun update() {
        launch(UI) {
            delay(500)
            loadData(true)
        }
    }

    override fun onResume() {
        super.onResume()
        SmApplication.getApp().getData<Boolean>(type?:"",true)?.let{
            if (it){
                loadData(true)
            }
        }
    }
}