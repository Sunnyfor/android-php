package com.cocosh.shmstore.mine.ui.authentication

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.SendBonus
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.SendRedPackageListAdapter
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.fragment_send_packages.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


/**
 *
 * Created by lmg on 2018/4/20.
 */
class SendPackageFragment : BaseFragment() {
    var list = arrayListOf<SendBonus.Bonus>()
    private var isInit = false
    var type: String? = null  //10 待付款 20审核中 30投放中 21被驳回
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
            override fun onCancleRelease(bonus: SendBonus.Bonus) {
                if ((bonus.redPacketOrderId ?: "").isEmpty()) {
                    ToastUtil.show("订单状态不正确")
                } else {
                    (activity as SendPackageActivity).cancleRelease(bonus)
                }
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
        params["currentPage"] = currentPage.toString()
        params["showCount"] = getLayoutView().smSwipeRefreshLayout.pageCount.toString()
        if (type != null) {
            params["orderStatus"] = type!!
        }
        ApiManager.get(0, getBaseActivity(), params, Constant.BONUS_SEND_LISTT, object : ApiManager.OnResult<BaseModel<SendBonus>>() {
            override fun onSuccess(data: BaseModel<SendBonus>) {

                if (data.success) {
                    getBaseActivity().isShowLoading = false
                    if (boolean) {
                        list.clear()
                        getLayoutView().smSwipeRefreshLayout.update(data.entity?.list)
                    } else {
                        getLayoutView().smSwipeRefreshLayout.loadMore(data.entity?.list)
                    }
                    list.addAll(data.entity?.list ?: arrayListOf())
                    getLayoutView().smSwipeRefreshLayout.recyclerView.adapter.notifyDataSetChanged()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                showReTryLayout()
            }

            override fun onCatch(data: BaseModel<SendBonus>) {
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

    fun notifyDataSetChanged(position: Int) {
        adapter?.notifyTimeLeave(position)
    }

    fun update() {
        launch(UI) {
            delay(500)
            loadData(true)
        }
    }
}