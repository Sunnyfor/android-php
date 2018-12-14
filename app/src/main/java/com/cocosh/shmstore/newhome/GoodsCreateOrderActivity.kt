package com.cocosh.shmstore.newhome

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.Address
import com.cocosh.shmstore.mine.presenter.AddRessPresenter
import com.cocosh.shmstore.mine.ui.AddressMangerActivity
import com.cocosh.shmstore.newCertification.ui.PayActivity
import com.cocosh.shmstore.newhome.adapter.CreateOrderListAdapter
import com.cocosh.shmstore.newhome.model.AddCar
import com.cocosh.shmstore.newhome.model.CreateGoodsBean
import com.cocosh.shmstore.newhome.model.CreateOrder
import com.cocosh.shmstore.newhome.model.GoodsDetail
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_goods_create_order.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject

class GoodsCreateOrderActivity : BaseActivity(), MineContrat.IAddressView {
    private val shopList = ArrayList<CreateGoodsBean>()
    private var address: Address? = null
    private var deduction = true
    private var skuid = "0"
    private var skustr = ""
    private var price = "0.00"
    private var redMoney = "0.00"
    private var number = "0"


    val mPresenter: AddRessPresenter  by lazy {
        AddRessPresenter(this, this)
    }

    override fun setLayout(): Int = R.layout.activity_goods_create_order

    override fun initView() {
        titleManager.defaultTitle("填写订单")

        shopList.clear()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false

        ll_select.setOnClickListener {
            if (deduction) {
                deduction = false
                view_select.setBackgroundResource(R.mipmap.ic_vouchers_select_no)
            } else {
                deduction = true
                view_select.setBackgroundResource(R.mipmap.ic_vouchers_select_yes)
            }
            countPayMoney(deduction)
        }


        skuid = intent.getStringExtra("skuid") ?: ""
        skustr = intent.getStringExtra("skustr") ?: ""
        price = intent.getStringExtra("price") ?: ""
        number = intent.getStringExtra("number") ?: ""


        intent.getStringExtra("type")?.let { type ->
            if (type == "1") {
                SmApplication.getApp().getData<GoodsDetail>(DataCode.GOODS_DETAIL, true)?.let {
                    val goodsList = arrayListOf<CreateGoodsBean>()
                    goodsList.add(CreateGoodsBean(it.store.id, it.store.name, it.goods?.id?:"", it.goods?.name?:"", (it.goods?.image?: arrayListOf(""))[0], skuid, skustr, price, number, null))
                    shopList.add(CreateGoodsBean(it.store.id, it.store.name, "", "", "", "", "", price, "", goodsList))
                    recyclerView.adapter = CreateOrderListAdapter(shopList)
                }
            }

            if (type == "2") {
                SmApplication.getApp().getData<ArrayList<CreateGoodsBean>>(DataCode.GOODS_DETAIL, true)?.let {
                    shopList.addAll(it)
                    recyclerView.adapter = CreateOrderListAdapter(it)
                }
            }
            txt_allMoney.text = ("¥ $price")
        }

        ll_address_empty.setOnClickListener(this)
        ll_address.setOnClickListener(this)
        btnCommit.setOnClickListener(this)
    }

