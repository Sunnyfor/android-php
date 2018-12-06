package com.cocosh.shmstore.newhome.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
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
import kotlinx.android.synthetic.main.fragment_bonus.*

class CollectionBonusFragment : BaseFragment(), MineContrat.ICollectionView {
    var listDatas = arrayListOf<NewCollection>()
    val pageCount = 20
    var pageNumber = "1"
    var timeStamp = ""
    var selectIndex = -2

    val mPresenter:CollectionPresenter by lazy {
        CollectionPresenter(getBaseActivity(),this)
    }
    var adapter: CollectionAdapter? = null

    override fun setLayout(): Int = R.layout.fragment_bonus

    override fun initView() {
        mPresenter.requestCollectionData(1, pageNumber, pageCount.toString(), timeStamp)
        sfSwiperefresh.setColorSchemeResources(R.color.blackText, R.color.blackText, R.color.blackText, R.color.blackText)


        /**
         * 加载更多。
         */

        val mLoadMoreListener = SwipeMenuRecyclerView.LoadMoreListener {
            pageNumber = listDatas.last().no
            mPresenter.requestCollectionData(0, pageNumber, pageCount.toString(), timeStamp)
        }

        // 自定义的核心就是DefineLoadMoreView类。
        val loadMoreView = DefineLoadMoreView(context)
        recyclerView.addFooterView(loadMoreView) // 添加为Footer。
        recyclerView.setLoadMoreView(loadMoreView) // 设置LoadMoreView更新监听。
        recyclerView.setLoadMoreListener(mLoadMoreListener) // 加载更多的监听。

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h50).toInt(), 0))
        adapter = CollectionAdapter(context, listDatas)
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
                val intent = Intent(context, BonusWebActivity::class.java)
                SmApplication.getApp().setData(DataCode.BONUS_ID, listDatas[index].no)
                intent.putExtra("title", listDatas[index].name)
                intent.putExtra("no", listDatas[index].no)
                intent.putExtra("collection", listDatas[index].status)
                intent.putExtra("state", listDatas[index].status)
                startActivity(intent)
            }

        })
    }

    override fun reTryGetData() {
        mPresenter.requestCollectionData(1, pageNumber.toString(), pageCount.toString(), timeStamp)
    }

    override fun onListener(view: View) {
    }

    override fun close() {
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

    override fun showError(type: Int) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onResume() {
        super.onResume()
        SmApplication.getApp().getData<BonusAction>(DataCode.BONUS, true)?.let {
            if (it == BonusAction.OPEN) {
                listDatas[selectIndex].status = "1"
            }

            if (it == BonusAction.GIVE) {
                listDatas[selectIndex].status = "2"
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