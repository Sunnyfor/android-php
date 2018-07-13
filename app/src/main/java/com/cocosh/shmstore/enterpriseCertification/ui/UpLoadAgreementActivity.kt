package com.cocosh.shmstore.enterpriseCertification.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.view.View
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.model.EntActiveInfoModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_upload_argeement.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*


/**
 * 上传委托凭证
 * Created by lmg on 2018/3/23.
 */
class UpLoadAgreementActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    private lateinit var mDialog: BottomPhotoDialog
    var path = Environment.getExternalStorageDirectory()
    private lateinit var mCameraFile: File
    private lateinit var mCropFile: File
    private lateinit var mGalleryFile: File

    private var updateFile: File? = null
    var token: String? = null
    private val uploadManager = UploadManager()
    private val params = HashMap<String, String>() //请求参数

    override fun setLayout(): Int = R.layout.activity_upload_argeement

    override fun initView() {
        titleManager.defaultTitle("身份验证")
        token = SmApplication.getApp().getData<String>(DataCode.QINIU_TOKEN, false)
        mDialog = BottomPhotoDialog(this)
        btn_upload.setOnClickListener(this)
        btnSure.setOnClickListener(this)
        mCameraFile = File(path, "IMAGE_FILE_NAME")//照相机的File对象
        mCropFile = File(path, "PHOTO_FILE_NAME")//裁剪后的File对象
        mGalleryFile = File(path, "IMAGE_GALLERY_NAME")//相册的File对象Co

        //法人获取身份证数据
        val front = SmApplication.getApp().getData<IDCardResult>(IDCardParams.ID_CARD_SIDE_FRONT, false)
        val back = SmApplication.getApp().getData<IDCardResult>(IDCardParams.ID_CARD_SIDE_BACK, false)
        val frontImg = SmApplication.getApp().getData<String>(DataCode.FRONT_URL, false)
        val backImg = SmApplication.getApp().getData<String>(DataCode.BACK_URL, false)

        //代办人身份证扫描结果
        val agentFront = SmApplication.getApp().getData<IDCardResult>(DataCode.AGENT_FRONT_DATA, false)
        val agentBack = SmApplication.getApp().getData<IDCardResult>(DataCode.AGENT_BACK_DATA, false)
        val agentFrontImg = SmApplication.getApp().getData<String>(DataCode.AGENT_FRONT_URL, false)
        val agentBackImg = SmApplication.getApp().getData<String>(DataCode.AGENT_BACK_URL, false)

        //法人参数
        front?.let {
            params["entrepreneurLegal.realName"] = front.name.toString() //真实姓名
            params["entrepreneurLegal.sex"] = front.gender.toString() //性别
            params["entrepreneurLegal.ethnic"] = front.ethnic.toString() //民族
            params["entrepreneurLegal.birth"] = front.birthday.toString() //生日
            params["entrepreneurLegal.idNo"] = front.idNumber.toString() //身份证号
            params["entrepreneurLegal.idFront"] = Constant.QINIU_KEY_HEAD + frontImg//身份证正面
            params["entrepreneurLegal.idBack"] = Constant.QINIU_KEY_HEAD + backImg //身份证背面
            params["entrepreneurLegal.cardAddress"] = front.address.toString() //身份证地址
        }

        back?.let {
            params["entrepreneurLegal.validityPeriodStartTime"] = back.signDate.toString() //身份证有效期开始时间
            params["entrepreneurLegal.validityPeriodEndTime"] = back.expiryDate.toString()//身份证有效期结束时间
            params["entrepreneurLegal.issuingAgency"] = back.issueAuthority.toString() //身份证地址签发机关
        }

        agentFront?.let {
            params["entrepreneurAgency.realName"] = agentFront.name.toString() //真实姓名
            params["entrepreneurAgency.sex"] = agentFront.gender.toString() //性别
            params["entrepreneurAgency.ethnic"] = agentFront.ethnic.toString() //民族
            params["entrepreneurAgency.birth"] = agentFront.birthday.toString() //生日
            params["entrepreneurAgency.idNo"] = agentFront.idNumber.toString() //身份证号
            params["entrepreneurAgency.idFront"] = Constant.QINIU_KEY_HEAD + agentFrontImg//身份证正面
            params["entrepreneurAgency.idBack"] = Constant.QINIU_KEY_HEAD + agentBackImg //身份证背面
            params["entrepreneurAgency.cardAddress"] = agentFront.address.toString() //身份证地址
        }
        //代办人参数

        agentBack?.let {
            params["entrepreneurAgency.validityPeriodStartTime"] = agentBack.signDate.toString() //身份证有效期开始时间
            params["entrepreneurAgency.validityPeriodEndTime"] = agentBack.expiryDate.toString()//身份证有效期结束时间
            params["entrepreneurAgency.issuingAgency"] = agentBack.issueAuthority.toString() //身份证地址签发机关
        }

        initListener()
        mCameraFile.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        mGalleryFile.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
    }

    private fun initListener() {
        mDialog.setOnItemClickListener(object : BottomPhotoDialog.OnItemClickListener {
            override fun onTopClick() {
                //拍照
                val intentFromCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
                    val uriForFile = FileProvider.getUriForFile(this@UpLoadAgreementActivity, "com.coco_sh.shmstore.provider", mCameraFile)
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
                    intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intentFromCapture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                } else {
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile))
                }
                startActivityForResult(intentFromCapture, Constant.CAMERA_REQUEST_CODE)
            }

            override fun onBottomClick() {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, Constant.SELECT_PIC_NOUGAT)
            }
        })
    }


    override fun onListener(view: View) {
        when (view.id) {
            R.id.btn_upload -> {
                val storage = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                val camera = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                if (storage || camera) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (storage) {
                            ActivityCompat.requestPermissions(this@UpLoadAgreementActivity,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    IntentCode.PERMISSIONS_EXTERNAL_STORAGE)
                        }
                        if (camera) {
                            ActivityCompat.requestPermissions(this@UpLoadAgreementActivity,
                                    arrayOf(Manifest.permission.CAMERA),
                                    IntentCode.PERMISSIONS_REQUEST_CAMERA)
                        }
                        return
                    }
                }
                mDialog.show()
            }
            R.id.btnSure -> {
                if (token == null) {
                    tokenRequest()
                } else {
                    updateImg()
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, UpLoadAgreementActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            return
        }
        when (requestCode) {
            Constant.CAMERA_REQUEST_CODE -> {//照相后返回
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val inputUri = FileProvider.getUriForFile(this, "com.coco_sh.shmstore.provider", mCameraFile)//通过FileProvider创建一个content类型的Uri
                    iv_argeement.setImageURI(inputUri)
                } else {
                    val inputUri = Uri.fromFile(mCameraFile)
                    iv_argeement.setImageURI(inputUri)
                }
                updateFile = mCameraFile
            }
            Constant.IMAGE_REQUEST_CODE -> {//版本<7.0  图库后返回
                if (data != null) {
                    // 得到图片的全路径
                    val uri = data.data
                    //crop(uri);
                    iv_argeement.setImageURI(uri)
                    updateFile = File(GetImagePath().getPath(this, uri))
                }
            }
            Constant.SELECT_PIC_NOUGAT//版本>= 7.0
            -> {
                if (data != null) {
                    data.data?.let {
                        val imgUri = File(GetImagePath().getPath(this, it))
                        updateFile = File(imgUri.path)
                        val dataUri = FileProvider.getUriForFile(this, "com.coco_sh.shmstore.provider", imgUri)
                        iv_argeement.setImageURI(dataUri)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            IntentCode.PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!mDialog.isShowing) {
                        mDialog.show()
                    }
                    BottomPhotoDialog(this).show()
                } else {
                    ToastUtil.show(getString(com.baidu.ocr.ui.R.string.camera_permission_required))
                }
            }
            IntentCode.PERMISSIONS_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!mDialog.isShowing) {
                        mDialog.show()
                    }
                } else {
                    ToastUtil.show("需要存储权限")
                }
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDialog.cancel()
    }

    fun updateImg() {
        if (updateFile != null) {
            showLoading()
            val key = UserManager.getUserId() + System.currentTimeMillis() + "voucher.jpg"
            uploadManager.put(updateFile, key, token, { _: String, responseInfo: ResponseInfo, _: JSONObject ->
                runOnUiThread {
                    hideLoading()
                    if (responseInfo.isOK) {
                        ToastUtil.show("上传成功！")
                        params["entrepreneurAgency.proxyImg"] = Constant.QINIU_KEY_HEAD + key//委托协议
                        commit() //提交认证信息
                    } else {
                        if (responseInfo.statusCode == ResponseInfo.InvalidToken) {
                            SmApplication.getApp().removeData(DataCode.QINIU_TOKEN)
                        }
                        ToastUtil.show(responseInfo.error)
                    }
                }
            }, null)
        }
    }

    //获取七牛token
    private fun tokenRequest() {
        showLoading()
        val map = HashMap<String, String>()
        map["dataType"] = "1"
        ApiManager.post(this, map, Constant.FACE_TOKEN, object : ApiManager.OnResult<String>() {

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
                    token = jsonObject.optString("token")
                    if (TextUtils.isEmpty(token)) {
                        LogUtil.i("七牛Token为空")
                        ToastUtil.show("获取认证信息失败，请稍后重试")
                    } else {
                        updateImg()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    //提交认证信息
    private fun commit() {
        ApiManager.post(this, params, Constant.SAVE_IDENTITY, object : ApiManager.OnResult<BaseModel<EntActiveInfoModel>>() {
            override fun onFailed(e: Throwable) {
                hideLoading()
            }

            override fun onSuccess(data: BaseModel<EntActiveInfoModel>) {
                LogUtil.i("结果：" + data)
                if (data.success) {

                    if (data.entity != null) {
                        SmApplication.getApp().setData(DataCode.ACTIVE_INFO, data.entity)
                    }
                    startActivity(Intent(this@UpLoadAgreementActivity, EnterpriseActiveActivity::class.java))
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onCatch(data: BaseModel<EntActiveInfoModel>) {

            }
        })
    }
}