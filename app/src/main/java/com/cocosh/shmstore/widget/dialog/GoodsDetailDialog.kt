package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.GoodsCreateOrderActivity
import com.cocosh.shmstore.newhome.model.AddCar
import com.cocosh.shmstore.newhome.model.GoodsDetail
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.utils.UserManager2.isLogin
import kotlinx.android.synthetic.main.dialog_goods_detail_format.*
import kotlinx.android.synthetic.main.item_goods_detail_label.view.*
import org.greenrobot.eventbus.EventBus

class GoodsDetailDialog(private var skuid: String, var count: String, var context: BaseActivity, private var goodsDetail: GoodsDetail, var result: (resultStr: String, skuId: String, count: String) -> Unit) : Dialog(context), View.OnClickListener {

    private val selectFlag = hashMapOf<String, String>()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_goods_detail_format)
        window.setBackgroundDrawableResource(R.color.transparent)
        window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.SercurityAnimation)

        tvCount.text = count

        itv_close.setOnClickListener(this)
        rl_add.setOnClickListener(this)
        rl_jian.setOnClickListener(this)
        btnAddCar.setOnClickListener(this)
        btnBuy.setOnClickListener(this)

        initData()

        setOnDismissListener {
            updateButton()
        }
    }

    fun initData() {
        goodsDetail.goods?.image?.let {
            if (it.isNotEmpty()){
                Glide.with(context)
                        .load(it[0])
                        .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.activity_bg)))
                        .into(ivPhoto)
            }
        }

        tvMoney.text = goodsDetail.goods?.price

        goodsDetail.goods?.sku?.attrs?.forEach {
            val view = LayoutInflater.from(context).inflate(R.layout.item_goods_detail_label, llLabel, false)
            view.tvName.text = it.name
            val values = it.vals.split(",")
            view.labView.setLabels(values)
            view.labView.setOnLabelSelectChangeListener { _: TextView, data: Any, isSelect: Boolean, _: Int ->
                if (isSelect) {
                    selectFlag[it.name] = data as String
                    updateButton()
                }
            }
            llLabel.addView(view)
        }

        goodsDetail.goods?.sku?.let { sku ->
            sku.desc?.last { it.id == skuid }?.ele?.forEach {
                selectFlag[it.key] = it.value
            }

            for (i in 0 until selectFlag.size) {
                sku.attrs?.let { list ->
                    llLabel.getChildAt(i)?.let { it ->
                        val values = list[i].vals.split(",")
                        if (values.isNotEmpty()){
                            val select =  values.indexOf(selectFlag[it.tvName.text.toString()])
                            if (select != -1){
                                (llLabel.getChildAt(i)).labView?.setSelects(select)
                            }
                        }
                    }
                }
            }

            val ele = StringBuilder()
            sku.desc?.let { it ->
                it[0].ele.forEach {
                    ele.append(it.value).append("，")
                }
            }
            updateButton()
        }


//        tvDesc.text = (ele.toString() + count + "件")

    }

    override fun onClick(v: View) {
        when (v.id) {
            itv_close.id -> dismiss()
            rl_add.id -> add()
            rl_jian.id -> sub()
            btnAddCar.id -> {
                addCar()
                dismiss()
            }
            btnBuy.id -> {

                if (!UserManager2.isLogin()) {
                    SmediaDialog(context).showLogin()
                    return
                }

                SmApplication.getApp().setData(DataCode.GOODS_DETAIL, goodsDetail)
                GoodsCreateOrderActivity.start(context,skuid,tvDesc.text.toString(),tvMoney.text.toString(),tvCount.text.toString()) //单个商品创建订单
                dismiss()
            }

        }
    }

    private fun add() {
        val count = tvCount.text.toString().toInt()
        if (count < (resultList?.last()?.num?:"0").toInt()) {
            tvCount.text = (count + 1).toString()
            countDesc()
        }else{
            ToastUtil.show("库存不足！")
        }
    }

    private fun sub() {
        val count = tvCount.text.toString().toInt()
        if (count > 1) {
            tvCount.text = (count - 1).toString()
            countDesc()
        }
    }

    private var resultList: ArrayList<GoodsDetail.Goods.Sku.Desc>? = null


    private fun updateButton() {
        var lastEntry: Map.Entry<String, String>? = null
        selectFlag.forEach { entry ->
            resultList = goodsDetail.goods?.sku?.desc?.filter { it.ele[entry.key] == entry.value } as ArrayList<GoodsDetail.Goods.Sku.Desc>
            lastEntry?.let { lastEntry ->
                resultList = resultList?.filter { it.ele[entry.key] == entry.value && it.ele[lastEntry.key] == lastEntry.value } as ArrayList<GoodsDetail.Goods.Sku.Desc>
            }
            lastEntry = entry
        }
        countDesc()
    }

    private fun countDesc() {
        val descSB = StringBuilder()

        resultList?.let { it ->
            it.last().ele.forEach {
                descSB.append(it.value).append("，")
            }
            descSB.append(tvCount.text.toString()).append("件")

            tvMoney.text = (it.last().price.toFloat() * tvCount.text.toString().toInt()).toString()
            tvDesc.text = descSB.toString()

            result(tvDesc.text.toString(), it.last().id, tvCount.text.toString())
        }
    }

    private fun addCar() {

        if (!UserManager2.isLogin()) {
            SmediaDialog(context).showLogin()
            return
        }

        val params = HashMap<String, String>()
        params["sku_id"] = resultList?.last()?.id ?: skuid
        params["shop_num"] = tvCount.text.toString()
        ApiManager2.post(context, params, Constant.ESHOP_CART_ADD, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                if (data.message?.isEmpty() == true){
                    ToastUtil.show("成功添加到购物车！")
                    EventBus.getDefault().post(AddCar())
                }else{
                    ToastUtil.show("成功添加到购物车！")
                }

            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<String>) {
            }

        })
    }

}