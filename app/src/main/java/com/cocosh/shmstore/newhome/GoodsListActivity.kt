package com.cocosh.shmstore.newhome

import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newhome.fragment.GoodsListFragment
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class GoodsListActivity : BaseActivity() {

    private val goodsListFragment:GoodsListFragment by lazy {
        GoodsListFragment()
    }

    override fun setLayout(): Int = R.layout.activity_goods_list

    override fun initView() {

        titleManager.defaultTitle(intent.getStringExtra("title"))

        supportFragmentManager.beginTransaction().add(R.id.content,goodsListFragment).commit()
        launch(UI) {
            delay(100)
            goodsListFragment.loadData("0",intent.getStringExtra("cate_id"))
        }

    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }
}