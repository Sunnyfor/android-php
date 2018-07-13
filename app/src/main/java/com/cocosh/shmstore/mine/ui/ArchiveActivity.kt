package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.*
import com.cocosh.shmstore.person.PersonRsultActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.cocosh.shmstore.widget.dialog.OnDialogResult
import com.cocosh.shmstore.widget.dialog.SelectDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_archive.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*


/**
 * 档案页面
 * Created by zhangye on 2018/4/8.
 */
class ArchiveActivity : BaseActivity(), BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {

    private var works = arrayListOf<IndustryModel>()
    private var uploadManager = UploadManager()
    private var file: File? = null
    private var token: String? = null

    override fun reTryGetData() {
        hideReTryLayout()
        loadData()
    }

    private lateinit var cameraUtils: CameraPhotoUtils
    private lateinit var pickerViewUtils: PickerViewUtils
    private var thread: Thread? = null
    var workDialog: SelectDialog? = null

    override fun setLayout(): Int = R.layout.activity_archive

    override fun initView() {
        cameraUtils = CameraPhotoUtils(this)
        cameraUtils.setAspectXY(resources.getDimension(R.dimen.w1080).toInt(),resources.getDimension(R.dimen.w1080).toInt())
        pickerViewUtils = PickerViewUtils(this)

        cameraUtils.onResultListener = this
        titleManager.defaultTitle("档案")
        isvHead.setOnClickListener(this)
        isvNickname.setOnClickListener(this)
        isvName.setOnClickListener(this)
        isvBirthday.setOnClickListener(this)
        isvAddress.setOnClickListener(this)
        isvSex.setOnClickListener(this)
        isvCompany.setOnClickListener(this)
        isvWork.setOnClickListener(this)
        isvInteresting.setOnClickListener(this)
        token = SmApplication.getApp().getData(DataCode.QINIU_TOKEN, false) //获取缓存的Token

        loadData()
    }

    override fun onListener(view: View) {
        when (view.id) {
            isvHead.id -> {
                val dialog = BottomPhotoDialog(this)
                dialog.setOnItemClickListener(this)
                dialog.show()
            }
            isvNickname.id -> {
                startActivity("昵称", isvNickname.getValue())
            }

            isvName.id -> {
                if (UserManager.getMemberEntrance()?.personStatus == AuthenStatus.PERSION_NO.type) {
                    val dialog = SmediaDialog(this)
                    dialog.setTitle("真实姓名需要进行实人认证")
                    dialog.setPositiveText("实人认证")
                    dialog.OnClickListener = View.OnClickListener {
                        SmApplication.getApp().setData(DataCode.AUTHER_ENTER, AuthenEnter.ARCHIVE)
                        val intent = Intent(this, ScanIdCardActivity::class.java)
                        intent.putExtra("type", "实人认证")
                        startActivity(intent)
                    }
                    dialog.show()
                } else {
                    startActivity(Intent(this, PersonRsultActivity::class.java))
                }
            }

            isvBirthday.id -> {
                pickerViewUtils.showDateYYMMDD(object : PickerViewUtils.OnPickerViewResultListener {
                    override fun onPickerViewResult(value: String) {
                        isvBirthday.setValue(value)
                        update("生日", value)
                    }
                })

            }

            isvSex.id -> {
                pickerViewUtils.showSex(object : PickerViewUtils.OnPickerViewResultListener {
                    override fun onPickerViewResult(value: String) {
                        update("性别", value)
                    }
                })
            }

            isvAddress.id -> {
                pickerViewUtils.showAddress(object : PickerViewUtils.OnAddressResultListener {
                    override fun onPickerViewResult(address: String, code: String) {
                        update("地区", code)
                    }
                })
            }

            isvCompany.id -> {
                startActivity("公司名称", isvCompany.getValue())
            }

            isvWork.id -> {
                loadIndustry() //加载所有行业弹窗
            }
        //兴趣爱好
            isvInteresting.id -> {
                startActivityForResult(Intent(this@ArchiveActivity, ArchiveInterestingActivity::class.java), IntentCode.IS_INPUT)
            }

        }
    }


    //打开相机
    override fun onTopClick() {
        cameraUtils.startCamera()
    }


