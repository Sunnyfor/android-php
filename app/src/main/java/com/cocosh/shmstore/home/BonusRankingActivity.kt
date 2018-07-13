package com.cocosh.shmstore.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.BonusRankingAdapter
import com.cocosh.shmstore.home.model.BonusRanking
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_ranking.view.*

/**
 * 红包排行榜
 * Created by zhangye on 2018/4/24.
 */
class BonusRankingActivity : BaseActivity() {
    var currentPage = 1
    private var idRedPacketBaseInfo: String? = null

    override fun setLayout(): Int = R.layout.activity_ranking

    override fun initView() {
        titleManager.defaultTitle("红包排行榜")
        isShowLoading = false
        idRedPacketBaseInfo = intent.getStringExtra("id")

        refreshLayout.recyclerView.layoutManager = LinearLayoutManager(this)

        refreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                loadData(true)
            }

            override fun onLoadMore(page: Int) {
                currentPage = page
                loadData(false)
            }

        }

        loadData(true)
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    private fun loadData(boolean: Boolean) {
        val params = hashMapOf<String, String>()
        idRedPacketBaseInfo?.let {
            params["idRedPacketOrderInfo"] = it
            params["currentPage"] = currentPage.toString()
            params["showCount"] = refreshLayout.pageCount.toString()
        }
        ApiManager.get(0, this, params, Constant.BONUS_RANKING, object : ApiManager.OnResult<BaseModel<BonusRanking>>() {
            override fun onSuccess(data: BaseModel<BonusRanking>) {
                if (data.success) {
                    data.entity?.let {
                        if (boolean) {
                            refreshLayout.recyclerView.adapter = BonusRankingAdapter(it)
                            it.resMyRankingVO?.let {
                                tvNo.text = it.myRank
                                tvName.text = it.myUserName
                                tvMoney.text = (it.myTotalAmount + " 元")
                                it.myHeadPic?.let {
                                    if (it.isNotEmpty())
                                        GlideUtils.loadHead(this@BonusRankingActivity, it, ivPhoto)
                                }
                            }
                                refreshLayout.update(it.rankingListVOS)
                        } else {

                                refreshLayout.loadMore(it.rankingListVOS)
                        }
                    }
                }else{
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<BonusRanking>) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        if (!UserManager.isLogin()) {
            finish()
        }
    }
}