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

    private val searchTitleFragment: SearchTitleFragment by lazy {
        SearchTitleFragment()
    }
    private val goodsSearchFragment:GoodsSearchFragment by lazy {
        GoodsSearchFragment()
    }

    private val titleList = arrayListOf(
            "全部",
            "店铺")



    override fun setLayout(): Int = R.layout.activity_goods_search

    override fun initView() {

        searchTitleFragment.onKeyWord = { keyword: String, isState: Boolean ->
            if (keyword.isEmpty()) {
                if (isState){

                }else{
                    ToastUtil.show("请输入关键字")
                }
            } else {
                goodsSearchFragment.searchGoods(keyword,type)
            }
        }
        titleManager.addTitleFragment(searchTitleFragment)

        supportFragmentManager.beginTransaction().add(R.id.content,goodsSearchFragment).commit()

        tab.tabMode = TabLayout.MODE_FIXED

        //添加tab选项卡
        titleList.forEach {
            tab.addTab(tab.newTab().setText(it))
        }

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