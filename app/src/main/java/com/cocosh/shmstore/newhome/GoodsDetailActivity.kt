package com.cocosh.shmstore.newhome

import android.content.Intent
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.AddressMangerActivity
import com.cocosh.shmstore.newhome.adapter.GoodsBannerAdapter
import com.cocosh.shmstore.newhome.adapter.GoodsDetailPhotoAdapter
import com.cocosh.shmstore.newhome.adapter.GoodsDetailShopAdapter
import com.cocosh.shmstore.newhome.model.GoodsDetail
import kotlinx.android.synthetic.main.activity_goods_detail.*

class GoodsDetailActivity : BaseActivity() {
    private val dataList = ArrayList<String>()
    private var goodsId = "1"
    override fun setLayout(): Int = R.layout.activity_goods_detail

    override fun initView() {

        titleManager.defaultTitle(intent.getStringExtra("title")?:"")

        goodsId = intent.getStringExtra("goods_id")?:""

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

        llAddress.setOnClickListener(this)
        llShop.setOnClickListener(this)
        loadData()
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


    fun loadData(){
        val params = hashMapOf(Pair("goods_id",goodsId))
        ApiManager2.get(this,params,Constant.ESHOP_GOODS_DETAIL,object : ApiManager2.OnResult<BaseBean<GoodsDetail>>(){
            override fun onSuccess(data: BaseBean<GoodsDetail>) {
                data.message?.let {
                    initBanner(it.goods.image)
                    text_goods_name.text = it.goods.name
                    text_goods_price.text = ("¥ "+it.goods.price)
                    recyclerView2.adapter = GoodsDetailPhotoAdapter(it.detail)
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<GoodsDetail>) {
            }

        })

    }


    fun initBanner(bannerList:ArrayList<String>){
        txt_all.text = bannerList.size.toString()
        viewPager.offscreenPageLimit = bannerList.size
        viewPager.adapter = GoodsBannerAdapter(this,bannerList)
        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                txt_current.text = (position + 1).toString()
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

}