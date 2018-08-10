package com.cocosh.shmstore.enterpriseCertification.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Environment
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduFace.utils.FileUtil
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.LicenseBean
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntLicensePresenter
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newCertification.ui.CertificationAddressActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.utils.ocr.RecognizeService
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_company_info.*
import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 *
 * Created by lmg on 2017/11/1.
 */
class CompanyInformationActivity : BaseActivity(), EntLicenseContrat.IView, BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {
    override fun onResult(file: File) {
        showLoading()
        licensePath = file.absolutePath
        GlideUtils.loadFullScreen(this, licensePath, iv_img)
        licensePath?.let {
            initAccessToken(it)
        }
    }

    override fun onTopClick() {
        cameraUtils.startCamera()
    }

    override fun onBottomClick() {
        cameraUtils.startPoto()
    }

    override fun reTryGetData() {

    }

    private var isClick = true
    private val REQUEST_CODE_BUSINESS_LICENSE = 123
    private var licensePath: String? = null
    private var pageType: Int? = null
    private lateinit var licenseBean: LicenseBean
    private var hasGotToken = false
    var path = Environment.getExternalStorageDirectory()
    var mCameraFile: File? = null
    var mCropFile: File? = null
    var mGalleryFile: File? = null
    var licenseKey = ""
    private lateinit var cameraUtils: CameraPhotoUtils
    var preserter = EntLicensePresenter(this, this)
    override fun setResultData(result: BaseBean<String>) {
        hideLoading()
        EnterpriseActiveActivity.start(this)
        finish()
    }

    override fun updatePhoto() {
        chackData() //提交资料
        //上传营业执照
    }

    override fun setLayout(): Int = R.layout.activity_company_info
    override fun initView() {
        cameraUtils = CameraPhotoUtils(this)
        cameraUtils.setAspectXY(resources.getDimension(R.dimen.w1080).toInt(), resources.getDimension(R.dimen.h1920).toInt())
        cameraUtils.onResultListener = this
        titleManager.defaultTitle("营业执照验证")
        licensePath = intent.getStringExtra("licensePath")
        pageType = intent.getIntExtra("PAGE_TYPE", -1)
        mCameraFile = File(path, "IMAGE_FILE_NAME")//照相机的File对象
        mCropFile = File(path, "PHOTO_FILE_NAME")//裁剪后的File对象
        mGalleryFile = File(path, "IMAGE_GALLERY_NAME")//相册的File对象Co
        if (!mCameraFile?.parentFile?.exists()!!) mCameraFile!!.parentFile.mkdirs()
        if (!mGalleryFile?.parentFile?.exists()!!) mGalleryFile!!.parentFile.mkdirs()
        initData()
        initListener()
    }

    private fun initData() {
        if (pageType == 222) {
            btn_input.text = "下一步"
        } else {
            btn_input.text = "提交"
        }
        val filter2 = InputFilter { source, _, _, _, _, _ ->
            val speChat = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“'。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())
            if (matcher.find()) "" else null;
        }
        //保留布局内的过滤条件
        val mFilters = arrayOfNulls<InputFilter>(edt_name.filters.size)
        edt_name.filters.forEachIndexed<InputFilter?> { i: Int, inputFilter: InputFilter? ->
            if (inputFilter != null) {
                mFilters[i] = inputFilter
            }
        }
        mFilters[edt_name.filters.size - 2] = filter2
        edt_name.filters = mFilters

        if (!TextUtils.isEmpty(licensePath)) {
            showLoading()
            GlideUtils.loadFullScreen(this, licensePath, iv_img)
            licensePath?.let {
                pullData(it)
            }

        } else {
            showScanErrorDialog()
        }
    }

    private fun initListener() {
        iv_img.setOnClickListener(this)
        iv_start_time.setOnClickListener(this)
        iv_end_time.setOnClickListener(this)
        iv_create.setOnClickListener(this)
        tv_start_time.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.equals(tv_start_time.text, "长期")) {
                    tv_end_time.text = ""
                    isClick = false
                } else {
                    isClick = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        btn_input.setOnClickListener {
            //检查数据
            if (isFastClick()) {
                preserter.getUpdateResult()
            }
            showLoading()
        }

        iv_end_time.setOnTouchListener { _, _ ->
            return@setOnTouchListener !isClick
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraUtils.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults)
    }

