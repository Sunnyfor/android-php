package com.cocosh.shmstore.mine.ui.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.InComerListAdapter
import com.cocosh.shmstore.mine.model.ProfitInfoModel
import com.cocosh.shmstore.mine.model.ProfitModel
import com.cocosh.shmstore.mine.ui.mywallet.OutToWalletActivity
import com.cocosh.shmstore.utils.PermissionCode
import com.cocosh.shmstore.utils.PermissionUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_income.*
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * Created by lmg on 2018/4/20.
 */
class IncomeActivity : BaseActivity() {
    private var TYPE_INCOME = ""
    var showCount = 5
    private val list = ArrayList<ProfitModel>()//adapter数据源
    override fun setLayout(): Int = R.layout.activity_income
    var adapter: InComerListAdapter? = null
    override fun initView() {
        titleManager.rightText("收益", "客服", View.OnClickListener {
            showDialog()
        })
        permissionUtil = PermissionUtil(this)
        TYPE_INCOME = intent.getStringExtra("TYPE_INCOME")
        if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type) {
            btn_withdraw_money.text = "转出至钱包"
            inComeTitle.text = "新媒人收益(元)"
        } else {

            btn_withdraw_money.text = "转出至服务商钱包"
            inComeTitle.text = "服务商收益(元)"
        }

        getProfitInfo()
        getProfitList(true, "0")

        btn_withdraw_money.setOnClickListener(this)

        recyclerView.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.recyclerView.setHasFixedSize(true)
        adapter = InComerListAdapter(list)
        recyclerView.recyclerView.adapter = adapter

        recyclerView.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                if (adapter?.itemCount != 0) {
                    //获取第一个可见view的位置
                    val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (adapter?.getItemData(firstItemPosition) != tvShow.text) {
                        tvShow.text = adapter?.getItemData(firstItemPosition)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        recyclerView.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                getProfitList(true, "0")
            }

            override fun onLoadMore(page: Int) {
                getProfitList(false, adapter?.getLastId() ?: "")
            }

        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            btn_withdraw_money.id -> {
                if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type) {
                    OutToWalletActivity.start(this, CommonType.CERTIFICATION_OUTTOWALLET.type, canWithDraw.text.toString())
                } else {
                    OutToWalletActivity.start(this, CommonType.FACILITATOR_OUTTOWALLET.type, canWithDraw.text.toString())
                }
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {

    }

    private lateinit var permissionUtil: PermissionUtil
    private fun showDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("首媒客服电话")
        dialog.setDesc("400-966-1168")
        dialog.setPositiveText("呼叫")
        dialog.OnClickListener = View.OnClickListener {
            if (permissionUtil.callPermission()) {
                callPhone()
            }
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCode.PHONE.type) {
            if (permissionUtil.checkPermission(permissions)) {
                callPhone()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:400-966-1168"))
        startActivity(intent)
    }


    companion object {
        fun start(mContext: Context, type: String) {
            mContext.startActivity(Intent(mContext, IncomeActivity::class.java).putExtra("TYPE_INCOME", type))
        }
    }

    private fun getProfitInfo() {
        val url = if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type){
            Constant.MYSELF_MATCHMAKER_PROFIT_DATA
        }else{
            Constant.MYSELF_PROVIDER_PROFIT_DATA
        }
        ApiManager2.post(1, this, hashMapOf(), url, object : ApiManager2.OnResult<BaseBean<ProfitInfoModel>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<ProfitInfoModel>) {
                    tvMoney.text = data.message?.current
                    desc.text = data.message?.tip
                    totalProfit.text = data.message?.accumulate
                    canWithDraw.text = data.message?.available ?: "0"
            }


            override fun onCatch(data: BaseBean<ProfitInfoModel>) {

            }
        })
    }

    fun getProfitList(boolean: Boolean, currentId:String) {
        val map = HashMap<String, String>()
        map["flowno"] = currentId
        map["num"] = recyclerView.pageCount.toString()

        val url = if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type){
            Constant.MYSELF_MATCHMAKER_PROFIT_CONTENT
        }else{
            Constant.MYSELF_PROVIDER_PROFIT_CONTENT
        }

        ApiManager2.post(1, this, map, url, object : ApiManager2.OnResult<BaseBean<ArrayList<ProfitModel>>>() {
            override fun onFailed(code: String, message: String) {
                recyclerView.isRefreshing = false
                recyclerView.update(null)
            }

            override fun onSuccess(data: BaseBean<ArrayList<ProfitModel>>) {
                    if (boolean) {
                        adapter?.clear()
                        recyclerView.update(data.message)
                        list.addAll(data.message!!)
                        adapter?.notifyDatas()
                        if (data.message?.size != 0) {
                            tvShow.visibility = View.VISIBLE
                            tvShow.text = adapter?.getItemData(0)
                        } else {
                            tvShow.visibility = View.INVISIBLE
                        }
                    } else {
                        recyclerView.loadMore(data.message)
                        list.addAll(data.message?: arrayListOf())
                        adapter?.notifyDatas()
                    }
            }


            override fun onCatch(data: BaseBean<ArrayList<ProfitModel>>) {

            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        setIntent(intent)
        TYPE_INCOME = intent?.getStringExtra("TYPE_INCOME") ?: ""
        if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type) {

            btn_withdraw_money.text = "转出至钱包"
        } else {
            btn_withdraw_money.text = "转出至服务商钱包"
        }
        getProfitInfo()
        getProfitList(true, "0")
    }
}