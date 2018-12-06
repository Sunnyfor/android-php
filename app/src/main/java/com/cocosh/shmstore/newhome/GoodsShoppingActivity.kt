package com.cocosh.shmstore.newhome

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.fragment.GoodsListFragment
import com.cocosh.shmstore.newhome.fragment.ShopGoodsListFragment
import com.cocosh.shmstore.newhome.model.Shop
import kotlinx.android.synthetic.main.activity_goods_shopping.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class GoodsShoppingActivity : BaseActivity() {

    private var shopId = ""

    private val shopGoodsListFragment: ShopGoodsListFragment by lazy {
        ShopGoodsListFragment()
    }

    override fun setLayout(): Int = R.layout.activity_goods_shopping

    override fun initView() {
        titleManager.defaultTitle(intent.getStringExtra("title"))
        shopId = intent.getStringExtra("shopId") ?: "0"
        supportFragmentManager.beginTransaction().add(R.id.content, shopGoodsListFragment).commit()
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        loadShop()
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }


    private fun loadShop() {
        val params = HashMap<String, String>()
        params["store_id"] = shopId
        ApiManager2.post(this, params, Constant.ESHOP_STORE, object : ApiManager2.OnResult<BaseBean<Shop>>() {
            override fun onSuccess(data: BaseBean<Shop>) {
                data.message?.let { it ->
                    Glide.with(this@GoodsShoppingActivity)
                            .load(it.logo)
                            .dontAnimate()
                            .placeholder(R.drawable.default_head)
                            .into(ivPhoto)

                    tvName.text = it.name
                    tvCount.text = (it.total + "件在售商品")

                    if (it.attention == "1") {
                        btnFollow.setBackgroundResource(R.mipmap.ic_shop_cancel_follow)
                    } else {
                        btnFollow.setBackgroundResource(R.mipmap.ic_shop_follow)
                    }
                    it.cates?.let { cates ->
                        cates.add(0, Shop.Gates("0", "全部"))
                        shopGoodsListFragment.loadData(shopId, "0")

                        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                            override fun onTabReselected(tab: TabLayout.Tab) {

                            }

                            override fun onTabUnselected(tab: TabLayout.Tab) {
                                tab.customView?.let {
                                    it as TextView
                                    it.setBackgroundResource(R.drawable.shape_btn_border_red2)
                                    it.setTextColor(ContextCompat.getColor(this@GoodsShoppingActivity, R.color.red))
                                }
                            }

                            override fun onTabSelected(tab: TabLayout.Tab) {
                                tab.customView?.let {
                                    it as TextView
                                    it.setBackgroundResource(R.drawable.shape_btn_red)
                                    it.setTextColor(ContextCompat.getColor(this@GoodsShoppingActivity, R.color.white))
                                }
                                shopGoodsListFragment.loadData(shopId, cates[tab.position].id)
                            }
                        })
                    }
                    it.cates?.forEach {
                        val customView = LayoutInflater.from(this@GoodsShoppingActivity).inflate(R.layout.layout_nav_text, null, false) as TextView
                        customView.text = it.name
                        customView.tag = it.id
                        val newTab = tabLayout.newTab()
                        newTab.customView = customView
                        tabLayout.addTab(newTab)
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<Shop>) {
            }

        })
    }


    companion object {
        fun start(context: Context, title: String, shopId: String) {
            context.startActivity(Intent(context, GoodsShoppingActivity::class.java)
                    .putExtra("title", title)
                    .putExtra("shopId", shopId))
        }
    }

}