    private fun chackData() {
//        if (TextUtils.isEmpty(licenseKey)) {
//            ToastUtil.show("请上传营业执图片！")
//            return
//        }
        if (TextUtils.isEmpty(edt_code.text) || edt_code.text.length !in 17..21) {
            ToastUtil.show("请输入正确的统一社会信用代码！")
            return
        }
        if (TextUtils.isEmpty(edt_name.text) || edt_name.text.length < 2) {
            ToastUtil.show("请输入正确的公司名称！")
            return
        }
        if (TextUtils.isEmpty(edt_layer_name.text) || edt_layer_name.text.length < 2) {
            ToastUtil.show("请输入正确的法人名！")
            return
        }

        if (TextUtils.isEmpty(tv_start_time.text)) {
            ToastUtil.show("请输入营业期限起始时间！")
            return
        } else {
            if (!TextUtils.equals(tv_start_time.text, "长期")) {
                if (TextUtils.isEmpty(tv_end_time.text)) {
                    ToastUtil.show("请输入营业期限终止时间！")
                    return
                }
            }
        }

        if (TextUtils.isEmpty(tv_create.text)) {
            ToastUtil.show("请输入成立日期！")
            return
        }
        licensePath?.let {
            ApiManager2.postImage(this, it, Constant.COMMON_UPLOADS, object : ApiManager2.OnResult<BaseBean<ArrayList<String>>>() {
                override fun onSuccess(data: BaseBean<ArrayList<String>>) {
                    data.message?.let {
                        licenseKey = it[0]
                        //上传数据
                        val map = HashMap<String, String>()
                        map["image"] = licenseKey
                        map["uscc"] = edt_code.text.toString()
                        map["name"] = edt_name.text.toString()
                        //        map["addr"] = "" //营业执照住址
                        map["legal"] = edt_layer_name.text.toString()
                        map["found"] = tv_create.text.toString().replace("年","-").replace("月","-").replace("日","")//成立日期
                        map["addr"] = edt_address.text.toString()//住所
                        map["capital"] = edt_money.text.toString()//注册资本
                        map["kind"] = edt_type.text.toString()//注册类型
                        map["beg_time"] = tv_start_time.text.toString().replace("年","-").replace("月","-").replace("日","") //营业执照有效期 开始
                        map["end_time"] = tv_end_time.text.toString().replace("年","-").replace("月","-").replace("日","") //营业执照有效期 结束

                        if (pageType == 222) {
                            //跳转地址选择页（服务商），并保存数据，带到下一页
                            SmApplication.getApp().setData(DataCode.FACILITATOR_KEY_MAP, map)
                            numAuth(edt_code.text.toString())
                        } else {
                            //提交信息（企业主）
                            preserter.pushData(map)
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

    override fun onListener(view: View) {
        when (view.id) {
            iv_img.id -> {
                val dialog = BottomPhotoDialog(this)
                dialog.setOnItemClickListener(this)
                dialog.show()
            }
            iv_start_time.id -> {
                showTimePicker(tv_start_time, true)
            }
            iv_end_time.id -> {
                showTimePicker(tv_end_time, true)
            }
            iv_create.id -> {
                showTimePicker(tv_create, false)
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(this@CompanyInformationActivity, CameraActivity::class.java)
        licensePath = FileUtil.getSaveFile(application, System.currentTimeMillis().toString()).absolutePath
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, licensePath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL)
        startActivityForResult(intent, IntentCode.REQUEST_CODE_BUSINESS_LICENSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 识别成功回调，营业执照识别
        if (requestCode == IntentCode.REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {
            showLoading()
            GlideUtils.loadFullScreen(this, licensePath, iv_img)
            licensePath?.let {
                initAccessToken(it)
            }

        } else {
            cameraUtils.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun pullData(path: String) {
        RecognizeService.recBusinessLicense(path, RecognizeService.ServiceListener { result ->
            LogUtil.e(result)
            hideLoading()
            if (!TextUtils.isEmpty(result)) {
                val gson = Gson()
                try {
                    licenseBean = gson.fromJson(result, LicenseBean::class.java)
                    if (TextUtils.equals(licenseBean.getWords_result()?.法人?.words, "无") || TextUtils.equals(licenseBean.getWords_result()?.社会信用代码?.words, "无")) {
                        if (!isFinishing) {
                            showScanErrorDialog()
                        }
                        return@ServiceListener
                    }
                } catch (e: Exception) {
                    if (!isFinishing) {
                        showScanErrorDialog()
                    }
                    return@ServiceListener
                }
                LogUtil.d("license result: " + result)
                //弹出确认信息弹窗
                showConfirmView(licenseBean)
            }
        })
    }

    /**
     * 自动填充弹窗
     */
    private fun showConfirmView(companyData: LicenseBean?) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle).create()
        builder.show()
        val window = builder.window
        window.setContentView(R.layout.dialog_ok)
        builder.setCancelable(false)
        val conpanyName = builder.findViewById(R.id.three_name) as TextView
        val conpanyNum = builder.findViewById(R.id.three_num) as TextView
        val conpanyP = builder.findViewById(R.id.three_person) as TextView
        val conpanyTime = builder.findViewById(R.id.three_time) as TextView
        val conpanyResidence = builder.findViewById(R.id.three_time) as TextView
        conpanyName.text = ("公司名称：" + companyData?.getWords_result()?.单位名称?.words)
        conpanyNum.text = ("社会信用号：" + companyData?.getWords_result()?.社会信用代码?.words)
        conpanyP.text = ("法定代表人：" + companyData?.getWords_result()?.法人?.words)
        conpanyTime.text = ("营业期限：" + companyData?.getWords_result()?.有效期?.words)
        conpanyResidence.text = ("地址:" + companyData?.getWords_result()?.地址?.words)
        builder.findViewById(R.id.re).setOnClickListener {
            builder.dismiss()
        }

        builder.findViewById(R.id.conf).setOnClickListener {
            if (!TextUtils.isEmpty(companyData?.getWords_result()?.单位名称?.words)
                    && !TextUtils.isEmpty(companyData?.getWords_result()?.社会信用代码?.words)) {
                inputData(companyData!!)
            } else {
                ToastUtil.show("扫描出错，请重新扫描！")
            }
            builder.dismiss()
        }
    }

    /**
     * 填充方法
     */
    private fun inputData(resultBean: LicenseBean) {
        edt_code.setText(resultBean.getWords_result()?.社会信用代码?.words)
        edt_name.setText(resultBean.getWords_result()?.单位名称?.words)
        edt_layer_name.setText(resultBean.getWords_result()?.法人?.words)
        if (!TextUtils.equals(resultBean.getWords_result()?.有效期?.words, "无")) {
            if (TextUtils.equals(resultBean.getWords_result()?.有效期?.words, "长期")) {
                tv_start_time.text = resultBean.getWords_result()?.有效期?.words
            } else {
                tv_end_time.text = resultBean.getWords_result()?.有效期?.words
            }
        }
        if (!TextUtils.equals(resultBean.getWords_result()?.成立日期?.words, "无")) {
            tv_start_time.text = resultBean.getWords_result()?.成立日期?.words
            tv_create.text = resultBean.getWords_result()?.成立日期?.words
        }
        edt_address.setText(resultBean.getWords_result()?.地址?.words)

    }

    //初始化Token并识别身份证信息
    private fun initAccessToken(filePath: String) {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.show(getString(R.string.networkError))
            return
        }

        if (hasGotToken) {
            pullData(filePath)
        } else {
            OCR.getInstance().initAccessToken(object : OnResultListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {
                    hasGotToken = true
                    pullData(filePath)
                }

                override fun onError(error: OCRError) {
                    error.printStackTrace()
                    hasGotToken = false
                    ToastUtil.show("获取token失败,请重试")
                }
            }, applicationContext)
        }
    }

    private fun showTimePicker(tv: TextView, isShow: Boolean) {
        val mBirthday = com.cocosh.shmstore.widget.wheel.TimePickerView(this, com.cocosh.shmstore.widget.wheel.TimePickerView.Type.YEAR_MONTH_DAY)
        mBirthday.show()
        mBirthday.setTime(Date())
        mBirthday.setTitle("")
        mBirthday.setLONGLayout(isShow)
        mBirthday.setCyclic(true)
        mBirthday.setCancelable(true)
        //时间选择后回调
        mBirthday.setOnTimeSelectListener { date ->
            if (date != "长期") {
                val dates = date.split("-")
                val newDate = dates[0] + "年" + dates[1] + "月" + dates[2] + "日"
                tv.text = newDate
            } else {
                tv.text = date
            }
        }
    }

    private fun showScanErrorDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("识别失败，请重新拍照")
        dialog.singleButton()
        dialog.show()
        dialog.OnClickListener = View.OnClickListener { startCamera() }
    }

    private fun numAuth(licenseNo: String) {
        val params = HashMap<String, String>()
        params["licenseNo"] = licenseNo
        ApiManager.get(0, this, params, Constant.FACILITOTAAR_AUTH_NUM, object : ApiManager.OnResult<BaseModel<Boolean>>() {
            override fun onSuccess(data: BaseModel<Boolean>) {
                hideLoading()
                if (data.success && data.code == 200) {
                    if (data.entity == true) {
                        startActivity(Intent(this@CompanyInformationActivity, CertificationAddressActivity::class.java).putExtra("FACILITATOR_TYPE", 333))
                    } else {
                        ToastUtil.show("营业执照号已注册！")
                    }
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
                hideLoading()
            }

            override fun onCatch(data: BaseModel<Boolean>) {
            }

        })
    }

    private val MIN_DELAY_TIME = 1000  // 两次点击间隔不能少于1000ms
    private var lastClickTime: Long = 0

    private fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime <= MIN_DELAY_TIME) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }
}