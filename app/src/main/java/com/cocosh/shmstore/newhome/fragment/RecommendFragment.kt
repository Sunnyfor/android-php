package com.cocosh.shmstore.newhome.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.BonusListActivity
import com.cocosh.shmstore.home.model.Bonus2
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.adapter.RecommendAdapter
import com.cocosh.shmstore.newhome.model.Goods
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.android.synthetic.main.fragment_recommend.view.*

//首页推荐
class RecommendFragment: BaseFragment() {
    private val goodsList = arrayListOf<Goods>()

    override fun setLayout(): Int  = R.layout.fragment_recommend

    override fun initView() {

//        goodsList.add(Goods("深度清洁毛孔 清爽不紧绷洗面奶","1200"))
//        goodsList.add(Goods("深度清洁毛孔 清爽不紧绷洗面奶","1000"))
//        goodsList.add(Goods("深度清洁毛孔 清爽不紧绷洗面奶","1500"))
//        goodsList.add(Goods("深度清洁毛孔 清爽不紧绷洗面奶","2000"))
//        goodsList.add(Goods("深度清洁毛孔 清爽不紧绷洗面奶","900"))

        getLayoutView().recyclerView.layoutManager = LinearLayoutManager(context)
        getLayoutView().recyclerView.adapter = RecommendAdapter(goodsList)
        getLayoutView().recyclerView.setHasFixedSize(true)
        getLayoutView().recyclerView.isNestedScrollingEnabled = false

        bonusOne.setOnClickListener(this)
        bonusTwo.setOnClickListener(this)
        bonusThree.setOnClickListener(this)
        loadBanner()
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
//                startBonus("媒体扶贫")
            }

            R.id.bonusThree -> {
//                startBonus("媒体扶贫")
                startBonus("粉丝红包")
//                startBonus("消费扶贫")
            }
        }
    }

    override fun close() {

    }

    private fun loadBanner() {
        val params = hashMapOf<String, String>()
        ApiManager2.get(0, getBaseActivity(), params, Constant.RP_HOME, object : ApiManager2.OnResult<BaseBean<ArrayList<Bonus2>>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<ArrayList<Bonus2>>) {
                data.message?.let {
                    getLayoutView().homeAdView.loadData(it)
                }
            }

            override fun onCatch(data: BaseBean<ArrayList<Bonus2>>) {
            }

        })
    }


    //跳转红包页面
    private fun startBonus(title: String) {
        val it = Intent(context, BonusListActivity::class.java)
        it.putExtra("title", title)
        startActivity(it)
    }
}