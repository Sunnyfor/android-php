package com.cocosh.shmstore.newhome

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newhome.fragment.GoodsSearchFragment
import com.cocosh.shmstore.title.SearchTitleFragment
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.ViewUtil
import kotlinx.android.synthetic.main.activity_goods_search.*

class GoodsSearchActivity : BaseActivity() {
    private var type = false
    private var keyWord = ""
    private val searchTitleFragment: SearchTitleFragment by lazy {
        SearchTitleFragment()
    }
    private val goodsSearchFragment: GoodsSearchFragment by lazy {
        GoodsSearchFragment()
    }

    private val titleList = arrayListOf(
            "商品",
            "店铺")


    override fun setLayout(): Int = R.layout.activity_goods_search

    override fun initView() {

        searchTitleFragment.onKeyWord = {
            if (keyWord.isEmpty()) {
                ToastUtil.show("请输入关键字")
            } else {
                if (type) {
                    goodsSearchFragment.searchShop(keyWord)
                } else {
                    goodsSearchFragment.searchGoods(keyWord)
                }
            }
        }

        searchTitleFragment.onKeyWordChanger = {
            keyWord = it
        }

        titleManager.addTitleFragment(searchTitleFragment)

        supportFragmentManager.beginTransaction().add(R.id.content, goodsSearchFragment).commit()

        tab.tabMode = TabLayout.MODE_FIXED

        //添加tab选项卡
        titleList.forEach {
            tab.addTab(tab.newTab().setText(it))
        }

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                type = tab.position != 0
                goodsSearchFragment.pager = "0"
                    if (type) {
                        goodsSearchFragment.searchShop(keyWord)
                    } else {
                        goodsSearchFragment.searchGoods(keyWord)
                    }
            }

        })
    }

    override fun onStart() {
        super.onStart()
        tab.let {
            it.post { ViewUtil.setIndicator(it, resources.getDimension(R.dimen.w200).toInt(), resources.getDimension(R.dimen.w200).toInt()) }
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }


}