    companion object {
        //商品详情页面跳转
        fun start(context: Context, skuid: String, skustr: String, price: String, number: String) {
            val intent = Intent(context, GoodsCreateOrderActivity::class.java)
            intent.putExtra("type", "1")
            intent.putExtra("skuid", skuid)
            intent.putExtra("skustr", skustr)
            intent.putExtra("price", price)
            intent.putExtra("number", number)
            context.startActivity(intent)
        }

        //购物车跳转
        fun start(context: Context, price: String) {
            val intent = Intent(context, GoodsCreateOrderActivity::class.java)
            intent.putExtra("type", "2")
            intent.putExtra("price", price)
            context.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        address = SmApplication.getApp().getData<Address>(DataCode.ADDRESS, false)
        if (address == null) {
            mPresenter.requestGetAddress(0) //加载地址
        } else {
            ll_address_empty.visibility = View.GONE
            ll_address.visibility = View.VISIBLE
            txt_name.text = address?.receiver
            txt_phone.text = address?.phone
            txt_address.text = ("${address?.district} ${address?.addr}")
        }
        loadRedMoney() //加载红包余额
    }

    override fun onListener(view: View) {
        when (view.id) {
            ll_address_empty.id, ll_address.id -> {
                startActivity(Intent(this, AddressMangerActivity::class.java).putExtra("type","buy"))
            }
            R.id.btnCommit -> {
                createOrder()
            }
        }
    }

    override fun reTryGetData() {
    }

    override fun deleteAddress(result: String) {

    }

    override fun getAddress(result: BaseBean<ArrayList<Address>>) {

        result.message?.let { it ->
            if (it.isNotEmpty()) {
                ll_address_empty.visibility = View.GONE
                ll_address.visibility = View.VISIBLE
                address = it.last { it.default == "1" }.apply {
                    txt_name.text = receiver
                    txt_phone.text = phone
                    txt_address.text = ("$district $addr")
                }
            }
            return
        }
        ll_address_empty.visibility = View.VISIBLE
        ll_address.visibility = View.GONE
    }

    override fun defaultAddress(result: String) {

    }


    private fun loadRedMoney() {
        ApiManager2.get(this, null, Constant.RP_REMAINS, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                txt_red_money.text = ("¥ " + data.message)
                data.message?.let {
                    redMoney = it
                    countPayMoney(deduction)
                }

            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<String>) {
            }
        })
    }

    private fun countPayMoney(boolean: Boolean) {
        if (boolean) {
            if (price.toFloat() <= redMoney.toFloat()) {
                txt_pay_money.text = ("0.00")
            } else {
                val payMoney = price.toFloat() - redMoney.toFloat()
                txt_pay_money.text = (payMoney.toString())
            }

        } else {
            txt_pay_money.text = (price)
        }
    }


    private fun createOrder() {

        if (address == null) {
            ToastUtil.show("请选择收货地址！")
            return
        }

        var deductionMoney = 0f

        val params = hashMapOf<String, String>()
        val parentArray = JSONArray()

        shopList.forEach { shop ->
            val shopObj = JSONObject()
            val goodsArray = JSONArray()
            shopObj.put("store_id", shop.store_id)
            shop.goodsList?.forEach {
                val goodsObj = JSONObject()
                goodsObj.put("stock_id", it.goods_id)
                goodsObj.put("sku_id", it.sku_id)
                goodsObj.put("num", it.num)
                goodsArray.put(goodsObj)
            }
            shopObj.put("goods", goodsArray)

            if (deduction && redMoney.toFloat() > 0) {
                shopObj.put("discount_type", "1")
                deductionMoney += shop.price.toFloat()
                val price = if (redMoney.toFloat() >= deductionMoney) {
                    shop.price
//                } else (shop.price.toFloat() - (redMoney.toFloat() - (deductionMoney - shop.price.toFloat()))).toString()
                } else (redMoney.toFloat() - (deductionMoney - shop.price.toFloat())).toString()
                shopObj.put("discount", price)
            }
            parentArray.put(shopObj)
        }

        val addressObj = JSONObject()
        addressObj.put("name", address?.receiver)
        addressObj.put("phone", address?.phone)
        addressObj.put("address", address?.id)

        params["receive"] = addressObj.toString()
        params["data"] = parentArray.toString()
        ApiManager2.post(this, params, Constant.ESHOP_ORDER_CREATE, object : ApiManager2.OnResult<BaseBean<CreateOrder>>() {
            override fun onSuccess(data: BaseBean<CreateOrder>) {
                data.message?.let { createOrder ->
                    val numberSb = StringBuilder()
                    createOrder.order_sn.forEach {
                        numberSb.append(it).append(",")
                    }
                    numberSb.deleteCharAt(numberSb.lastIndex)
                    PayActivity.start(this@GoodsCreateOrderActivity, numberSb.toString(), data.message?.actual
                            ?: "0.00", "6")
                    SmApplication.getApp().removeData(DataCode.ADDRESS)
                    EventBus.getDefault().post(AddCar())
                    finish()
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<CreateOrder>) {

            }

        })
    }
}