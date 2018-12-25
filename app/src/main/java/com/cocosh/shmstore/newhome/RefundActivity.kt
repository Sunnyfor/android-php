package com.cocosh.shmstore.newhome

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.Order
import com.cocosh.shmstore.newhome.model.Reason
import com.cocosh.shmstore.newhome.model.RefundMoney
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.StringUtils
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.OnDialogResult
import com.cocosh.shmstore.widget.dialog.SelectDialog
import kotlinx.android.synthetic.main.activity_refund.*
import kotlinx.android.synthetic.main.layout_item_show.view.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray

class RefundActivity : BaseActivity() {

    var type = 1
    var sku_id = ""
    var order_sn = ""
    var reason = ""

    var goods: Order.Goods? = null

    val reasonList = ArrayList<Reason>()

    override fun setLayout(): Int = R.layout.activity_refund

    override fun initView() {

        type = intent.getIntExtra("type", 1)

        order_sn = intent.getStringExtra("order_sn") ?: ""

        val title = if (type == 1) "退款" else "退货"

        titleManager.defaultTitle(title)

        SmApplication.getApp().getData<Order.Goods>(DataCode.ORDER_GOODS_ITEM, true)?.apply {
            goods = this
            this@RefundActivity.sku_id = sku_id
            Glide.with(this@RefundActivity)
                    .load(image)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(ColorDrawable(ContextCompat.getColor(this@RefundActivity, R.color.activity_bg)))
                    .into(ivPhoto)
            tvGoodsName.text = goods_name

            val descSB = StringBuilder()

            attr.forEach {
                descSB.append(it.value).append("，")
            }
            descSB.deleteCharAt(descSB.lastIndex)

            tvDesc.text = descSB

            tvMoney.text = price

            tvShowCount.text = ("x$salenum")
        }


        txt_reason.setOnClickListener {
            val selectDialog = SelectDialog(this)
            selectDialog.onDialogResult = object : OnDialogResult {
                override fun onResult(result: Any) {
                    val reasonData = result as Reason
                    txt_reason.setValue(reasonData.reason)
                    reason = reasonData.reason_id
                    selectDialog.dismiss()
                }
            }
            selectDialog.setReason(reasonList)
            selectDialog.show()
        }


        view_up.showLoading = {
            showLoading()
        }
        view_up.hideLoading = {
            hideLoading()
        }

        if (type == 1) {
            queryMoney()
        } else {

        }

        loadReason()  //加载理由

        btn_commit.setOnClickListener(this)
    }

    override fun onListener(view: View) {
        when(view.id){
            R.id.btn_commit -> {
                request()
            }
        }
    }

    override fun reTryGetData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        view_up.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun start(context: Context, type: Int, goods: Order.Goods, order_sn: String) {
            SmApplication.getApp().setData(DataCode.ORDER_GOODS_ITEM, goods)
            context.startActivity(Intent(context, RefundActivity::class.java)
                    .putExtra("type", type)
                    .putExtra("order_sn", order_sn))
        }
    }

    private fun queryMoney() {
        val params = HashMap<String, String>()
        params["order_sn"] = order_sn
        params["sku_id"] = sku_id
        ApiManager2.post(this, params, Constant.ESHOP_RETURN_DETAIL, object : ApiManager2.OnResult<BaseBean<RefundMoney>>() {
            override fun onSuccess(data: BaseBean<RefundMoney>) {
                txt_refund_money.tvIcon.setTextColor(ContextCompat.getColor(this@RefundActivity, R.color.red))
                txt_refund_money.setIcon(StringUtils.insertComma(data.message?.balance ?: "0", 2))
                txt_refund_red.tvIcon.setTextColor(ContextCompat.getColor(this@RefundActivity, R.color.red))
                txt_refund_red.setIcon(StringUtils.insertComma(data.message?.rp_balance ?: "0", 2))
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<RefundMoney>) {

            }

        })
    }

    //加载理由
    private fun loadReason() {
        ApiManager2.post(this, hashMapOf(), Constant.ESHOP_RETURN_REASON, object : ApiManager2.OnResult<BaseBean<ArrayList<Reason>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Reason>>) {
                if (data.message?.isNotEmpty() == true) {
                    reasonList.clear()
                    reasonList.addAll(data.message ?: arrayListOf())
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ArrayList<Reason>>) {

            }
        })
    }

    //退货退款申请
    private fun request() {

        if (reason.isEmpty()){
            if (type == 1){
                ToastUtil.show("请选择退款理由！")
            }else{
                ToastUtil.show("请选择退货理由！")
            }
            return
        }

        val params = HashMap<String, String>()
        params["order_sn"] = order_sn
        params["sku_id"] = sku_id
        params["order_type"] = type.toString()
        params["reason"] = reason
        params["explain"] = edit_reason.text.toString()
        if (type == 2) {
            val jsonArray = JSONArray()
            view_up.list.forEach {
                jsonArray.put(it)
            }
            params["image"] = jsonArray.toString()
        }
        showLoading()
        ApiManager2.post(this, params, Constant.ESHOP_RETURN_APPLY, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                hideLoading()
                if (data.status == "200") {
                    goods?.style = type
                    EventBus.getDefault().post(Order("", "", "", "", "", "", "", arrayListOf()))
                    finish()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(code: String, message: String) {
                hideLoading()
            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }
}