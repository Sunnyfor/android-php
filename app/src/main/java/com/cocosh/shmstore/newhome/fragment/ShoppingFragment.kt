package com.cocosh.shmstore.newhome.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsCreateOrderActivity
import com.cocosh.shmstore.newhome.adapter.InvalidGoodsAdapter
import com.cocosh.shmstore.newhome.adapter.ShoppingListAdapter
import com.cocosh.shmstore.newhome.model.AddCar
import com.cocosh.shmstore.newhome.model.CreateGoodsBean
import com.cocosh.shmstore.newhome.model.ShoppingCarts
import com.cocosh.shmstore.title.LeftRightTitleFragment
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.StringUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.fragment_shopping_car.*
import kotlinx.android.synthetic.main.layout_left_right_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject

class ShoppingFragment : BaseFragment() {
    private var isAll = false
    private var money = 0f
    private var mData = ArrayList<ShoppingCarts.Shopping>()
    private var invalidList = arrayListOf<ShoppingCarts.Shopping>()
    private var isEdit = false
    private val titleFragment = LeftRightTitleFragment()
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

            tvCardMoney.text = ("¥" + StringUtils.insertComma(money.toString(), 2))
        }
    }

    private val invalidGoodsAdapter:InvalidGoodsAdapter by lazy {
        InvalidGoodsAdapter(invalidList)
    }

    override fun setLayout(): Int = R.layout.fragment_shopping_car

    override fun initView() {
        EventBus.getDefault().register(this)

        titleFragment.goneLeft()
        titleFragment.title("购物车")
        titleFragment.rightIcon("编辑")
        titleFragment.setRightOnClickListener(
                View.OnClickListener {
                    if (isEdit) {
                        isEdit = false
                        titleFragment.tvRight.text = ("编辑")
                        rlDelete.visibility = View.GONE
                        llResult.visibility = View.VISIBLE
                        refreshLayout.isEnabled = true
                        modifyCount()
                        rl_invalid.visibility = View.VISIBLE
                        recyclerView2.visibility = View.VISIBLE
                    } else {
                        isEdit = true
                        titleFragment.tvRight.text = ("保存")
                        rlDelete.visibility = View.VISIBLE
                        llResult.visibility = View.GONE
                        refreshLayout.isEnabled = false
                        rl_invalid.visibility = View.GONE
                        recyclerView2.visibility = View.GONE
                    }
                    shoppingListAdapter.isEdit = isEdit
                    shoppingListAdapter.notifyDataSetChanged()
                })
        showTitle(titleFragment)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = shoppingListAdapter

        recyclerView2.layoutManager = LinearLayoutManager(context)
        recyclerView2.setHasFixedSize(true)
        recyclerView2.isNestedScrollingEnabled = false
        recyclerView2.adapter = invalidGoodsAdapter


        refreshLayout.setColorSchemeResources(R.color.red)
        refreshLayout.setOnRefreshListener {
            loadData()
        }

        llAll.setOnClickListener {
            isAll = !isAll
            shoppingListAdapter.selectAll(isAll)
        }

        loadData()

        btn_delete.setOnClickListener {
            delete()
        }

        txt_invalid_clear.setOnClickListener(this)

        btn_buy.setOnClickListener(this)
    }

    override fun reTryGetData() {
        loadData()
    }

    override fun onListener(view: View) {
        when(view.id){
            R.id.btn_buy -> {
                createOrder()
            }

            R.id.txt_invalid_clear -> {
                val dialog = SmediaDialog(context)
                dialog.setTitle("是否清空失效商品")
                dialog.OnClickListener = View.OnClickListener {
                    clearInvalid()
                }
                dialog.show()
            }

        }
    }

    override fun close() {
        EventBus.getDefault().unregister(this)
    }

    //加载购物车数据
    private fun loadData() {
        isAll = false
        money = 0f

        tvCardMoney.text = ("¥" + StringUtils.insertComma(money.toString(), 2))

        view_select.setBackgroundResource(R.drawable.bg_select_round_gray_no)
        mData.clear()
        ApiManager2.post(getBaseActivity(), hashMapOf(), Constant.ESHOP_CARTS, object : ApiManager2.OnResult<BaseBean<ShoppingCarts>>() {
            override fun onSuccess(data: BaseBean<ShoppingCarts>) {
                isEdit = false
                shoppingListAdapter.isEdit = isEdit
                titleFragment.tvRight?.text = ("编辑")
                rlDelete.visibility = View.GONE
                llResult.visibility = View.VISIBLE
                refreshLayout.isEnabled = true
                refreshLayout.isRefreshing = false
                rl_invalid.visibility = View.VISIBLE
                recyclerView2.visibility = View.VISIBLE

                if (data.message == null || data.message?.list?.size == 0) {
                    if (data.message?.valid == null || data.message?.valid?.size == 0){
                        rlSelect.visibility = View.GONE
                        titleFragment.getRightText().visibility = View.GONE
                        showReTryLayout("购物车还是空的，赶紧行动吧！",true)
                    }

                } else {
                    titleFragment.getRightText().visibility = View.VISIBLE
                    rlSelect.visibility = View.VISIBLE
                    hideReTryLayout()
                }

                data.message?.list.let { it ->
                    it?.forEach { carts ->
                        if (!mData.contains(carts)) {
                            val goodsList = it.filter { it.store_name == carts.store_name } as ArrayList
                            mData.add(ShoppingCarts.Shopping(carts.store_id, carts.store_name, "", "", "", "", "", linkedMapOf(), "", false,"", goodsList))
                        }
                    }
                }

                shoppingListAdapter.notifyDataSetChanged()

                //失效商品

                invalidList.clear()

                if (data.message?.valid == null || (data.message?.valid?: arrayListOf()).isEmpty()){
                    rl_invalid.visibility = View.GONE
                }else{
                    rl_invalid.visibility = View.VISIBLE
                    txt_invalid_count.text = ("失效商品${data.message?.valid?.size}件")
                    invalidList.addAll(data.message?.valid?: arrayListOf())
                    invalidGoodsAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailed(code: String, message: String) {
                isEdit = false
                shoppingListAdapter.isEdit = isEdit
                titleFragment.tvRight?.text = ("编辑")
                rlDelete.visibility = View.GONE
                llResult.visibility = View.VISIBLE
                refreshLayout.isEnabled = true
                refreshLayout.isRefreshing = false
                shoppingListAdapter.notifyDataSetChanged()

                invalidList.clear()
                invalidGoodsAdapter.notifyDataSetChanged()

                if (code == "200") {
                    rlSelect.visibility = View.GONE
                    titleFragment.getRightText().visibility = View.GONE
                    showReTryLayout("购物车还是空的，赶紧行动吧！",true)
                } else {
                    titleFragment.getRightText().visibility = View.VISIBLE
                    rlSelect.visibility = View.VISIBLE
                    hideReTryLayout()
                }
            }

            override fun onCatch(data: BaseBean<ShoppingCarts>) {
            }

        })

    }

    //修改购物车数量
    private fun modifyCount() {
        val jsonArray = JSONArray()
        mData.forEach { shopList ->
            shopList.goodsList.forEach {
                jsonArray.put(JSONObject().put(it.sku_id, it.num))
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


    //从删除购物车
    private fun delete() {
        val jsonArray = JSONArray()
        mData.forEach { shoppingCarts ->
            shoppingCarts.goodsList.filter { it.isChecked }.forEach {
                jsonArray.put(JSONObject().put(it.sku_id, "0"))
            }
        }

        val params = hashMapOf<String, String>()
        params["data"] = jsonArray.toString()
        ApiManager2.post(getBaseActivity(), params, Constant.ESHOP_CART_SAVE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                ToastUtil.show("删除成功！")
                loadData()
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAddGoodsEvent(addCar: AddCar) {
        loadData()
    }


    private fun createOrder() {

        val shopList = arrayListOf<CreateGoodsBean>()

        mData.forEach { carts ->
            var shopPrice = 0f
            val goodsList = arrayListOf<CreateGoodsBean>()
            val createGoodsBean = CreateGoodsBean(carts.store_id, carts.store_name, "", "", "", "", "", "", "", goodsList)
            val childList = carts.goodsList.filter { it.isChecked }
            childList.forEach { it ->
                shopPrice+= (it.sku_price.toFloat() * it.num.toFloat())
                val skuSb = StringBuilder()
                it.sku_attrs?.forEach {
                    skuSb.append(it.value).append(",")
                }
                skuSb.deleteCharAt(skuSb.lastIndex)
                goodsList.add(CreateGoodsBean(carts.store_id, carts.store_name, it.goods_id, it.goods_name, it.sku_image, it.sku_id, skuSb.toString(), it.sku_price, it.num, null))
            }
            if (childList.isNotEmpty()){
                createGoodsBean.price = shopPrice.toString()
                shopList.add(createGoodsBean)
            }
        }

        if (shopList.isEmpty()){
            ToastUtil.show("请选择商品！")
            return
        }

        SmApplication.getApp().setData(DataCode.GOODS_DETAIL,shopList)
        GoodsCreateOrderActivity.start(context,money.toString())
    }


    //清理失效商品
    fun clearInvalid(){
        ApiManager2.post(getBaseActivity(), hashMapOf(),Constant.ESHOP_CART_CLEAN,object :ApiManager2.OnResult<BaseBean<String>>(){
            override fun onSuccess(data: BaseBean<String>) {
               if (data.status == "200"){
                   loadData()
                   ToastUtil.show("清理成功！")
               }else{
                   ToastUtil.show(data.message)
               }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && mData.isEmpty()) {
            loadData()
        }
    }
}