package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
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
import com.cocosh.shmstore.newhome.fragment.CollectionBonusFragment
import com.cocosh.shmstore.newhome.fragment.CollectionGoodsFragment
import com.cocosh.shmstore.newhome.fragment.CollectionShopFragment
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.widget.DefineLoadMoreView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * Created by lmg on 2018/4/25.
 * 收藏
 */
class CollectionActivity : BaseActivity() {

    private val fragments = arrayListOf<BaseFragment>()

    private val collectionBonusFragment: CollectionBonusFragment by lazy {
        CollectionBonusFragment()
    }

    private val collectionGoodsFragment: CollectionGoodsFragment by lazy {
        CollectionGoodsFragment()
    }

    private val collectionShopFragment: CollectionShopFragment by lazy {
        CollectionShopFragment()
    }

    private val titleString = arrayListOf(
            "红包", "商品", "店铺")

    override fun setLayout(): Int = R.layout.activity_collection

    override fun initView() {
        titleManager.defaultTitle("我的收藏")

        fragments.add(collectionBonusFragment)
        fragments.add(collectionGoodsFragment)
        fragments.add(collectionShopFragment)

        tabLayout.setupWithViewPager(viewPager)

        viewPager.adapter = object :FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getCount(): Int = fragments.size

            override fun getPageTitle(position: Int): CharSequence {
                return titleString[position]
            }
        }


    }


    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, CollectionActivity::class.java))
        }
    }



}