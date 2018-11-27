package com.cocosh.shmstore.newhome.fragment

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.home.BonusListActivity
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsDetailActivity
import com.cocosh.shmstore.newhome.adapter.RecommendAdapter
import com.cocosh.shmstore.newhome.model.Goods
import com.cocosh.shmstore.newhome.model.NewHomeBanner
import com.cocosh.shmstore.newhome.model.Recommend
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.android.synthetic.main.fragment_recommend.view.*

//首页推荐
class RecommendFragment : BaseFragment() {
    private val goodsList = arrayListOf<Goods>()

    override fun setLayout(): Int = R.layout.fragment_recommend

    override fun initView() {

//        goodsList.add(Goods("", "深度清洁毛孔 清爽不紧绷洗面奶", "1200", "", ""))
//        goodsList.add(Goods("", "深度清洁毛孔 清爽不紧绷洗面奶", "1000", "", ""))
//        goodsList.add(Goods("", "深度清洁毛孔 清爽不紧绷洗面奶", "1500", "", ""))
//        goodsList.add(Goods("", "深度清洁毛孔 清爽不紧绷洗面奶", "2000", "", ""))
//        goodsList.add(Goods("", "深度清洁毛孔 清爽不紧绷洗面奶", "900", "", ""))

        val recommendAdapter = RecommendAdapter(goodsList)
        getLayoutView().recyclerView.layoutManager = LinearLayoutManager(context)
        getLayoutView().recyclerView.adapter = recommendAdapter
        getLayoutView().recyclerView.setHasFixedSize(true)
        getLayoutView().recyclerView.isNestedScrollingEnabled = false

        recommendAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                startActivity(Intent(context, GoodsDetailActivity::class.java)
                        .putExtra("id", goodsList[index].id)
                        .putExtra("title", goodsList[index].name))
            }
        })


        bonusOne.setOnClickListener(this)
        bonusTwo.setOnClickListener(this)
        bonusThree.setOnClickListener(this)
        loadBanner()
        loadData()

        val ranks = arrayListOf(
                "http://img.sccnn.com/bimg/339/12122.jpg",
                "http://img.sccnn.com/bimg/339/11732.jpg",
                "http://img.sccnn.com/bimg/339/16606.jpg",
                "http://img.sccnn.com/bimg/339/11254.jpg",
                "http://img.sccnn.com/bimg/339/16088.jpg",
                "http://pic35.photophoto.cn/20150630/0018031349781196_b.jpg"
        )
        initRank(ranks)
    }

    override fun reTryGetData() {

    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.bonusOne -> {
                startBonus("大众红包")
            }

            R.id.bonusTwo -> {
                startBonus("精准红包")
            }

            R.id.bonusThree -> {
                startBonus("粉丝红包")
            }
        }
    }

    override fun close() {

    }

    private fun loadBanner() {
        val params = hashMapOf<String, String>()
        ApiManager2.get(0, getBaseActivity(), params, Constant.RP_HOME, object : ApiManager2.OnResult<BaseBean<NewHomeBanner>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<NewHomeBanner>) {
                data.message?.let {
                    getLayoutView().tvMoney.text = it.total
                    getLayoutView().homeAdView.loadData(it.data)
                }
            }

            override fun onCatch(data: BaseBean<NewHomeBanner>) {
            }

        })
    }

    fun loadData() {
        val params = hashMapOf<String, String>()
        ApiManager2.get(0, getBaseActivity(), params, Constant.ESHOP_RECOMMEND, object : ApiManager2.OnResult<BaseBean<Recommend>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<Recommend>) {
                data.message?.let { it ->
                    initRank(it.rank ?: arrayListOf())
                }
            }

            override fun onCatch(data: BaseBean<Recommend>) {
            }

        })
    }

    //跳转红包页面
    private fun startBonus(title: String) {
        val it = Intent(context, BonusListActivity::class.java)
        it.putExtra("title", title)
        startActivity(it)
    }


    //初始化排行榜数据
    fun initRank(ranks: ArrayList<String>) {
        ranks.forEachIndexed { index, s ->
            when (index) {
                0 -> ivRank1
                1 -> ivRank2
                2 -> ivRank3
                3 -> ivRank4
                4 -> ivRank5
                5 -> ivRank6
                else -> null
            }?.let {
                Glide.with(context)
                        .load(s)
                        .dontAnimate()
                        .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                        .into(it)
            }
        }
    }
}