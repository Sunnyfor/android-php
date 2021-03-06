package com.cocosh.shmstore.home

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.home.model.BonusAttr
import com.cocosh.shmstore.home.model.BonusConfig
import com.cocosh.shmstore.home.model.BonusParam
import com.cocosh.shmstore.home.model.ModifyBonus
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.vouchers.model.Vouchers
import com.cocosh.shmstore.widget.VouchersListDialog
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.activity_bonus_send.*
import java.io.File
import java.math.BigDecimal
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * 发送红包
 * Created by zhangye on 2018/4/19.
 */
class SendBonusActivity : BaseActivity(), BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {
    private val cameraPhotoUtils = CameraPhotoUtils(this)
    private val pickerViewUtils = PickerViewUtils(this)
    private var bonusConfig: BonusConfig? = null
    private var money: Float = 1.0f
    var url: String? = null
    var file: File? = null
    var targetAreaCode: String? = null
    var type: String? = null
    var price = "1"
    var rules = "小于"
    var rulesCount = "1"
    var modifyBonus: ModifyBonus? = null
    private var selectMap = HashMap<String,Vouchers>()
    private var vouchersCode:String? = null //优惠券Code
    var arrayList:ArrayList<Vouchers>? = null

    override fun setLayout(): Int = R.layout.activity_bonus_send

    override fun initView() {

        SmApplication.getApp().addActivity(DataCode.BONUS_SEND_ACTIVITYS, this)
        titleManager.defaultTitle("发红包")
        ivPhoto.setOnClickListener(this)
        cameraPhotoUtils.setAspectXY(resources.getDimension(R.dimen.w640).toInt(), resources.getDimension(R.dimen.w360).toInt())
        cameraPhotoUtils.onResultListener = this
        isvLocation.setOnClickListener(this)
        isvLocation.setValue("请选择地区")
        isvTime.setOnClickListener(this)
        isvTime.setValue("请选择时间")
        btnNext.setOnClickListener(this)
        type = intent.getStringExtra("type")

        edtNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.indexOf("0") == 0) {
                    edtNumber.setText("")
                    return
                }

                var bonusCount = s.toString()
                if (bonusCount.isEmpty()) {
                    bonusCount = "0"
                }

