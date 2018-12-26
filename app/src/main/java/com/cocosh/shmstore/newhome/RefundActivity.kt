package com.cocosh.shmstore.newhome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.cocosh.shmstore.newhome.model.Courier
import com.cocosh.shmstore.newhome.model.Reason
import com.cocosh.shmstore.newhome.model.RefundMoney
import com.cocosh.shmstore.newhome.model.RefundShow
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.OnDialogResult
import com.cocosh.shmstore.widget.dialog.SelectDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_refund.*
import kotlinx.android.synthetic.main.layout_item_show.view.*
import org.greenrobot.eventbus.EventBus

class RefundActivity : BaseActivity() {

    var type = 1
    var sku_id = ""
    var order_sn = ""
    var reason = ""
    var codeType = ""
    var goods: Order.Goods? = null

    val reasonList = ArrayList<Reason>()
    val courierList = ArrayList<Courier>()

    val permissionUtil: PermissionUtil by lazy {
        PermissionUtil(this)
    }

    override fun setLayout(): Int = R.layout.activity_refund

    override fun initView() {

        type = intent.getIntExtra("type", 1)

        order_sn = intent.getStringExtra("order_sn") ?: ""

        intent.getStringExtra("hint")?.let {
            txt_status.text = it
        }

        val title = if (type == 1 || type == 3) "退款" else "退货"

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


        view_up.showLoading = {
            showLoading()
        }
        view_up.hideLoading = {
            hideLoading()
        }

        when (type) {
            1 -> {
                txt_status.text = "申请退款"
                txt_refund_money.visibility = View.VISIBLE
                txt_refund_red.visibility = View.VISIBLE
                queryMoney()
                loadReason()  //加载理由
                txt_reason.setOnClickListener(this)
            }
            2 -> {
                txt_status.text = "申请退货"
                ll_up.visibility = View.VISIBLE
                loadReason()  //加载理由
                txt_reason.setOnClickListener(this)

            }
            3, 4, 5, 7-> {
                edit_reason.isEnabled = false
                edit_reason.isFocusable = false
                btn_commit.visibility = View.GONE

                if (type == 5){
                 loadCourier()
                }
                show()
            }
        }
        txt_number.setOnClickListener(this)
        btn_commit.setOnClickListener(this)
        btn_service.setOnClickListener(this)
    }


