package com.cocosh.shmstore.newhome.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.newhome.adapter.ShoppingListAdapter
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import com.cocosh.shmstore.title.DefaultTitleFragment
import kotlinx.android.synthetic.main.fragment_shopping_car.*

class ShoppingFragment : BaseFragment() {
    private var isAll = false

    private var mData = ArrayList<ShoppingCarts>()

    private val shoppingListAdapter: ShoppingListAdapter by lazy {
        ShoppingListAdapter(mData){
            isAll = true

            mData.forEach { list ->
                if (!list.isChecked){
                    isAll = false
                }
                list.goodsList.forEach {
                    if (!it.isChecked){
                        isAll = false
                    }
                }
            }
            if (isAll){
                rlSelect.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
            }else{
                rlSelect.setBackgroundResource(R.drawable.bg_select_round_gray_no)
            }
        }
    }

    override fun setLayout(): Int = R.layout.fragment_shopping_car

    override fun initView() {
        val defaultTitleFragment = DefaultTitleFragment()
        defaultTitleFragment.singleText()
        defaultTitleFragment.title("购物车")
        showTitle(defaultTitleFragment)

        val testArray = arrayListOf<ShoppingCarts>()

        testArray.add(ShoppingCarts("", "倾尽天下", "", "袜子", "", "100", "", "1"))
        testArray.add(ShoppingCarts("", "倾尽天下", "", "裤子", "", "100", "", "1"))
        testArray.add(ShoppingCarts("", "倾尽天下", "", "鞋子", "", "100", "", "1"))
        testArray.add(ShoppingCarts("", "倾尽天下", "", "帽子", "", "100", "", "1"))
        testArray.add(ShoppingCarts("", "成人用品", "", "咖啡", "", "100", "", "1"))
        testArray.add(ShoppingCarts("", "成人用品", "", "奶茶", "", "100", "", "1"))
        testArray.add(ShoppingCarts("", "成人用品", "", "充电宝", "", "100", "", "1"))


        testArray.forEach { carts ->
            if (!mData.contains(carts)) {
                val goodsList = testArray.filter { it.store_name == carts.store_name } as ArrayList
                mData.add(ShoppingCarts("", carts.store_name, "", "", "", "", "", "",false,goodsList))
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = shoppingListAdapter

        rlSelect.setOnClickListener {
            shoppingListAdapter.selectAll(!isAll)
        }
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }
}