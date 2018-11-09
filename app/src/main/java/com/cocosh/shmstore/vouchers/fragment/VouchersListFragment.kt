package com.cocosh.shmstore.vouchers.fragment

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.model.ValueByKey
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.vouchers.apdater.VouchersListAdapter
import com.cocosh.shmstore.vouchers.model.Vouchers
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.dialog.ShareDialog
import kotlinx.android.synthetic.main.fragment_vouchers_list.*
import kotlinx.android.synthetic.main.fragment_vouchers_list.view.*

class VouchersListFragment : BaseFragment() {

    private var type = 0
    private var flag = false //筛选状态
    private var position = 0 //筛选位置
    private var vouchersListAdapter: VouchersListAdapter? = null
    private val arrayList = ArrayList<Vouchers>()
    private var args:Int = 0
    override fun setLayout(): Int = R.layout.fragment_vouchers_list

    override fun initView() {
        if (type != 0) {
            getLayoutView().llNav.visibility = View.GONE
        } else {
            getLayoutView().tvDefault.setOnClickListener(this)
            getLayoutView().tvMoney.setOnClickListener(this)
            getLayoutView().tvDate.setOnClickListener(this)
        }


        vouchersListAdapter = VouchersListAdapter(arrayList, type){
            val shareDialog = ShareDialog(getBaseActivity())
            shareDialog.showShareBase("小红娘送你红包礼券啦，快来领取吧！","抢不完的现金红包等你拿！",it)
        }
        refreshLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        refreshLayout.recyclerView.adapter = vouchersListAdapter

        refreshLayout.onRefreshResult = object:SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                loadData()
            }

            override fun onLoadMore(page: Int) {

            }
        }



        loadData()
    }

    fun setType(type: Int): VouchersListFragment {
        this.type = type
        return this
    }


    override fun reTryGetData() {

    }

    override fun onClick(view: View) {
        initDefault()
        when (view.id) {
            R.id.tvDefault -> {
                position = 0
                getLayoutView().tvDefault.setTextColor(ContextCompat.getColor(context, R.color.red))
                args = 0
                loadData()
            }
            R.id.tvMoney -> {
                if (position != 1) {
                    flag = false
                }
                position = 1
                getLayoutView().tvMoney.setTextColor(ContextCompat.getColor(context, R.color.red))
                if (!flag) {
                    flag = true
                    getLayoutView().vMoney.setBackgroundResource(R.mipmap.ic_vt_top)
                } else {
                    flag = false
                    getLayoutView().vMoney.setBackgroundResource(R.mipmap.ic_vt_bottom)
                }
                args = 1
                loadData(flag)
            }
            R.id.tvDate -> {
                if (position != 2) {
                    flag = false
                }
                position = 2
                getLayoutView().tvDate.setTextColor(ContextCompat.getColor(context, R.color.red))
                if (!flag) {
                    flag = true
                    getLayoutView().vDate.setBackgroundResource(R.mipmap.ic_vt_top)
                } else {
                    flag = false
                    getLayoutView().vDate.setBackgroundResource(R.mipmap.ic_vt_bottom)
                }
                args = 1
                loadData(flag)
            }
        }

    }


    override fun onListener(view: View) {

    }

    override fun close() {

    }

    private fun initDefault() {
        getLayoutView().tvDefault.setTextColor(ContextCompat.getColor(context, R.color.blackText))
        getLayoutView().tvMoney.setTextColor(ContextCompat.getColor(context, R.color.blackText))
        getLayoutView().tvDate.setTextColor(ContextCompat.getColor(context, R.color.blackText))
        getLayoutView().vMoney.setBackgroundResource(R.mipmap.ic_vt_default)
        getLayoutView().vDate.setBackgroundResource(R.mipmap.ic_vt_default)

    }


    fun loadData(flag: Boolean = true) {
        val params = hashMapOf<String, String>()
        params["type"] = (type+1).toString()
        when (args) {
            1 -> params["face"] = if (flag) "desc" else "asc"
            2 -> params["time"] = if (flag) "desc" else "asc"
        }
        ApiManager2.post(getBaseActivity(), params, Constant.COUPON_KIND, object : ApiManager2.OnResult<BaseBean<ArrayList<Vouchers>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Vouchers>>) {
                refreshLayout.isRefreshing = false
                data.message?.let {
                    arrayList.clear()
                    arrayList.addAll(it)
                    vouchersListAdapter?.notifyDataSetChanged()
                }
            }

            override fun onFailed(code: String, message: String) {
                refreshLayout.update(null)
            }

            override fun onCatch(data: BaseBean<ArrayList<Vouchers>>) {
            }
        })

    }
}