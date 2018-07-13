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
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.InComerListAdapter
import com.cocosh.shmstore.mine.model.ProfitInfoModel
import com.cocosh.shmstore.mine.model.ProfitModel
import com.cocosh.shmstore.mine.ui.mywallet.OutToWalletActivity
import com.cocosh.shmstore.utils.PermissionCode
import com.cocosh.shmstore.utils.PermissionUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_income.*
import java.util.*
import kotlin.collections.ArrayList


/**
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
            getProfitInfo("1")
            getProfitList(true, "0", "1")
            btn_withdraw_money.text = "转出至钱包"
        } else {
            getProfitInfo("2")
            getProfitList(true, "0", "2")
            btn_withdraw_money.text = "转出至服务商钱包"
        }
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
                if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type) {
                    getProfitList(true, "0", "1")
                } else {
                    getProfitList(true, "0", "2")
                }
            }

            override fun onLoadMore(page: Int) {
                if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type) {
                    getProfitList(false, adapter?.getLastId() ?: "", "1")
                } else {
                    getProfitList(false, adapter?.getLastId() ?: "", "2")
                }
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

    fun getProfitInfo(personType: String) {
        val map = HashMap<String, String>()
        map["personType"] = personType
        ApiManager.get(1, this, map, Constant.PROFITINFO_INFO, object : ApiManager.OnResult<BaseModel<ProfitInfoModel>>() {
            override fun onSuccess(data: BaseModel<ProfitInfoModel>) {
                if (data.success && data.code == 200) {
                    tvMoney.text = data.entity?.profit
                    desc.text = data.entity?.accountPeriodStr
                    totalProfit.text = data.entity?.aggregateAmounts
                    canWithDraw.text = data.entity?.canWithdraw ?: "0"
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<ProfitInfoModel>) {

            }
        })
    }

    fun getProfitList(boolean: Boolean, currentId: String, personType: String) {
        val map = HashMap<String, String>()
        map["currentId"] = currentId
        map["showCount"] = recyclerView.pageCount.toString()
        map["personType"] = personType
        ApiManager.get(1, this, map, Constant.PROFITINFO_LIST, object : ApiManager.OnResult<BaseModel<ArrayList<ProfitModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<ProfitModel>>) {
                if (data.success && data.code == 200) {
                    if (boolean) {
                        adapter?.clear()
                        recyclerView.update(data.entity)
                        list.addAll(data.entity!!)
                        adapter?.notifyDatas()
                        if (data.entity?.size != 0) {
                            tvShow.visibility = View.VISIBLE
                            tvShow.text = adapter?.getItemData(0)
                        } else {
                            tvShow.visibility = View.INVISIBLE
                        }
                    } else {
                        recyclerView.loadMore(data.entity)
                        list.addAll(data.entity!!)
                        adapter?.notifyDatas()
                    }
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<ArrayList<ProfitModel>>) {

            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        setIntent(intent)
        TYPE_INCOME = intent?.getStringExtra("TYPE_INCOME") ?: ""
        if (TYPE_INCOME == CommonType.CERTIFICATION_INCOME.type) {
            getProfitInfo("1")
            getProfitList(true, "0", "1")
            btn_withdraw_money.text = "转出至钱包"
        } else {
            getProfitInfo("2")
            getProfitList(true, "0", "2")
            btn_withdraw_money.text = "转出至服务商钱包"
        }
    }
}