                val bCount = BigDecimal(bonusCount)
                val bigPrice = BigDecimal(price)
                money = bCount.multiply(bigPrice).toFloat()
                tvAmountValue.text = StringUtils.insertComma(money)
                tvGetBonusCountValue.text = bonusCount
                tvAdCountValue.text = bonusCount
                if (type != null) {
                    tvMoney.text = StringUtils.insertComma(money)
                    tvDMoney.text = StringUtils.insertComma(money)
                }
            }
        })

        vNumber.setOnClickListener{}

        cbUse.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                llDefault.visibility = View.GONE
                llDiscount.visibility = View.VISIBLE
                vNumber.visibility = View.VISIBLE
                edtNumber.isCursorVisible = false
                loadDiscount(true)
            }else{
                llDefault.visibility = View.VISIBLE
                llDiscount.visibility = View.GONE
                vNumber.visibility = View.GONE
                edtNumber.isCursorVisible = true
            }
        }

        if (type != null) {
            loadData()
        } else {
            loadModifyData()
        }

        //点击弹窗优惠券
        rlVouchers.setOnClickListener { _ ->
            arrayList?.let {
                VouchersListDialog(it,if(cbUse.isChecked)selectMap else hashMapOf(),this@SendBonusActivity){
                    loadDiscount(true)
                }.show()
            }

        }

        rlUse.setOnClickListener {
            cbUse.isChecked = !cbUse.isChecked
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            ivPhoto.id -> {
                val dialog = BottomPhotoDialog(this)
                dialog.setOnItemClickListener(this)
                dialog.show()
            }
            isvLocation.id -> {
                pickerViewUtils.showSendBonusAddress(object : PickerViewUtils.OnAddressResultListener {
                    override fun onPickerViewResult(address: String, code: String) {
                        targetAreaCode = code
                        isvLocation.setValue(address)
                        LogUtil.i("选择的城市Code:$targetAreaCode")
                    }
                })
            }
            isvTime.id -> {
                pickerViewUtils.showReleaseTime(object : PickerViewUtils.OnPickerViewResultListener {
                    override fun onPickerViewResult(value: String) {
                        isvTime.setValue(value)
                    }
                })
            }
            btnNext.id -> {

                if (edtName.text.isEmpty()) {
                    ToastUtil.show("红包名称不能为空")
                    return
                }

                if (url == null) {
                    ToastUtil.show("红包图片不能为空")
                    return
                }

                if (isvLocation.getValue() == "请选择地区") {
                    ToastUtil.show("请选择地区")
                    return
                }

                if (isvTime.getValue() == "请选择时间") {
                    ToastUtil.show("请选择时间")
                    return
                }

                if (type != null) {
                    if (rules == "小于") {
                        if (edtNumber.text.isEmpty() || edtNumber.text.toString().toInt() < rulesCount.toInt()) {
                            ToastUtil.show("红名数量不能小于$rulesCount")
                            return
                        }
                    } else {
                        if (edtNumber.text.isEmpty() || edtNumber.text.toString().toInt() > rulesCount.toInt()) {
                            ToastUtil.show("红名数量不能大于$rulesCount")
                            return
                        }
                    }
                    nextBonus()
                } else {
                    nextMotify()
                }
            }
        }

    }

    override fun reTryGetData() {
        if (type != null) {
            loadData()
        } else {
            loadModifyData()
        }
    }


    override fun onTopClick() {
        cameraPhotoUtils.startCamera()
    }

    override fun onBottomClick() {
        cameraPhotoUtils.startPoto()
    }

    override fun onResult(file: File) {
        this.file = file
        updateImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraPhotoUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == IntentCode.FINISH) {
            finish()
        } else {
            cameraPhotoUtils.onActivityResult(requestCode, resultCode, data)
        }
    }



    fun loadData() {
        val params = HashMap<String, String>()
        params["type_word"] = type ?: ""
        ApiManager2.get(1, this, params, Constant.RP_TYPE_ATTRS, object : ApiManager2.OnResult<BaseBean<ArrayList<BonusAttr>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<BonusAttr>>) {
                data.message?.let { it ->
                    it.forEach {

                        if (it.keyword == "price") {

                            price = it.limit[0].rules[0].toString()
                            tvPrice.text = ("￥${price}元/个")
                        }

                        if (it.keyword == "total") {
                            val map = (it.limit[0].rules[0] as LinkedTreeMap<*, *>)
                            if (map[">="] != null) {
                                rules = "小于"
                                rulesCount = (map[">="] as Double).toInt().toString()
                            }

                            if (map["<="] != null) {
                                rules = "大于"
                                rulesCount = (map["<="] as Double).toInt().toString()
                            }
                            edtNumber.hint = ("红包投放数量不得$rules$rulesCount")
                        }
                    }
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<ArrayList<BonusAttr>>) {

            }
        })
            loadVouchers()
    }


    private fun loadModifyData() {
        val params = HashMap<String, String>()
        params["no"] = intent.getStringExtra("id")
        ApiManager2.get(1, this, params, Constant.MYSELF_SENDRP_RPINFO, object : ApiManager2.OnResult<BaseBean<ModifyBonus>>() {
            override fun onSuccess(data: BaseBean<ModifyBonus>) {
                data.message?.let {
                    modifyBonus = it
                    edtName.setText(it.name)
                    edtName.setSelection(it.name.length)

                    isvLocation.setValue("${it.pos_prov}-${it.pos_city}") //投放位置
                    isvLocation.setOnClickListener(null)

                    isvTime.setValue(StringUtils.timeStampFormatDateYYMMdd(it.pubtime))
                    isvTime.setOnClickListener(null)

                    edtNumber.isFocusable = false
                    edtNumber.setText(it.total)

                    url = it.image
                    Glide.with(this@SendBonusActivity).load(url).into(ivPhoto)
                    price = it.price
                    tvPrice.text = ("￥${price}元/个")
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ModifyBonus>) {
            }

        })
    }


    private fun nextBonus() {
        val params = hashMapOf<String, String>()
        params["type_id"] = type ?: ""

        val paramsList = ArrayList<BonusParam>()
        paramsList.add(BonusParam("base", "name", edtName.text.toString()))
        paramsList.add(BonusParam("base", "image", url ?: ""))

        val addressCode = targetAreaCode?.split("-")
        addressCode?.let {
            paramsList.add(BonusParam("base", "pos_prov", it[0]))
            paramsList.add(BonusParam("base", "pos_city", it[1]))
        }
        paramsList.add(BonusParam("base", "pubtime", StringUtils.dateFormatTimeStampYYMMdd(isvTime.getValue())))

        paramsList.add(BonusParam("base", "total", edtNumber.text.toString()))

        paramsList.add(BonusParam("base", "amount", money.toString()))

        paramsList.add(BonusParam("base", "price", price))


        SmApplication.getApp().setData(DataCode.SEND_BONUS, paramsList) //传递红包数据

        val intent = Intent(this@SendBonusActivity, SendBonusDetailActivity::class.java)
        intent.putExtra("profit", tvAmountValue.text.toString())
        intent.putExtra("money", money.toString())
        intent.putExtra("type", type)
        startActivityForResult(intent, 0)

    }

    private fun nextMotify() {
        modifyBonus?.name = edtName.text.toString()
        SmApplication.getApp().setData(DataCode.MOTIFY_BONUS, modifyBonus)
        val intent = Intent(this@SendBonusActivity, SendBonusDetailActivity::class.java)
        startActivityForResult(intent, 0)
    }


    //上传图片
    private fun updateImage() {
        showLoading()
        file?.let { it ->
            ApiManager2.postImage(this, it.path, Constant.COMMON_UPLOADS, object : ApiManager2.OnResult<BaseBean<ArrayList<String>>>() {
                override fun onSuccess(data: BaseBean<ArrayList<String>>) {
                    data.message?.let {
                        Glide.with(this@SendBonusActivity).load(file).into(ivPhoto)
                        if (type != null) {
                            url = it[0]
                        } else {
                            modifyBonus?.image = it[0]
                        }

                    }
                }

                override fun onFailed(code: String, message: String) {
                }

                override fun onCatch(data: BaseBean<ArrayList<String>>) {
                }


            })

        }
    }


    override fun onDestroy() {
        SmApplication.getApp().deleteActivity(DataCode.BONUS_SEND_ACTIVITYS, this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bonusConfig?.let {
            it.orderNumber = it.newOrderNumber
        }
    }

    fun loadDiscount(flag:Boolean) {
        SmApplication.getApp().getData<HashMap<String, Vouchers>>(DataCode.VOUCHERS_SELECT, true)?.let {
            selectMap = it
        }

        selectMap.let { it ->
            var discountPrice = 0f  //优惠总金额

            val selectIdSb = StringBuilder()

            it.forEach {
                selectIdSb.append(it.key).append(",")
                discountPrice += it.value.face_value.toInt()
            }
            vouchersCode = selectIdSb.toString().dropLast(1)
            //选中优惠券强制设置数量
            if (flag){
                val mCount = (discountPrice * 2 / price.toFloat()).toInt().toString()
                edtNumber.setText(mCount)
            }
            money /= 2
            tvDMoney.text = money.toString()
            cbUse.isChecked = flag
            tvDiscount.text = discountPrice.toString() //优惠金额
            tvDiscountPrice.text = ("${discountPrice}元红包礼券 >>")
            tvDiscountDesc.text = ("投放金额满${discountPrice}元可用")
        }
    }

    //加载全部优惠券
    private fun loadVouchers() {
        val params = hashMapOf<String, String>()
        params["type"] = "1"
        ApiManager2.post(this, params, Constant.COUPON_KIND, object : ApiManager2.OnResult<BaseBean<ArrayList<Vouchers>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Vouchers>>) {
                data.message?.let { it ->

                    arrayList = it

                    rlVouchers.visibility = View.VISIBLE
                    val hashMap = HashMap<String,Vouchers>()
                    it.forEach {
                        hashMap[it.code] = it
                    }

                    if ( intent.getBooleanExtra("isUse",false)){
                        loadDiscount(true)
                        SmApplication.getApp().setData(DataCode.VOUCHERS_SELECT, hashMap)
                    }else{
                        SmApplication.getApp().setData(DataCode.VOUCHERS_SELECT, hashMap)
                        loadDiscount(false)
                    }
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<ArrayList<Vouchers>>) {
            }
        })

    }
}