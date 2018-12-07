package com.cocosh.shmstore.newhome.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.adapter.ShoppingListAdapter
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import com.cocosh.shmstore.title.LeftRightTitleFragment
import com.cocosh.shmstore.utils.StringUtils
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_shopping_car.*
import kotlinx.android.synthetic.main.layout_left_right_title.*
import org.json.JSONArray
import org.json.JSONObject

class ShoppingFragment : BaseFragment() {
    private var isAll = false
    private var money = 0f
    private var mData = ArrayList<ShoppingCarts>()
    private var isEdit = false

    private val shoppingListAdapter: ShoppingListAdapter by lazy {
        ShoppingListAdapter(mData) {
            money = 0f
            isAll = true
            mData.forEach { list ->
                list.goodsList.forEach {
                    if (!it.isChecked) {
                        isAll = false
                    } else {
                        money += (it.sku_price.toFloat() * it.num.toInt())
                    }
                }
            }

            if (isAll) {
                view_select.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
            } else {
                view_select.setBackgroundResource(R.drawable.bg_select_round_gray_no)
            }

            tvCardMoney.text = ("¥"+StringUtils.insertComma(money.toString(),2))
        }
    }

    override fun setLayout(): Int = R.layout.fragment_shopping_car

    override fun initView() {
        val titleFragment = LeftRightTitleFragment()
        titleFragment.goneLeft()
        titleFragment.title("购物车")
        titleFragment.rightIcon("编辑")
        titleFragment.setRightOnClickListener(
                View.OnClickListener {
                    if (isEdit){
                        isEdit = false
                        titleFragment.tvRight.text =("编辑")
                        rlDelete.visibility = View.GONE
                        llResult.visibility = View.VISIBLE
                        refreshLayout.isEnabled = true
                        modifyCount()
                    }else{
                        isEdit = true
                        titleFragment.tvRight.text = ("保存")
                        rlDelete.visibility = View.VISIBLE
                        llResult.visibility = View.GONE
                        refreshLayout.isEnabled = false
                    }
                    shoppingListAdapter.isEdit = isEdit
                    shoppingListAdapter.notifyDataSetChanged()
                })
        showTitle(titleFragment)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = shoppingListAdapter

        refreshLayout.setColorSchemeResources(R.color.red)
        refreshLayout.setOnRefreshListener {
            loadData()
        }

        llAll.setOnClickListener {
            isAll = !isAll
            shoppingListAdapter.selectAll(isAll)
        }

        loadData()
    }

    override fun reTryGetData() {
    }

    override fun onListener(view: View) {
    }

    override fun close() {
    }

    //加载购物车数据
    private fun loadData() {
        isAll = false
        money = 0f
        tvCardMoney.text = ("¥"+StringUtils.insertComma(money.toString(),2))

        view_select.setBackgroundResource(R.drawable.bg_select_round_gray_no)
        mData.clear()
        ApiManager2.post(getBaseActivity(), hashMapOf(), Constant.ESHOP_CARTS, object : ApiManager2.OnResult<BaseBean<ArrayList<ShoppingCarts>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<ShoppingCarts>>) {
                refreshLayout.isRefreshing = false
                data.message?.let { it ->
                    it.forEach { carts ->
                        if (!mData.contains(carts)) {
                            val goodsList = it.filter { it.store_name == carts.store_name } as ArrayList
                            mData.add(ShoppingCarts(carts.store_id, carts.store_name, "", "", "", "", "", linkedMapOf(), "",false, goodsList))
                            shoppingListAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
                refreshLayout.isRefreshing = false
            }

            override fun onCatch(data: BaseBean<ArrayList<ShoppingCarts>>) {
            }

        })

    }



    private fun modifyCount() {
        val jsonArray = JSONArray()
        mData.forEach { shopList ->
            shopList.goodsList.forEach {
                jsonArray.put(JSONObject().put(it.sku_id,it.num))
            }
        }
        val params = hashMapOf<String, String>()
        params["data"] = jsonArray.toString()
        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_CART_SAVE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                    ToastUtil.show("保存成功！")
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}