package com.cocosh.shmstore.newhome

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.ui.AddressMangerActivity
import com.cocosh.shmstore.newhome.adapter.GoodsDetailPhotoAdapter
import com.cocosh.shmstore.newhome.adapter.GoodsDetailShopAdapter
import kotlinx.android.synthetic.main.activity_goods_detail.*

class GoodsDetailActivity : BaseActivity() {
    private val dataList = ArrayList<String>()

    override fun setLayout(): Int = R.layout.activity_goods_detail

    override fun initView() {

        titleManager.defaultTitle(intent.getStringExtra("title")?:"")

        dataList.add("http://img.sccnn.com/bimg/339/12122.jpg")
        dataList.add("http://pic35.photophoto.cn/20150630/0018031349781196_b.jpg")
        dataList.add("http://img.sccnn.com/bimg/339/11732.jpg")
        dataList.add("http://img.sccnn.com/bimg/339/16606.jpg")
        dataList.add("http://img.sccnn.com/bimg/339/11254.jpg")
        dataList.add("http://img.sccnn.com/bimg/339/16088.jpg")

        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = GoodsDetailShopAdapter(dataList)

        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.isNestedScrollingEnabled = false
        recyclerView2.setHasFixedSize(true)
        recyclerView2.adapter = GoodsDetailPhotoAdapter(dataList)

        llAddress.setOnClickListener(this)
        llShop.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when (view.id){
            R.id.llAddress -> {
                startActivity(Intent(this,AddressMangerActivity::class.java))
            }
            R.id.llShop -> {
                startActivity(Intent(this,GoodsShoppingActivity::class.java))
            }
        }
    }

    override fun reTryGetData() {
    }
}