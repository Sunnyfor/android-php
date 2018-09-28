package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.home.BonusWebActivity
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.mine.adapter.CollectionAdapter
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.NewCollection
import com.cocosh.shmstore.mine.presenter.CollectionPresenter
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.widget.DefineLoadMoreView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * Created by lmg on 2018/4/25.
 * 收藏
 */
class CollectionActivity : BaseActivity(), MineContrat.ICollectionView {
    var listDatas = arrayListOf<NewCollection>()
    val pageCount = 20
    var pageNumber = "1"
    var timeStamp = ""
    var selectIndex = -2
    var mPresenter = CollectionPresenter(this, this)
    var adapter: CollectionAdapter? = null
    override fun setLayout(): Int = R.layout.activity_collection

    override fun initView() {
        titleManager.defaultTitle("收藏")
        mPresenter.requestCollectionData(1, pageNumber.toString(), pageCount.toString(), timeStamp)
        sfSwiperefresh.setColorSchemeResources(R.color.blackText, R.color.blackText, R.color.blackText, R.color.blackText)

        /**
         * 加载更多。
         */
        val mLoadMoreListener = SwipeMenuRecyclerView.LoadMoreListener {
            recyclerView.postDelayed(Runnable {
                pageNumber = listDatas.last().no
                mPresenter.requestCollectionData(0, pageNumber.toString(), pageCount.toString(), timeStamp)
            }, 300)
        }

        // 自定义的核心就是DefineLoadMoreView类。
        val loadMoreView = DefineLoadMoreView(this)
        recyclerView.addFooterView(loadMoreView) // 添加为Footer。
        recyclerView.setLoadMoreView(loadMoreView) // 设置LoadMoreView更新监听。
        recyclerView.setLoadMoreListener(mLoadMoreListener) // 加载更多的监听。

        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h50).toInt(), 0))
        adapter = CollectionAdapter(this, listDatas)
        recyclerView.adapter = adapter

        //下拉刷新
        sfSwiperefresh.setOnRefreshListener {
            //拉取默认数据
            pageNumber = "1"
            timeStamp = ""
            listDatas.clear()
            recyclerView.adapter.notifyDataSetChanged()
            sfSwiperefresh.isRefreshing = true
            mPresenter.requestCollectionData(0, pageNumber.toString(), pageCount.toString(), timeStamp)
        }

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                selectIndex = index
                val intent = Intent(this@CollectionActivity, BonusWebActivity::class.java)
                SmApplication.getApp().setData(DataCode.BONUS_ID, listDatas[index].no)
                intent.putExtra("title", listDatas[index].name)
                intent.putExtra("no", listDatas[index].no)
                intent.putExtra("collection", listDatas[index].status)
                if (listDatas[index].status == "1") {//未打开
                    intent.putExtra("state", "SEIZE")
                }

                if (listDatas[index].status == "2") { //已打开
                    intent.putExtra("state", "RECEIVE")
                }

                if (listDatas[index].status == "1") {//已赠送
                    intent.putExtra("state", "ISGIVE")
                }
                startActivity(intent)
            }

        })
    }


    override fun collection(result: BaseBean<ArrayList<NewCollection>>) {
        sfSwiperefresh.isRefreshing = false
        if (result.message != null){
            if (result.message?.size == 0) {
                recyclerView.loadMoreFinish(true, false)
                return
            }
            listDatas.addAll(result.message!!)
            recyclerView.adapter.notifyDataSetChanged()
            recyclerView.loadMoreFinish(false, true)
        }else{
            recyclerView.loadMoreFinish(true, false)
        }
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {
        mPresenter.requestCollectionData(1, pageNumber.toString(), pageCount.toString(), timeStamp)
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, CollectionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        SmApplication.getApp().getData<BonusAction>(DataCode.BONUS, true)?.let {
            if (it == BonusAction.OPEN) {
                listDatas[selectIndex].status = "2"
            }

            if (it == BonusAction.GIVE) {
                listDatas[selectIndex].status = "1"
            }
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        pageNumber = "1"
        timeStamp = ""
        super.onDestroy()
    }
}