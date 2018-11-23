package com.cocosh.shmstore.newhome.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsListActivity
import com.cocosh.shmstore.newhome.adapter.OneGoodsClazzAdapter
import com.cocosh.shmstore.newhome.adapter.TwoGoodsClazzAdapter
import com.cocosh.shmstore.newhome.model.GoodsClazz
import com.cocosh.shmstore.title.DefaultTitleFragment
import kotlinx.android.synthetic.main.activity_goods_clazz.view.*

class GoodsClazzFragment : BaseFragment() {
    private val oneList = arrayListOf<GoodsClazz.Bean>()


    override fun setLayout(): Int = R.layout.activity_goods_clazz

    override fun initView() {
        val defaultTitleFragment = DefaultTitleFragment()
        defaultTitleFragment.singleText()
        defaultTitleFragment.title("分类")
        showTitle(defaultTitleFragment)

        getLayoutView().recyclerView.layoutManager = LinearLayoutManager(context)
        val oneGoodsClazzAdapter = OneGoodsClazzAdapter(oneList)

        oneGoodsClazzAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                oneGoodsClazzAdapter.index = index
                oneGoodsClazzAdapter.notifyDataSetChanged()
                getLayoutView().recyclerView2.adapter = TwoGoodsClazzAdapter(oneList[index].data?:ArrayList())
            }
        })
        getLayoutView().recyclerView.adapter = oneGoodsClazzAdapter

        getLayoutView().recyclerView2.layoutManager = LinearLayoutManager(context)
        loadData()
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {

    }


    fun loadData() {
        val params = HashMap<String, String>()

        ApiManager2.get(getBaseActivity(), params, Constant.ESHOP_CLASSES, object : ApiManager2.OnResult<BaseBean<GoodsClazz>>() {
            override fun onSuccess(data: BaseBean<GoodsClazz>) {
                data.message?.data?.let { arrayList ->
                    oneList.clear()
                    oneList.addAll(arrayList.filter { it.deep == "1" })
                    oneList.forEach { parent ->
                        arrayList.filter { it.deep == "2" && it.parent == parent.id }.forEach { item ->
                            if (parent.data == null) {
                                parent.data = arrayListOf()
                            }
                            parent.data?.add(item)
                            parent.data?.addAll(arrayList.filter { it.deep == "3" && it.parent == item.id } as ArrayList<GoodsClazz.Bean>)
                        }
                    }
                    getLayoutView().recyclerView.adapter.notifyDataSetChanged()
                    if (oneList.isNotEmpty()) {
                        oneList[0].data?.let {
                            getLayoutView().recyclerView2.adapter = TwoGoodsClazzAdapter(it)
                        }
                    }

                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<GoodsClazz>) {

            }
        })
    }

}