package com.cocosh.shmstore.home

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.BonusConfig
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_bonus_send.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.math.BigDecimal
import java.util.HashMap


/**
 * 发送红包
 * Created by zhangye on 2018/4/19.
 */
class SendBonusActivity : BaseActivity(), BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {
    private val cameraPhotoUtils = CameraPhotoUtils(this)
    private val pickerViewUtils = PickerViewUtils(this)
    var bonusConfig: BonusConfig? = null
    private var money: Float = 0.0f
    var url: String? = null
    var file: File? = null
    var targetAreaCode: String? = null
    var isfill = false //是否填充历史数据

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
        loadData()

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

                bonusConfig?.unitPriceRedPacket?.let {
                    val bcount = BigDecimal(bonusCount)
                    val price = BigDecimal(it.toString())
                    money = bcount.multiply(price).toFloat()
                    tvAmountValue.text = StringUtils.insertComma(money)
                    tvGetBonusCountValue.text = bonusCount
                    tvAdCountValue.text = bonusCount
                    tvMoney.text = ("需支付 ${StringUtils.insertComma(money)}元")
                }
            }
        })

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

                if (edtNumber.text.isEmpty() || edtNumber.text.toString().toInt() < bonusConfig?.minRedPacketCount ?: 0) {
                    ToastUtil.show("红名数量不能小于${bonusConfig?.minRedPacketCount}")
                    return
                }

                bonusConfig?.maxRedPacketCount?.let {
                    if (it != 0) {
                        if (edtNumber.text.toString().toInt() > it) {
                            ToastUtil.show("红名数量不能大于$it")
                            return
                        }
                    }
                }



                saveBonus()
            }
        }

    }

    override fun reTryGetData() {
        loadData()
    }


    override fun onTopClick() {
        cameraPhotoUtils.startCamera()
    }

    override fun onBottomClick() {
        cameraPhotoUtils.startPoto()
    }

    override fun onResult(file: File) {
        this.file = file
        tokenRequest()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraPhotoUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == IntentCode.FINISH){
            finish()
        }else{
            cameraPhotoUtils.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun loadData() {
        ApiManager.post(1, this, hashMapOf(), Constant.BONUS_PARAMS, object : ApiManager.OnResult<BaseModel<BonusConfig>>() {
            override fun onSuccess(data: BaseModel<BonusConfig>) {
                if (data.success) {
                    bonusConfig = data.entity
                    initEditConfig()

                    if (bonusConfig?.prevAction != 0) {
                        val dialog = SmediaDialog(this@SendBonusActivity)
                        dialog.setTitle("您还有未发完的红包，\n" + "是否继续发送？")
                        dialog.OnClickListener = View.OnClickListener {
                            isfill = true
                            bonusConfig?.businessRedPacketInfo?.let {

                                targetAreaCode = it.targetAreaCode
                                edtName.setText(it.redPacketName)
                                edtName.setSelection(edtName.text.length)
                                url = it.redPacketImg
                                Glide.with(this@SendBonusActivity).load(url).into(ivPhoto)
                                isvLocation.setValue(it.targetAreaName)
                                it.startTime?.split(" ")?.get(0)?.let {
                                        isvTime.setValue(StringUtils.isTimeOut(it))
                                }

                                edtNumber.setText(it.redPacketCount.toString())
                            }
                        }

                        dialog.cancelOnClickListener = View.OnClickListener {
                            bonusConfig?.let {
                                it.orderNumber = it.newOrderNumber
                            }
                        }

                        dialog.show()
                    }
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<BonusConfig>) {

            }
        })

    }

    fun initEditConfig() {
        bonusConfig?.let {
            edtNumber.hint = ("红包投放数量不得小于${it.minRedPacketCount}")
            tvPrice.text = ("￥${StringUtils.insertComma(it.unitPriceRedPacket)}元/个")

        }
    }


    private fun saveBonus() {
        val params = hashMapOf<String, String>()
        bonusConfig?.businessRedPacketInfo?.idRedPacketOrder?.let {
            params["idRedPacketOrder"] = it.toString()
        }
        params["orderNumber"] = bonusConfig?.orderNumber.toString()
        params["redPacketName"] = edtName.text.toString()

        if (!url?.contains(Constant.QINIU_KEY_HEAD)!!) {
            url = Constant.QINIU_KEY_HEAD + url
        }

        params["redPacketImg"] = url ?: ""
        params["targetAreaCode"] = targetAreaCode ?: ""
        params["redPacketCount"] = edtNumber.text.toString()
        params["redPacketTotalPrice"] = money.toString()
        params["startTime"] = isvTime.getValue() + " 00:00:00"
        ApiManager.post(this, params, Constant.BONUS_SAVE, object : ApiManager.OnResult<BaseModel<Int>>() {
            override fun onSuccess(data: BaseModel<Int>) {
                if (data.success) {
                    if (data.entity == null || data.entity == 0){
                        ToastUtil.show("提交失败")
                        return
                    }
                    bonusConfig?.businessRedPacketInfo?.idRedPacketOrder = data.entity ?: 0
                    SmApplication.getApp().setData(DataCode.BONUS_CONFIG, bonusConfig)

                    val intent = Intent(this@SendBonusActivity, SendBonusDetailActivity::class.java)
                    intent.putExtra("isfill", isfill)
                    intent.putExtra("money", tvAmountValue.text.toString())
                    intent.putExtra("idRedPacketOrder", data.entity.toString())
                    startActivityForResult(intent,0)
                } else {
                    ToastUtil.show(data.message)
                }

            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<Int>) {

            }
        })

    }


    //获取七牛token
    private fun tokenRequest() {
        showLoading()

        val map = HashMap<String, String>()
        map["dataType"] = "1"
        ApiManager.get(0, this, map, Constant.FACE_TOKEN, object : ApiManager.OnResult<String>() {

            override fun onCatch(data: String) {}

            override fun onFailed(e: Throwable) {
                hideLoading()
                LogUtil.d("获取token失败" + e)
            }

            override fun onSuccess(data: String) {
                hideLoading()
                LogUtil.d("获取七牛Token结果：" + data)
                try {
                    val jsonObject = JSONObject(data)
                    val mToken = jsonObject.optString("token")
                    if (TextUtils.isEmpty(mToken)) {
                        ToastUtil.show("七牛Token为空")
                    } else {
                        update(mToken)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     * 上传图片
     */
    private fun update(token: String) {
        showLoading()
        url = UserManager.getUserId() + System.currentTimeMillis() + "bonusPhoto.jpg"
        UploadManager().put(file, url, token, { _, info, _ ->
            runOnUiThread {
                hideLoading()
                if (info.isOK) {
                    Glide.with(this).load(file).into(ivPhoto)
                } else {
                    url = null
                    hideLoading()
                    ToastUtil.show("提交失败,请稍后重试")
                    LogUtil.i("七牛上传图片错误:${info.error}")
                }
            }
        }, null)
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