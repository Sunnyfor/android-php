package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.MoneyWaterListAdapter
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.WalletWaterModel
import com.cocosh.shmstore.mine.presenter.EntWalletWaterPresenter
import com.cocosh.shmstore.utils.PickerViewUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.DefineLoadMoreView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import kotlinx.android.synthetic.main.activity_money_water.*


/**
 * Created by lmg on 2018/4/17.
 */
class MoneyWaterActivity : BaseActivity(), MineContrat.IEntWalletWaterView {
    var selectBegTime = ""
    var mPresenter = EntWalletWaterPresenter(this, this)
    private var TYPE_WATERLIST = ""//type_my我的钱包流水"type_enterprise"企业liushui
    private lateinit var pickerViewUtils: PickerViewUtils
    override fun setLayout(): Int = R.layout.activity_money_water

    override fun entWalletWaterData(result: BaseBean<ArrayList<WalletWaterModel>>) {
            if (result.message?.size?:0 == 0) {
                recyclerView.loadMoreFinish(true, false)
                return
            }
            list.addAll(result.message!!)
            recyclerView.adapter.notifyDataSetChanged()
            recyclerView.loadMoreFinish(false, true)
    }

    override fun walletWaterData(result: BaseBean<ArrayList<WalletWaterModel>>) {
            if (result.message?.size?:0 == 0) {
                recyclerView.loadMoreFinish(true, false);
                return
            }
            list.addAll(result.message!!)
            recyclerView.adapter.notifyDataSetChanged()
            recyclerView.loadMoreFinish(false, true);
    }

    override fun initView() {
        var title = ""
        TYPE_WATERLIST = intent.getStringExtra("TYPE_WATERLIST")

//        val dateYYMMddFormatToTimeStamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        var urlStr = ""
//        try {
//            urlStr = URLEncoder.encode(dateYYMMddFormatToTimeStamp.format(System.currentTimeMillis()), "Utf-8")
//        } catch (e: UnsupportedEncodingException) {
//            e.printStackTrace()
//        }

        if (TYPE_WATERLIST == Constant.TYPE_ENTERPRISE) {
            title = "企业余额明细"
            mPresenter.requestEntWalletWaterData(1, "", "", "20")
        } else {
            title = "财富额明细"
            mPresenter.requestWalletWaterData(1, "", "", "20")
        }

        titleManager.rightText(title, resources.getString(R.string.iconCalendar), View.OnClickListener {
            //日历
            pickerViewUtils.showDateYYMM(object : PickerViewUtils.OnPickerViewResultListener {
                override fun onPickerViewResult(value: String) {
                    list.clear()
                    recyclerView.adapter.notifyDataSetChanged()
                    selectBegTime = value
                    if (TYPE_WATERLIST == Constant.TYPE_ENTERPRISE) {
                        mPresenter.requestEntWalletWaterData(0, "", selectBegTime, "20")
                    } else {
                        mPresenter.requestWalletWaterData(0, "", selectBegTime, "20")
                    }
                }
            })
        })
        pickerViewUtils = PickerViewUtils(this)

        /**
         * 加载更多。
         */
        val mLoadMoreListener = SwipeMenuRecyclerView.LoadMoreListener {
            recyclerView.postDelayed({
                if (TYPE_WATERLIST == Constant.TYPE_ENTERPRISE) {
                    mPresenter.requestEntWalletWaterData(0, list.last().flowno
                            ?: "", selectBegTime, "10")
                } else {
                    mPresenter.requestWalletWaterData(0, list.last().flowno
                            ?: "", selectBegTime, "10")
                }
            }, 300)
        }

        // 自定义的核心就是DefineLoadMoreView类。
        val loadMoreView = DefineLoadMoreView(this)
        recyclerView.addFooterView(loadMoreView) // 添加为Footer。
        recyclerView.setLoadMoreView(loadMoreView) // 设置LoadMoreView更新监听。
        recyclerView.setLoadMoreListener(mLoadMoreListener) // 加载更多的监听。

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MoneyWaterListAdapter(list)
    }

    var list = arrayListOf<WalletWaterModel>()
    override fun onListener(view: View) {

    }

    override fun reTryGetData() {
        if (TYPE_WATERLIST == Constant.TYPE_ENTERPRISE) {
            mPresenter.requestEntWalletWaterData(1, "", "", "10")
        } else {
            mPresenter.requestWalletWaterData(1, "", "", "10")
        }
    }

    companion object {
        fun start(mContext: Context, type: String) {
            mContext.startActivity(Intent(mContext, MoneyWaterActivity::class.java).putExtra("TYPE_WATERLIST", type))
        }
    }
}