    //打开相册
    override fun onBottomClick() {
        cameraUtils.startPoto()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    //回调处理
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_INPUT) {
            loadData()
        } else {
            cameraUtils.onActivityResult(requestCode, resultCode, data)
        }
    }


    /**
     * 图片回调结果
     */
    override fun onResult(file: File) {
        this.file = file
        tokenRequest()
    }


    fun startActivity(title: String, value: String?) {
        val it = Intent(this, SingleEditActivity::class.java)
        it.putExtra("title", title)
        it.putExtra("value", value)
        startActivityForResult(it, IntentCode.IS_INPUT)
    }


    private fun loadData() {
        ApiManager.get(1, this, hashMapOf(), Constant.GET_MYFILEINFO, object : ApiManager.OnResult<BaseModel<ArchiveModel>>() {
            override fun onSuccess(data: BaseModel<ArchiveModel>) {
                data.entity?.let {
                    val memberEntrance = UserManager.getMemberEntrance()
                    it.headPic?.let {
                        GlideUtils.loadHead(baseContext, it, ivHead)
                        memberEntrance?.headPic = it
                    }

                    it.nickName?.let {
                        isvNickname.setValue(it)
                        memberEntrance?.userNick = it
                    }

                    it.realName?.let {
                        isvName.setValue(it)
                        memberEntrance?.realName
                    }

                    it.birthday?.let {
                        isvBirthday.setValue(it)
                    }

                    it.sex?.let {
                        isvSex.setValue(it)
                    }

                    it.regionName?.let {
                        isvAddress.setValue(it)
                    }
                    it.companyName?.let {
                        isvCompany.setValue(it)
                    }
                    it.industryName?.let {
                        isvWork.setValue(it)
                    }

                    progressBar_big.progress = (it.degreeOfPerfection * 100).toInt()
                    progressBar_big.invalidate()
                    UserManager.setArchivalCompletion(it.degreeOfPerfection.toString())//更新缓存中的进度
                    memberEntrance?.degreeOfPerfection = it.degreeOfPerfection.toString()
                    UserManager.setMemberEntrance(memberEntrance)
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<ArchiveModel>) {
            }

        })
    }


    private fun loadIndustry() {
        if (!works.isEmpty()) {
            showIndustry()
            return
        }
        ApiManager.get(0, this, null, Constant.GET_INDUSTRYINFO, object : ApiManager.OnResult<BaseModel<ArrayList<IndustryModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<IndustryModel>>) {
                if (data.success) {
                    data.entity?.let {
                        works = it
                    }
                    showIndustry()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<ArrayList<IndustryModel>>) {

            }

        })
    }

    private fun showIndustry() {
        workDialog = SelectDialog(this)
        workDialog?.setData(works)
        workDialog?.onDialogResult = object : OnDialogResult {
            override fun onResult(result: Any) {
                workDialog?.dismiss()
                update("所属行业", (result as IndustryModel).industryId ?: "")
            }
        }
        workDialog?.show()
    }


    private fun update(type: String, value: String) {
        val params = hashMapOf<String, String>()
        when (type) {
            "头像" -> params["headPic"] = value
            "昵称" -> params["nickName"] = value
            "生日" -> params["birthday"] = value
            "性别" -> params["sex"] = value
            "公司名称" -> params["companyName"] = value
            "所属行业" -> params["industryId"] = value
            "地区" -> params["areaCode"] = value
        }

        ApiManager.post(this, params, Constant.UPDATE_MYFILESINFO, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success) {
                    loadData()
                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<String>) {

            }

        })

    }


    //获取七牛token
    private fun tokenRequest() {
        showLoading()

        if (token != null) {
            updatePhoto()
            return
        }
        val map = HashMap<String, String>()
        map["dataType"] = "1"
        ApiManager.get(0, this, map, Constant.FACE_TOKEN, object : ApiManager.OnResult<String>() {

            override fun onCatch(data: String) {}

            override fun onFailed(e: Throwable) {
                hideLoading()
                LogUtil.d("获取token失败" + e)
            }

            override fun onSuccess(data: String) {
                LogUtil.d("获取七牛Token结果：" + data)
                try {
                    val jsonObject = JSONObject(data)
                    val token = jsonObject.optString("token")
                    if (TextUtils.isEmpty(token)) {
                        ToastUtil.show("七牛Token为空")
                    } else {
                        //七牛token存储到内存
                        this@ArchiveActivity.token = token
                        SmApplication.getApp().setData(DataCode.QINIU_TOKEN, token)
                        updatePhoto()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun updatePhoto() {
        val fileName = "${System.currentTimeMillis()}head.jpg"

        file?.let {
            uploadManager.put(it, fileName, token, { _, info, _ ->
                info?.let {
                    runOnUiThread {
                        if (it.isOK) {
                            update("头像", Constant.QINIU_KEY_HEAD + fileName)
                        } else
                            hideLoading()
                        if (info.statusCode == ResponseInfo.InvalidToken) {
                            SmApplication.getApp().removeData(DataCode.QINIU_TOKEN)
                        }
                    }
                }
            }, null)
        }

    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        loadData()
    }
}