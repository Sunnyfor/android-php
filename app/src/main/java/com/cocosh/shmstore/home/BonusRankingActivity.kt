package com.cocosh.shmstore.home

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.home.adapter.BonusRankingAdapter
import com.cocosh.shmstore.home.model.BonusRanking
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_ranking.*

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
        idRedPacketBaseInfo = intent.getStringExtra("comment_id")

        refreshLayout.recyclerView.layoutManager = LinearLayoutManager(this)

        refreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page
                loadData(true)
            }

            override fun onLoadMore(page: Int) {
//                currentPage = page
//                loadData(false)
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
//        idRedPacketBaseInfo?.let {
//            params["idRedPacketOrderInfo"] = it
//            params["currentPage"] = currentPage.toString()
//            params["showCount"] = refreshLayout.pageCount.toString()
//        }
        params["num"] = "30"

        ApiManager2.post(0, this, params, Constant.RP_RANK, object : ApiManager2.OnResult<BaseBean<BonusRanking>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<BonusRanking>) {
                data.message?.let {
                    //                        if (boolean) {
                    refreshLayout.recyclerView.adapter = BonusRankingAdapter(it)
                    it.mine?.let {
                        when (it.rank?.toInt()) {
                            1, 2, 3 -> tvNo.setTextColor(ContextCompat.getColor(this@BonusRankingActivity, R.color.red))
                            else -> tvNo.setTextColor(ContextCompat.getColor(this@BonusRankingActivity, R.color.textGray))
                        }

                        tvNo.text = it.rank
                        tvName.text = it.nickname
                        tvMoney.text = (it.amount + " 元")
                        it.avatar?.let {
                            if (it.isNotEmpty())
                                GlideUtils.loadHead(this@BonusRankingActivity, it, ivPhoto)
                        }
                    }
                    refreshLayout.update(it.list)
//                        } else {
//
//                                refreshLayout.loadMore(it.rankingListVOS)
//                        }
                }
            }


            override fun onCatch(data: BaseBean<BonusRanking>) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        if (!UserManager2.isLogin()) {
            finish()
        }
    }
}