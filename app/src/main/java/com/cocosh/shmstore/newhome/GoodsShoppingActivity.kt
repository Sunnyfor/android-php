package com.cocosh.shmstore.newhome

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newhome.fragment.GoodsListFragment
import kotlinx.android.synthetic.main.activity_goods_shopping.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class GoodsShoppingActivity : BaseActivity() {
    private val goodsListFragment:GoodsListFragment by lazy {
        GoodsListFragment()
    }
    override fun setLayout(): Int = R.layout.activity_goods_shopping

    override fun initView() {
        supportFragmentManager.beginTransaction().add(R.id.content,goodsListFragment).commit()
        launch(UI) {
            delay(100)
            goodsListFragment.loadData("","")
        }
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}