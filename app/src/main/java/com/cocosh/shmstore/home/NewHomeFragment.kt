package com.cocosh.shmstore.home

import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.fragment.GoodsListFragment
import com.cocosh.shmstore.newhome.fragment.RecommendFragment
import com.cocosh.shmstore.newhome.model.GoodsNav
import com.cocosh.shmstore.title.NewHomeTitleFragment
import com.cocosh.shmstore.utils.ViewUtil
import com.cocosh.shmstore.widget.popupwindow.GoodsNavPopup
import kotlinx.android.synthetic.main.fragment_new_home.*

class NewHomeFragment : BaseFragment() {
    var index = 0
    private var menuList = arrayListOf<GoodsNav.Data>()


    private val recommendFragment: RecommendFragment by lazy {
        RecommendFragment()
    }

    private val goodsListFragment: GoodsListFragment by lazy {
        GoodsListFragment()
    }

    override fun setLayout(): Int = R.layout.fragment_new_home

    override fun initView() {

        showTitle(NewHomeTitleFragment())


        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout2.tabMode = TabLayout.MODE_SCROLLABLE

        childFragmentManager.beginTransaction().add(R.id.flContent, goodsListFragment).hide(goodsListFragment).commit()
        childFragmentManager.beginTransaction().add(R.id.flContent, recommendFragment).commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                index = tab.position
                tabLayout2.removeAllTabs()
                if (tab.position != 0) {
                    tabLayout2.visibility = View.VISIBLE
                    menuList[tab.position].data.forEach {

                        val customView = LayoutInflater.from(context).inflate(R.layout.layout_nav_text, null, false) as TextView
                        customView.text = it.name
                        val newTab = tabLayout2.newTab()
                        newTab.customView = customView
                        tabLayout2.addTab(newTab)
                    }
                    if (menuList[index].data.isEmpty()){
                        tabLayout2.visibility = View.GONE
                        goodsListFragment.loadData(menuList[index].id)
                    }
                    childFragmentManager.beginTransaction().show(goodsListFragment).commit()
                    childFragmentManager.beginTransaction().hide(recommendFragment).commit()

                } else {
                    tabLayout2.visibility = View.GONE
                    childFragmentManager.beginTransaction().hide(goodsListFragment).commit()
                    childFragmentManager.beginTransaction().show(recommendFragment).commit()
                }
            }
        })

        tabLayout2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.customView?.let {
                    it as TextView
                    it.setBackgroundResource(R.drawable.shape_btn_border_red2)
                    it.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.let {
                    it as TextView
                    it.setBackgroundResource(R.drawable.shape_btn_red)
                    it.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                goodsListFragment.loadData(menuList[index].data[tab.position].id)
            }
        })

        tvMore.setOnClickListener(this)

        loadNav()
    }

    override fun onStart() {
        super.onStart()
        tabLayout.post { ViewUtil.reflex(tabLayout) }
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
        when (view.id) {
            tvMore.id -> {
                tvCover.visibility = View.VISIBLE
                tvMore.text = resources.getString(R.string.iconMoreTop)
                val goodsNavPopup = GoodsNavPopup(context, menuList, index) {
                    tabLayout.getTabAt(it)?.select()
                }
                goodsNavPopup.setOnDismissListener {
                    tvCover.visibility = View.GONE
                    tvMore.text = resources.getString(R.string.iconMoreDown)
                }
                goodsNavPopup.showAsDropDown(tabLayout)
            }
        }
    }

    override fun close() {
    }


    private fun loadNav() {
        ApiManager2.get(getBaseActivity(), null, Constant.ESHOP_CLASS_RECOMMEND, object : ApiManager2.OnResult<BaseBean<GoodsNav>>() {
            override fun onSuccess(data: BaseBean<GoodsNav>) {
                data.message?.data?.let { arrayList ->
                    menuList.clear()
                    menuList.add(GoodsNav.Data("0", "0", "推荐", "1", arrayListOf()))
                    menuList.addAll(arrayList.filter { it.deep == "1" })
                    menuList.forEach { goodsNav ->
                        tabLayout.addTab(tabLayout.newTab().setText(goodsNav.name))
                        goodsNav.data = arrayList.filter { it.parent == goodsNav.id } as ArrayList<GoodsNav.Data>
                    }
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<GoodsNav>) {

            }

        })
    }
}