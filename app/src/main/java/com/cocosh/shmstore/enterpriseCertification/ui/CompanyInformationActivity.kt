package com.cocosh.shmstore.enterpriseCertification.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
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
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.contrat.EntLicenseContrat
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.LicenseBean
import com.cocosh.shmstore.enterpriseCertification.ui.presenter.EntLicensePresenter
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newCertification.ui.CertificationAddressActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.utils.ocr.RecognizeService
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.google.gson.Gson
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_company_info.*
import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap


/**
 *
 * Created by lmg on 2017/11/1.
 */
class CompanyInformationActivity : BaseActivity(), EntLicenseContrat.IView {
    override fun reTryGetData() {

    }

    private var isClick = true
    private val REQUEST_CODE_BUSINESS_LICENSE = 123
    private var licensePath: String? = null
    private var pageType: Int? = null
    private lateinit var licenseBean: LicenseBean
    private var hasGotToken = false
    private var mDialog: BottomPhotoDialog? = null
    var path = Environment.getExternalStorageDirectory()
    var mCameraFile: File? = null
    var mCropFile: File? = null
    var mGalleryFile: File? = null
    var licenseKey = ""
    var preserter = EntLicensePresenter(this, this)
    override fun setResultData(result: BaseModel<EntActiveInfoModel>) {
        if (result.success && result.code == 200) {
            //回传结果 刷新导航页
            SmApplication.getApp().setData(DataCode.COMPANY_NAME, licenseBean.getWords_result()?.单位名称?.words!!)
            SmApplication.getApp().setData(DataCode.LAYER_NAME, licenseBean.getWords_result()?.法人?.words!!)
            SmApplication.getApp().setData(DataCode.ACTIVE_INFO, result.entity!!)
            EnterpriseActiveActivity.start(this)
            finish()
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun setQINIUToken(result: String) {
        val licenseImg = UserManager.getUserId() + System.currentTimeMillis() + "license.jpg"//身份证背面图片

        //上传营业执照
        showLoading()
        UploadManager().put(licensePath, licenseImg, result, { key, info, _ ->
            runOnUiThread {
                hideLoading()
                if (info.isOK) {
                    //存储图片路径（用于接口提交数据）
                    licenseKey = Constant.QINIU_KEY_HEAD + key
                    chackData()
                } else {
                    if (info.statusCode == ResponseInfo.InvalidToken) {
                        SmApplication.getApp().removeData(DataCode.QINIU_TOKEN)
                    }
                    ToastUtil.show("提交失败，请稍后重试！")
                }
            }
        }, null)
    }

    override fun setLayout(): Int = R.layout.activity_company_info
    override fun initView() {
        titleManager.defaultTitle("营业执照验证")
        licensePath = intent.getStringExtra("licensePath")
        pageType = intent.getIntExtra("PAGE_TYPE", -1)
        mDialog = BottomPhotoDialog(this)
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
            pullData(licensePath!!)
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

        edt_code.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                edt_code.background = null
                edt_name.background = null
                edt_layer_name.background = null
            }
        }

        edt_name.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                edt_code.background = null
                edt_name.background = null
                edt_layer_name.background = null
            }
        }

        edt_layer_name.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                edt_code.background = null
                edt_layer_name.background = null
                edt_name.background = null
            }
        }

        mDialog?.setOnItemClickListener(object : BottomPhotoDialog.OnItemClickListener {
            override fun onTopClick() {
                //拍照
                startCamera()
            }

            override fun onBottomClick() {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, Constant.SELECT_PIC_NOUGAT)
            }
        })

        btn_input.setOnClickListener {
            //检查数据
            preserter.getQINNIUToken()
        }

        iv_end_time.setOnTouchListener { _, _ ->
            return@setOnTouchListener !isClick
        }
    }

    private fun chackData() {
        if (TextUtils.isEmpty(licenseKey)) {
            ToastUtil.show("请上传营业执图片！")
            return
        }
        if (TextUtils.isEmpty(edt_code.text) || edt_code.text.length !in 17..21) {
            edt_code.setBackgroundResource(R.drawable.shape_rectangle_round)
            ToastUtil.show("请输入正确的统一社会信用代码！")
            return
        }
        if (TextUtils.isEmpty(edt_name.text) || edt_name.text.length < 2) {
            edt_name.setBackgroundResource(R.drawable.shape_rectangle_round)
            ToastUtil.show("请输入正确的公司名称！")
            return
        }
        if (TextUtils.isEmpty(edt_layer_name.text) || edt_layer_name.text.length < 2) {
            edt_layer_name.setBackgroundResource(R.drawable.shape_rectangle_round)
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

        //上传数据
        val map = HashMap<String, String>()
        map["licenceImg"] = licenseKey
        map["corpTax"] = edt_code.text.toString()
        map["corpFname"] = edt_name.text.toString()
        map["bizAddress"] = "" //营业执照住址
        map["legalRepresentative"] = edt_layer_name.text.toString()
        map["foundingTime"] = tv_create.text.toString()//成立日期
        map["domicile"] = edt_address.text.toString()//住所
        map["registeredCapital"] = edt_money.text.toString()//注册资本
        map["registeredType"] = edt_money.text.toString()//注册类型
        map["scope"] = ""
        if (pageType == 222) {
            map["startTime"] = tv_start_time.text.toString() //营业执照有效期 开始
            map["endTime"] = tv_end_time.text.toString() //营业执照有效期 结束
            //跳转地址选择页（服务商），并保存数据，带到下一页
            SmApplication.getApp().setData(DataCode.FACILITATOR_KEY_MAP, map)
            numAuth(edt_code.text.toString())
        } else {
            if (tv_end_time.text.isNullOrEmpty()) {
                map["bizValidityPeriod"] = tv_start_time.text.toString() //营业执照有效期
            } else {
                map["bizValidityPeriod"] = tv_start_time.text.toString() + "至" + tv_end_time.text //营业执照有效期
            }
            //提交信息（企业主）
            preserter.pushData(map)
        }

    }

    override fun onListener(view: View) {
        when (view.id) {
            iv_img.id -> {
                val storage = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                val camera = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                if (storage || camera) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (storage) {
                            ActivityCompat.requestPermissions(this@CompanyInformationActivity,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    IntentCode.PERMISSIONS_EXTERNAL_STORAGE)
                        }
                        if (camera) {
                            ActivityCompat.requestPermissions(this@CompanyInformationActivity,
                                    arrayOf(Manifest.permission.CAMERA),
                                    IntentCode.PERMISSIONS_REQUEST_CAMERA)
                        }
                        return
                    }
                }
                mDialog?.show()
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
            initAccessToken(licensePath!!)
        }

        when (requestCode) {
            Constant.CAMERA_REQUEST_CODE -> {//照相后返回
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val inputUri = FileProvider.getUriForFile(this, "com.coco_sh.shmstore.provider", mCameraFile)//通过FileProvider创建一个content类型的Uri
                    val url = GetImagePath().getPath(this, inputUri)
                    GlideUtils.loadFullScreen(this, url, iv_img)
                    showLoading()
                    initAccessToken(url!!)
                } else {
                    val inputUri = Uri.fromFile(mCameraFile)
                    val url = GetImagePath().getPath(this, inputUri)
                    GlideUtils.loadFullScreen(this, url, iv_img)
                    showLoading()
                    initAccessToken(url!!)
                }
            }
            Constant.IMAGE_REQUEST_CODE -> {//版本<7.0  图库后返回
                if (data != null) {
                    // 得到图片的全路径
                    val uri = data.data
                    //crop(uri);
                    val url = GetImagePath().getPath(this, uri)
                    GlideUtils.loadFullScreen(this, url, iv_img)
                    showLoading()
                    initAccessToken(url!!)
                }
            }
            Constant.SELECT_PIC_NOUGAT//版本>= 7.0
            -> {
                val imgUri = File(GetImagePath().getPath(this, data?.data!!))
                val dataUri = FileProvider.getUriForFile(this, "com.coco_sh.shmstore.provider", imgUri)
                val url = GetImagePath().getPath(this, dataUri)
                GlideUtils.loadFullScreen(this, url, iv_img)
                showLoading()
                initAccessToken(url!!)
            }
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
                        showScanErrorDialog()
                        return@ServiceListener
                    }
                } catch (e: Exception) {
                    showScanErrorDialog()
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
                isShowLoading = false
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
                isShowLoading = false
            }

            override fun onCatch(data: BaseModel<Boolean>) {
            }

        })
    }
}