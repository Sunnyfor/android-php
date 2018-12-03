package com.cocosh.shmstore.newhome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.webkit.WebSettings
import com.bumptech.glide.Glide
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
import com.cocosh.shmstore.utils.GoodsDetailActivityManager
import com.cocosh.shmstore.widget.dialog.GoodsDetailDialog
import kotlinx.android.synthetic.main.activity_goods_detail.*

class GoodsDetailActivity : BaseActivity() {
    private var goodsId = "1"
    private var goodsDetail: GoodsDetail? = null
    override fun setLayout(): Int = R.layout.activity_goods_detail

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        GoodsDetailActivityManager.addActivity(this)
        GoodsDetailActivityManager.removeFirstActivity()

        titleManager.defaultTitle(intent.getStringExtra("title") ?: "")

        goodsId = intent.getStringExtra("goods_id") ?: ""


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


//        webView.settings.useWideViewPort = true
//        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
//        webView.settings.loadWithOverviewMode = true
//        webView.settings.javaScriptEnabled = true
//        recyclerView2.layoutManager = LinearLayoutManager(this)
//        recyclerView2.isNestedScrollingEnabled = false
//        recyclerView2.setHasFixedSize(true)

        llFormat.setOnClickListener(this)
        llAddress.setOnClickListener(this)
        llShop.setOnClickListener(this)
        loadData()
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.llAddress -> {
                startActivity(Intent(this, AddressMangerActivity::class.java))
            }
            R.id.llShop -> {
//                startActivity(Intent(this, GoodsShoppingActivity::class.java))
            }
            R.id.llFormat -> {
                goodsDetail?.let {
                    val goodsDetailDialog = GoodsDetailDialog(this, it)
                    goodsDetailDialog.show()
                }
            }
//
        }
    }

    override fun reTryGetData() {
    }


    fun loadData() {
        val params = hashMapOf(
                Pair("goods_id", goodsId),
                Pair("goods_id", goodsId))
        ApiManager2.get(this, params, Constant.ESHOP_GOODS_DETAIL, object : ApiManager2.OnResult<BaseBean<GoodsDetail>>() {
            override fun onSuccess(data: BaseBean<GoodsDetail>) {
                data.message?.let {
                    goodsDetail = it
                    initBanner(it.goods.image)
                    text_goods_name.text = it.goods.name
                    text_goods_price.text = ("¥ " + it.goods.price)


                    Glide.with(this@GoodsDetailActivity).load(it.store.logo)
                            .placeholder(ColorDrawable(ContextCompat.getColor(this@GoodsDetailActivity, R.color.activity_bg)))
                            .into(ivPhoto)
                    tvName.text = it.store.name
                    tvCount.text = (it.store.total + "件在售商品")
                    if (it.store.attention == "1") {
                        btnFollow.setBackgroundResource(R.mipmap.ic_shop_cancel_follow)
                    } else {
                        btnFollow.setBackgroundResource(R.mipmap.ic_shop_follow)
                    }
                    recyclerView.adapter = GoodsDetailShopAdapter(it.store.goods ?: arrayListOf())

                    val html = "<html><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1, minimum-scale=1, maximum-scale=1,user-scalable=no\">"+"<meta http-equiv=Content-Type content=\"text/html; charset=gb2312\">"+"</head>"+"<body style=\"margin:0;padding:0\">"+
                            it.detail.replace("<img", "<img width=100%")+"</body></html>"
                    webView.loadDataWithBaseURL(null,html, "text/html", "gb2312",null)
//                    recyclerView2.adapter = GoodsDetailPhotoAdapter(it.detail)
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<GoodsDetail>) {
            }

        })

    }


    fun initBanner(bannerList: ArrayList<String>) {
        txt_all.text = bannerList.size.toString()
        viewPager.offscreenPageLimit = bannerList.size
        viewPager.adapter = GoodsBannerAdapter(this, bannerList)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                txt_current.text = (position + 1).toString()
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    companion object {

        fun start(context: Context, title: String, good_id: String) {
            context.startActivity(Intent(context, GoodsDetailActivity::class.java)
                    .putExtra("title", title)
                    .putExtra("goods_id", good_id))
        }
    }

    override fun onDestroy() {
        GoodsDetailActivityManager.removeActivity(this)
        super.onDestroy()
    }
}