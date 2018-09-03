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
import com.cocosh.shmstore.home.model.MotifyBonus
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import kotlinx.android.synthetic.main.activity_bonus_send.*
import org.json.JSONArray
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


/**
 * 发送红包
 * Created by zhangye on 2018/4/19.
 */
class SendBonusActivity : BaseActivity(), BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {
    private val cameraPhotoUtils = CameraPhotoUtils(this)
    private val pickerViewUtils = PickerViewUtils(this)
    var bonusConfig: BonusConfig? = null
    private var money: Float = 1.0f
    var url: String? = null
    var file: File? = null
    var targetAreaCode: String? = null
    //    var isfill = false //是否填充历史数据
    var type: String? = null
    var price = "1"
    var rules = "小于"
    var rulesCount = "1"
    var motifyBonus: MotifyBonus? = null

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

                val bcount = BigDecimal(bonusCount)
                val bigPrice = BigDecimal(price)
                money = bcount.multiply(bigPrice).toFloat()
                tvAmountValue.text = StringUtils.insertComma(money)
                tvGetBonusCountValue.text = bonusCount
                tvAdCountValue.text = bonusCount
                if (type != null){
                    tvMoney.text = ("需支付 ${StringUtils.insertComma(money)}元")
                }
            }
        })
        if (type != null) {
            loadData()
        } else {
            loadMotifyData()
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
                }, true)
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

                if (type != null){
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
                }else{
                    nextMotify()
                }
            }
        }

    }

    override fun reTryGetData() {
        if (type != null) {
            loadData()
        } else {
            loadMotifyData()
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

//    fun loadData() {
//        ApiManager.post(1, this, hashMapOf(), Constant.BONUS_PARAMS, object : ApiManager.OnResult<BaseModel<BonusConfig>>() {
//            override fun onSuccess(data: BaseModel<BonusConfig>) {
//                if (data.success) {
//                    bonusConfig = data.entity
//                    initEditConfig()
//
//                    if (bonusConfig?.prevAction != 0) {
//                        val dialog = SmediaDialog(this@SendBonusActivity)
//                        dialog.setTitle("您还有未发完的红包，\n" + "是否继续发送？")
//                        dialog.OnClickListener = View.OnClickListener {
//                            isfill = true
//                            bonusConfig?.businessRedPacketInfo?.let {
//
//                                targetAreaCode = it.targetAreaCode
//                                edtName.setText(it.redPacketName)
//                                edtName.setSelection(edtName.text.length)
//                                url = it.redPacketImg
//                                Glide.with(this@SendBonusActivity).load(url).into(ivPhoto)
//                                isvLocation.setValue(it.targetAreaName)
//                                it.startTime?.split(" ")?.get(0)?.let {
//                                        isvTime.setValue(StringUtils.isTimeOut(it))
//                                }
//
//                                edtNumber.setText(it.redPacketCount.toString())
//                            }
//                        }
//
//                        dialog.cancelOnClickListener = View.OnClickListener {
//                            bonusConfig?.let {
//                                it.orderNumber = it.newOrderNumber
//                            }
//                        }
//
//                        dialog.show()
//                    }
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//
//            }
//
//            override fun onCatch(data: BaseModel<BonusConfig>) {
//
//            }
//        })
//
//    }

    fun loadData() {
        val params = HashMap<String, String>()
        params["type_id"] = type ?: ""
        ApiManager2.get(1, this, params, Constant.RP_TYPE_ATTRS, object : ApiManager2.OnResult<BaseBean<ArrayList<BonusAttr>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<BonusAttr>>) {
                data.message?.let {
                    it.forEach {

                        if (it.keyword == "price") {
                            val jsonArray = JSONArray(it.limit[0].rules)
                            price = jsonArray.optString(0)
                            tvPrice.text = ("￥${price}元/个")
                        }

                        if (it.keyword == "total") {

                            val jsonArray = JSONArray(it.limit[0].rules)
                            val jsonObj = jsonArray.getJSONObject(0)
                            if (jsonObj.optString(">=") != "") {
                                rules = "小于"
                                rulesCount = jsonObj.optString(">=")
                            }

                            if (jsonObj.optString("<=") != "") {
                                rules = "大于"
                                rulesCount = jsonObj.optString("<=")
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
    }


    private fun loadMotifyData() {
        val params = HashMap<String, String>()
        params["rp_id"] = intent.getStringExtra("id")
        ApiManager2.get(1, this, params, Constant.MYSELF_SENDRP_RPINFO, object : ApiManager2.OnResult<BaseBean<MotifyBonus>>() {
            override fun onSuccess(data: BaseBean<MotifyBonus>) {
                data.message?.let {
                    motifyBonus = it
                    edtName.setText(it.name)
                    edtName.setSelection(it.name.length)

                    isvLocation.setValue("${it.pos_prov}-${it.pos_city}") //投放位置
                    isvLocation.setOnClickListener(null)

                    isvTime.setValue(StringUtils.dateFormat(it.pubtime))
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

            override fun onCatch(data: BaseBean<MotifyBonus>) {
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
        paramsList.add(BonusParam("base", "pubtime", isvTime.getValue()))

        paramsList.add(BonusParam("base", "total", edtNumber.text.toString()))

        paramsList.add(BonusParam("base", "amount", money.toString()))

        SmApplication.getApp().setData(DataCode.SEND_BONUS, paramsList) //传递红包数据

        val intent = Intent(this@SendBonusActivity, SendBonusDetailActivity::class.java)
        intent.putExtra("profit", tvAmountValue.text.toString())
        intent.putExtra("money",money.toString())
        intent.putExtra("type", type)
        startActivityForResult(intent, 0)

    }

    private fun nextMotify(){
        motifyBonus?.name = edtName.text.toString()
        SmApplication.getApp().setData(DataCode.MOTIFY_BONUS,motifyBonus)
        val intent = Intent(this@SendBonusActivity, SendBonusDetailActivity::class.java)
        startActivityForResult(intent,0)
    }



    //上传图片
    private fun updateImage() {
        showLoading()
        file?.let {
            ApiManager2.postImage(this, it.path, Constant.COMMON_UPLOADS, object : ApiManager2.OnResult<BaseBean<ArrayList<String>>>() {
                override fun onSuccess(data: BaseBean<ArrayList<String>>) {
                    data.message?.let {
                        Glide.with(this@SendBonusActivity).load(file).into(ivPhoto)
                        if (type != null) {
                            url = it[0]
                        } else {
                            motifyBonus?.image = it[0]
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

}