    override fun onListener(view: View) {
        when (view.id) {
            R.id.btn_commit -> {
                if (type == 5) {
                    logistics()
                } else {
                    request()
                }
            }
            R.id.txt_reason -> {
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

            R.id.txt_number -> {
                val selectDialog = SelectDialog(this)
                selectDialog.onDialogResult = object : OnDialogResult {
                    override fun onResult(result: Any) {
                        val courierData = result as Courier
                        txt_number.setValue(courierData.com)
                        codeType = courierData.no
                        selectDialog.dismiss()
                    }
                }
                selectDialog.setCourier(courierList)
                selectDialog.show()
            }

            R.id.btn_service -> {
                val dialog = SmediaDialog(this)
                dialog.setTitle("小红娘客服：01078334322")
                dialog.setPositiveText("拨打")
                dialog.OnClickListener = View.OnClickListener {
                    if (permissionUtil.callPermission()) {
                        callPhone()
                    }
                }
                dialog.show()

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

        fun start(context: Context, type: Int, goods: Order.Goods, order_sn: String, hint: String) {
            SmApplication.getApp().setData(DataCode.ORDER_GOODS_ITEM, goods)
            context.startActivity(Intent(context, RefundActivity::class.java)
                    .putExtra("type", type)
                    .putExtra("order_sn", order_sn)
                    .putExtra("hint", hint))
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

        if (reason.isEmpty()) {
            if (type == 1) {
                ToastUtil.show("请选择退款理由！")
            } else {
                ToastUtil.show("请选择退货理由！")
            }
            return
        }

        val params = HashMap<String, Any>()
        params["order_sn"] = order_sn
        params["sku_id"] = sku_id
        if (type == 1) {
            params["order_type"] = "0"
        } else {
            params["order_type"] = "1"
        }
        params["reason"] = reason
        params["explain"] = edit_reason.text.toString()
        if (type == 2) {
            val imageSb = StringBuilder()
            view_up.list.forEach {
                imageSb.append(it).append(",")
            }
            if (imageSb.isNotEmpty()) {
                imageSb.deleteCharAt(imageSb.lastIndex)
                params["image"] = imageSb.toString()
            }
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

    private fun show() {
        val params = HashMap<String, String>()
        params["order_sn"] = order_sn
        params["sku_id"] = sku_id
        ApiManager2.post(this, params, Constant.ESHOP_RETURN_SHOW, object : ApiManager2.OnResult<BaseBean<RefundShow>>() {
            override fun onSuccess(data: BaseBean<RefundShow>) {
                data.message?.let { it ->
                    edit_reason.setText(it.explain ?: " ")
                    if (type == 3) {
                        //退款金额
                        txt_refund_money.tvIcon.setTextColor(ContextCompat.getColor(this@RefundActivity, R.color.red))
                        txt_refund_money.setIcon(StringUtils.insertComma(it.actual, 2))
                        txt_refund_red.tvIcon.setTextColor(ContextCompat.getColor(this@RefundActivity, R.color.red))
                        txt_refund_red.setIcon(StringUtils.insertComma(it.discount, 2))
                        txt_refund_money.visibility = View.VISIBLE
                        txt_refund_red.visibility = View.VISIBLE
                    }
                    if (type == 4 || type == 5 || type == 7) {
                        ll_up.visibility = View.VISIBLE
                        view_up.preViewModel()
                        view_up.setData(it.refund_image ?: arrayListOf())

                        if (type == 5 || type == 7){
                            txt_number.visibility = View.VISIBLE
                            edit_number.visibility = View.VISIBLE

                            if (type == 5){
                                btn_commit.visibility = View.VISIBLE
                            }

                            if (type == 7){
                                txt_number.setOnClickListener(null)
                                edit_number.isEnabled = false
                                edit_number.isFocusable = false
                                txt_number.setNoValueIcon(it.express_type)
                                edit_number.setText(it.express_code)
                            }
                        }
                    }

                    txt_time.visibility = View.VISIBLE
                    txt_time.text = StringUtils.timeStampFormatDateYYMMddHHssmm(it.addtime)
                    txt_reason.setNoValueIcon(it.reason ?: "")
                    txt_desc.text = (it.refuse_reason + it.refuse_explain)

                    if (it.status == "2") {
                        btn_service.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<RefundShow>) {

            }

        })
    }

    //提交物流
    private fun logistics() {

        if (codeType.isEmpty()){
            ToastUtil.show("请选择物流公司!")
            return
        }

        if (edit_number.text.isEmpty()){
            ToastUtil.show("请输入物流单号")
            return
        }

        val params = HashMap<String, Any>()
        params["order_sn"] = order_sn
        params["sku_id"] = sku_id
        params["type"] = codeType //(必填) 物流公司编号
        params["code"] = edit_number.text.toString() //(必填) 发货单号
        showLoading()
        ApiManager2.post(this, params, Constant.ESHOP_RETURN_SHIPPING, object : ApiManager2.OnResult<BaseBean<String>>() {
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

    //加载物流公司
    private fun loadCourier() {
        ApiManager2.post(this, hashMapOf(), Constant.ESHOP_RETURN_SHIPPING_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Courier>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Courier>>) {
                courierList.clear()
                courierList.addAll(data.message ?: arrayListOf())
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<ArrayList<Courier>>) {

            }

        })

    }


    @SuppressLint("MissingPermission")
    private fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:01078334322"))
        startActivity(intent)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCode.PHONE.type) {
            if (permissionUtil.checkPermission(permissions)) {
                callPhone()
            }
        }
    }
}