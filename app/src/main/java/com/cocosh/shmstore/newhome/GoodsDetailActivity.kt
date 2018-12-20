package com.cocosh.shmstore.newhome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.ui.activity.LoginActivity
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.Address
import com.cocosh.shmstore.mine.presenter.AddRessPresenter
import com.cocosh.shmstore.mine.ui.AddressMangerActivity
import com.cocosh.shmstore.newhome.adapter.GoodsBannerAdapter
import com.cocosh.shmstore.newhome.adapter.GoodsCommentListAdapter
import com.cocosh.shmstore.newhome.adapter.GoodsDetailShopAdapter
import com.cocosh.shmstore.newhome.adapter.GoodsParamsAdapter
import com.cocosh.shmstore.newhome.model.CommentBean
import com.cocosh.shmstore.newhome.model.GoodsDetail
import com.cocosh.shmstore.newhome.model.GoodsFavEvent
import com.cocosh.shmstore.newhome.model.Shop
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.GoodsDetailActivityManager
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.GoodsDetailDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_goods_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class GoodsDetailActivity : BaseActivity(), MineContrat.IAddressView {

    private var goodsId = "1"
    private var goodsDetail: GoodsDetail? = null
    private var skuid = "0"
    private var count = "1"

    val mPresenter: AddRessPresenter  by lazy {
        AddRessPresenter(this, this)
    }

    override fun deleteAddress(result: String) {
    }

    override fun getAddress(result: BaseBean<ArrayList<Address>>) {
        result.message?.let { it ->
            if (it.isNotEmpty()) {
                val address = it.last { it.default == "1" }
                txt_address.text = (address.district + " " + address.addr)
            }
        }
    }

    override fun defaultAddress(result: String) {

    }


    override fun setLayout(): Int = R.layout.activity_goods_detail

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        GoodsDetailActivityManager.addActivity(this)
        GoodsDetailActivityManager.removeFirstActivity()

        titleManager.defaultTitle(intent.getStringExtra("title") ?: "")

        goodsId = intent.getStringExtra("goods_id") ?: ""


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView_params.layoutManager = LinearLayoutManager(this)
        recyclerView_params.setHasFixedSize(true)
        recyclerView_params.isNestedScrollingEnabled = false

        text_add_car.setOnClickListener(this)
        llFormat.setOnClickListener(this)
        llAddress.setOnClickListener(this)
        llShop.setOnClickListener(this)
        rl_shop.setOnClickListener(this)
        llCollect.setOnClickListener(this)
        btnFollow.setOnClickListener(this)
        btn_buy.setOnClickListener(this)
        EventBus.getDefault().register(this)
        txt_go_shop.setOnClickListener(this)

        rl_commend_more.setOnClickListener(this)

        loadData()
        loadComment()
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.llAddress -> {
                if (UserManager2.isLogin()) {
                    startActivity(Intent(this, AddressMangerActivity::class.java).putExtra("type", "buy"))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            R.id.llShop -> {
                GoodsShoppingActivity.start(this, goodsDetail?.store?.name
                        ?: "", goodsDetail?.store?.id ?: "")
//                startActivity(Intent(this, GoodsShoppingActivity::class.java))
            }
            R.id.llFormat -> {
                showAddCar()
            }
            R.id.llCollect -> {
                if (UserManager2.isLogin()) {
                    favGoods()
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }

            R.id.text_add_car -> {
                showAddCar()
            }
            R.id.rl_shop -> {
                GoodsShoppingActivity.start(this, goodsDetail?.store?.name
                        ?: "", goodsDetail?.store?.id ?: "")
            }
            R.id.btnFollow -> {
                favShop()
            }
            R.id.btn_buy -> {
                showAddCar()
            }

            R.id.txt_go_shop -> {
                GoodsShoppingActivity.start(this, goodsDetail?.store?.name
                        ?: "", goodsDetail?.store?.id ?: "")
            }

            R.id.rl_commend_more -> {
                GoodsCommentListActivity.start(this,goodsId)
            }

        }
    }

    override fun reTryGetData() {
    }


    fun loadData() {
        val params = hashMapOf(
                Pair("goods_id", goodsId))
        ApiManager2.get(this, params, Constant.ESHOP_GOODS_DETAIL, object : ApiManager2.OnResult<BaseBean<GoodsDetail>>() {
            override fun onSuccess(data: BaseBean<GoodsDetail>) {
                data.message?.let { it ->
                    goodsDetail = it
                    it.goods?.image?.let {
                        initBanner(it)
                    }
                    text_goods_name.text = it.goods?.name
                    text_goods_price.text = ("¥ " + it.goods?.price)

                    it.goods?.sku?.let { sku ->
                        if (sku.desc?.isNotEmpty() == true) {
                            val ele = StringBuilder()
                            sku.desc!![0].ele.forEach {
                                ele.append(it.value).append("，")
                            }
                            tvEle.text = (ele.toString() + "1件")
                            skuid = sku.desc!![0].id
                        }
                    }

                    Glide.with(this@GoodsDetailActivity).load(it.store.logo)
                            .dontAnimate()
                            .placeholder(ColorDrawable(ContextCompat.getColor(this@GoodsDetailActivity, R.color.activity_bg)))
                            .into(ivPhoto)
                    tvName.text = it.store.name
                    tvCount.text = (it.store.total + "件在售商品")

                    if (it.store.attention == "1") {
                        btnFollow.setBackgroundResource(R.mipmap.ic_shop_cancel_follow)
                    } else {
                        btnFollow.setBackgroundResource(R.mipmap.ic_shop_follow)
                    }

                    if (it.goods?.fav == "1") {
                        view_collect.setBackgroundResource(R.mipmap.ic_goods_collect_red)
                    } else {
                        view_collect.setBackgroundResource(R.mipmap.ic_goods_collect)
                    }

                    recyclerView.adapter = GoodsDetailShopAdapter(it.store.goods ?: arrayListOf())


                    val contentSb = StringBuilder()

                    val source = it.detail.split("</p>")
                    source.forEach {

                        if (it.contains("<img")) {
                            contentSb.append(it.replace("&nbsp;", "").replace("<br>", ""))
                        } else {
                            contentSb.append(it)
                        }
                        contentSb.append("</p>")
                    }

                    val style = "<style>img{display:block;vertical-align:bottom;float:left;width:100%;}*{padding:0;margin:0;}</style>"
                    val html = "<html><head><meta http-equiv=Content-Type content=\"text/html; charset=gb2312\"></head>$style<body>$contentSb</body></html>"
                    webView.loadDataWithBaseURL(null, html, "text/html", "gb2312", null)
//                    recyclerView2.adapter = GoodsDetailPhotoAdapter(it.detail)

                    it.goods?.params?.let {
                        if (it.isNotEmpty()) {
                            ll_goods_params.visibility = View.VISIBLE
                            recyclerView_params.adapter = GoodsParamsAdapter(it)
                        }

                    }


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
        super.onDestroy()
        SmApplication.getApp().removeData(DataCode.ADDRESS)
        GoodsDetailActivityManager.removeActivity(this)
        EventBus.getDefault().unregister(this)
    }

    private fun showAddCar() {
        goodsDetail?.let {
            val goodsDetailDialog = GoodsDetailDialog(skuid, count, this, it) { resultStr: String, skuId: String, count: String ->
                tvEle.text = resultStr
                this.skuid = skuId
                this.count = count
            }
            goodsDetailDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val address = SmApplication.getApp().getData<Address>(DataCode.ADDRESS, false)
        if (address == null) {
            mPresenter.requestGetAddress(0)
        } else {
            txt_address.text = (address.district + " " + address.addr)
        }
    }


    //收藏商品方法
    private fun favGoods() {

        if (!UserManager2.isLogin()) {
            SmediaDialog(this@GoodsDetailActivity).showLogin()
            return
        }

        val params = hashMapOf<String, String>()
        params["goods_id"] = goodsId
        params["op"] = if (goodsDetail?.goods?.fav == "1") "2" else "1"
        ApiManager2.post(this, params, Constant.ESHOP_FAV_GOODS, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                if (goodsDetail?.goods?.fav == "1") {
                    goodsDetail?.goods?.fav = "0"
                } else {
                    goodsDetail?.goods?.fav = "1"
                }
                EventBus.getDefault().post(GoodsFavEvent(goodsId, goodsDetail?.goods?.fav ?: "0"))
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }

    private fun favShop() {
        if (!UserManager2.isLogin()) {
            SmediaDialog(this@GoodsDetailActivity).showLogin()
            return
        }
        val params = hashMapOf<String, String>()
        params["store_id"] = goodsDetail?.store?.id ?: ""
        params["op"] = if (goodsDetail?.store?.attention == "0") "1" else "2"
        ApiManager2.post(this, params, Constant.ESHOP_FAV_STORE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                if (goodsDetail?.store?.attention == "0") {
                    goodsDetail?.store?.attention = "1"
                    btnFollow.setBackgroundResource(R.mipmap.ic_shop_cancel_follow)
                } else {
                    goodsDetail?.store?.attention = "0"
                    btnFollow.setBackgroundResource(R.mipmap.ic_shop_follow)
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }


    private fun loadComment(){
        val params = HashMap<String,String>()
        params["goods_id"] = goodsId
        params["num"] = "1"

        ApiManager2.post(this,params,Constant.ESHOP_COMMENTS,object : ApiManager2.OnResult<BaseBean<ArrayList<CommentBean>>>(){
            override fun onSuccess(data: BaseBean<ArrayList<CommentBean>>) {
                if (data.message != null && data.message?.isNotEmpty() == true){
                    ll_comment.visibility = View.VISIBLE
                    recycler_comment.layoutManager = LinearLayoutManager(this@GoodsDetailActivity)
                    recycler_comment.adapter = GoodsCommentListAdapter(data.message?: arrayListOf())
                }else{
                    ll_comment.visibility = View.GONE
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<ArrayList<CommentBean>>) {

            }

        })
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onShopEvent(shop: Shop) {
        if (shop.id != goodsDetail?.store?.id) {
            return
        }
        goodsDetail?.store?.attention = shop.attention
        if (shop.attention == "1") {
            btnFollow.setBackgroundResource(R.mipmap.ic_shop_cancel_follow)
        } else {
            btnFollow.setBackgroundResource(R.mipmap.ic_shop_follow)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGoodsEvent(goods: GoodsFavEvent) {
        if (goods.goods_id != goodsDetail?.goods?.id) {
            return
        }
        goodsDetail?.goods?.fav = goods.isfav
        if (goods.isfav == "1") {
            view_collect.setBackgroundResource(R.mipmap.ic_goods_collect_red)
        } else {
            view_collect.setBackgroundResource(R.mipmap.ic_goods_collect)
        }
    }


}