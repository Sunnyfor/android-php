package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.mine.adapter.RedAccountListAdapter
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.MyWalletModel
import com.cocosh.shmstore.mine.model.RedWaterModel
import com.cocosh.shmstore.mine.presenter.RedWalletPresenter
import com.cocosh.shmstore.mine.ui.authentication.CommonType
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.DefineLoadMoreView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import kotlinx.android.synthetic.main.activity_red_account.*


/**
 * Created by lmg on 2018/4/17.
 */
class RedAccountActivity : BaseActivity(), MineContrat.IRedWalletView {
    var list = arrayListOf<RedWaterModel>()
    var count = 5
    var isClick = false
    var ruleMoney: String? = ""
    var money: String? = ""
    var mPresenter = RedWalletPresenter(this, this)
    override fun setLayout(): Int = R.layout.activity_red_account
    override fun redWalletData(result: BaseModel<MyWalletModel>) {
        if (result?.success && result.code == 200) {
            tvMoney.text = result.entity?.redBalance
//            rule.text = "注：红包金额必须>" + result.entity?.withdrawalMoney + "元时才可以转出!"
            ruleMoney = result.entity?.redBalance
            money = result.entity?.withdrawalMoney
            if (result.entity?.redBalance?.toDouble()!! > result.entity?.withdrawalMoney?.toDouble()!!) {
                btn_withdraw_money.setBackgroundResource(R.drawable.shape_btn_red)
                isClick = true
                return
            }
            btn_withdraw_money.setBackgroundResource(R.drawable.shape_btn_redpackage)
            isClick = false
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun redWalletWaterData(result: BaseModel<ArrayList<RedWaterModel>>) {
        if (result.success && result.code == 200) {
            if (result.entity != null) {
                if (result.entity!!.size == 0) {
                    recyclerView.loadMoreFinish(true, false);
                    return
                }
                list.addAll(result.entity!!)
                recyclerView.adapter.notifyDataSetChanged()
                recyclerView.loadMoreFinish(false, true)
            }
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun initView() {
        titleManager.defaultTitle("红包账户")
        btn_withdraw_money.setOnClickListener(this)
        mPresenter.requestRedWalletData(1)
        mPresenter.requestRedWalletWaterData(1, "", "", count.toString(), "", "")


        /**
         * 加载更多。
         */
        val mLoadMoreListener = SwipeMenuRecyclerView.LoadMoreListener {
            recyclerView.postDelayed(Runnable {
                mPresenter.requestRedWalletWaterData(0, list[list.size - 1]?.idUserAccountRecord
                        ?: "", "", count.toString(), "", "")
            }, 300)
        }

        // 自定义的核心就是DefineLoadMoreView类。
        val loadMoreView = DefineLoadMoreView(this)
        recyclerView.addFooterView(loadMoreView) // 添加为Footer。
        recyclerView.setLoadMoreView(loadMoreView) // 设置LoadMoreView更新监听。
        recyclerView.setLoadMoreListener(mLoadMoreListener) // 加载更多的监听。

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RedAccountListAdapter(list)
    }

    override fun onListener(view: View) {
        when (view.id) {
            btn_withdraw_money.id -> {
                if (isClick) OutToWalletActivity.start(this, CommonType.REDACCOUNT_OUTTOWALLET.type, ruleMoney
                        ?: "0") else {
                    ToastUtil.show("红包金额必须>" + money + "元时才可以转出!")
                }
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {
        mPresenter.requestRedWalletData(1)
        mPresenter.requestRedWalletWaterData(1, "", "", count.toString(), "", "")
    }


    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, RedAccountActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        list.clear()
        recyclerView.adapter.notifyDataSetChanged()
        mPresenter.requestRedWalletData(1)
        mPresenter.requestRedWalletWaterData(1, "", "", count.toString(), "", "")
    }
}