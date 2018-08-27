package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.baiduScan.ScanIdCardActivity
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.model.AuthenEnter
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.model.IndustryModel
import com.cocosh.shmstore.mine.model.MemberEntrance2
import com.cocosh.shmstore.person.PersonRsultActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import com.cocosh.shmstore.widget.dialog.OnDialogResult
import com.cocosh.shmstore.widget.dialog.SelectDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_archive.*
import java.io.File


/**
 * 档案页面
 * Created by zhangye on 2018/4/8.
 */
class ArchiveActivity : BaseActivity(), BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {

    private var works = arrayListOf<IndustryModel>()
    private var uploadManager = UploadManager()
    private var file: File? = null
    private var token: String? = null
    private var interest: String? = null

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
        cameraUtils.setAspectXY(resources.getDimension(R.dimen.w1080).toInt(), resources.getDimension(R.dimen.w1080).toInt())
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
                if (UserManager2.getCommonData()?.cert?.r == AuthenStatus.PERSION_NO.type) {
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
                        isvAddress.tag = address
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
                startActivityForResult(Intent(this@ArchiveActivity, InterestingActivity::class.java).putExtra("hobby", interest), IntentCode.IS_INPUT)
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
        updatePhoto()
    }


    fun startActivity(title: String, value: String?) {
        val it = Intent(this, SingleEditActivity::class.java)
        it.putExtra("title", title)
        it.putExtra("value", value)
        startActivityForResult(it, IntentCode.IS_INPUT)
    }


    private fun loadData() {
        UserManager2.loadMemberEntrance(this, object : ApiManager2.OnResult<BaseBean<MemberEntrance2>>() {
            override fun onSuccess(data: BaseBean<MemberEntrance2>) {
                data.message?.let {
                    UserManager2.setMemberEntrance(data.message)
                    GlideUtils.loadHead(baseContext, it.avatar, ivHead) //头像
                    isvNickname.setValue(it.nickname) //昵称
                    isvName.setValue(it.realname)
                    isvBirthday.setValue(it.birth)
                    isvSex.setValue(it.gender)
                    isvAddress.setValue(it.district)
                    isvCompany.setValue(it.company)
                    isvWork.setValue(it.industry_name)
                    progressBar_big.progress = (it.degree)
                    progressBar_big.invalidate()
                    interest = it.hobby
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<MemberEntrance2>) {

            }
        })

//        val memberEntrance2 = UserManager2.getMemberEntrance()
//        memberEntrance2?.let {
//            GlideUtils.loadHead(baseContext, it.avatar, ivHead) //头像
//            isvNickname.setValue(it.nickname) //昵称
//            isvName.setValue(it.name)
//            isvBirthday.setValue(it.birth)
//            isvSex.setValue(it.gender)
//            isvAddress.setValue(it.district)
//            isvCompany.setValue(it.company)
//            isvWork.setValue(it.industry_name)
//            progressBar_big.progress = (it.degree)
//            progressBar_big.invalidate()
//            interest = it.hobby
//        }
    }


    private fun loadIndustry() {
        if (!works.isEmpty()) {
            showIndustry()
            return
        }
        ApiManager2.get(0, this, null, Constant.INDUSTRY, object : ApiManager2.OnResult<BaseBean<ArrayList<IndustryModel>>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<ArrayList<IndustryModel>>) {
                data.message?.let {
                    works = it
                }
                showIndustry()
            }

            override fun onCatch(data: BaseBean<ArrayList<IndustryModel>>) {

            }

        })
    }

    private fun showIndustry() {
        workDialog = SelectDialog(this)
        workDialog?.setData(works)
        workDialog?.onDialogResult = object : OnDialogResult {
            override fun onResult(result: Any) {
                workDialog?.dismiss()
                val industry = result as IndustryModel
                val strsb = StringBuilder()
                strsb.append(industry.code + "-" + industry.name)
                update("所属行业", strsb.toString())
            }
        }
        workDialog?.show()
    }


    //更新用户资料
    private fun update(type: String, value: String) {
        val params = hashMapOf<String, String>()
        when (type) {
            "头像" -> params["avatar"] = value
            "生日" -> params["birth"] = value
            "性别" -> params["gender"] = if (value == "女") "0" else "1"
            "所属行业" -> params["industry"] = value.split("-")[0]
            "地区" -> {
                val addressCode = value.split("-")
                params["province"] = addressCode[0]
                params["city"] = addressCode[1]
                params["town"] = addressCode[2]
            }
        }

        UserManager2.updateMemberEntrance(this, params, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
//                when (type) {
//                    "头像" -> {
//                        UserManager2.getMemberEntrance()?.let {
//                            it.avatar = value
//                            UserManager2.setMemberEntrance(it)
//                        }
//                    }
//                    "生日" -> {
//                        UserManager2.getMemberEntrance()?.let {
//                            it.birth = value
//                            UserManager2.setMemberEntrance(it)
//                        }
//                    }
//                    "性别" -> {
//                        UserManager2.getMemberEntrance()?.let {
//                            it.gender = value
//                            UserManager2.setMemberEntrance(it)
//                        }
//                    }
//                    "所属行业" -> {
//                        UserManager2.getMemberEntrance()?.let {
//                            it.industry_name = value.split("-")[1]
//                            UserManager2.setMemberEntrance(it)
//                        }
//                    }
//                    "地区" -> {
//                        UserManager2.getMemberEntrance()?.let {
//                            it.district = isvAddress.tag.toString()
//                            UserManager2.setMemberEntrance(it)
//                        }
//                    }
//
//                }
                loadData()
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })


    }


    //上传头像
    private fun updatePhoto() {
        file?.let {
            ApiManager2.postImage(this, it.path, Constant.COMMON_UPLOADS, object : ApiManager2.OnResult<BaseBean<ArrayList<String>>>() {
                override fun onSuccess(data: BaseBean<ArrayList<String>>) {
                    data.message?.let {
                        update("头像", it[0])
                    }

                }

                override fun onFailed(code: String, message: String) {

                }

                override fun onCatch(data: BaseBean<ArrayList<String>>) {

                }

            })
        }

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        loadData()
    }


}