package com.cocosh.shmstore.newhome

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.newhome.fragment.GoodsListFragment
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class GoodsListActivity : BaseActivity() {

    private var isActive = false

    private val goodsListFragment: GoodsListFragment by lazy {
        GoodsListFragment()
    }

    override fun setLayout(): Int = R.layout.activity_goods_list

    override fun initView() {

        titleManager.defaultTitle(intent.getStringExtra("title"))

        isActive = intent.getBooleanExtra("isActive",false)

        val id = intent.getStringExtra("cate_id")

        supportFragmentManager.beginTransaction().add(R.id.content, goodsListFragment).commit()
        launch(UI) {
            delay(100)
            if (!isActive){
                goodsListFragment.loadData("0",id)
            }else{
                goodsListFragment.loadData(id)
            }
        }

    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }

    companion object {

        fun start(context: Context,cate_id:String,title:String,isActive:Boolean) {
            context.startActivity(Intent(context, GoodsListActivity::class.java)
                    .putExtra("title",title)
                    .putExtra("cate_id", cate_id)
                    .putExtra("isActive",isActive))
        }
    }
}