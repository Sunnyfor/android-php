package com.cocosh.shmstore.widget.dialog

import android.Manifest
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.login.ui.activity.LoginActivity
import com.cocosh.shmstore.mine.ui.ArchiveActivity
import com.cocosh.shmstore.utils.UserManager
import kotlinx.android.synthetic.main.dialog_default.*
import java.util.*

/**
 * 首媒默认样式的对话框
 * Created by zhangye on 2018/1/27.
 */
class SmediaDialog : Dialog, View.OnClickListener {
    private var baseActivity: BaseActivity
    var OnClickListener: View.OnClickListener? = null
    var cancelOnClickListener: View.OnClickListener? = null
    private var cancleIsFinish = false

    constructor(context: Context?) : this(context, 0)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        baseActivity = context as BaseActivity
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_default)
        tvCancel.setOnClickListener(this)
        tvSure.setOnClickListener(this)
        setCanceledOnTouchOutside(false)
        window.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onClick(v: View) {
        when (v.id) {
            tvCancel.id -> {
                cancelOnClickListener?.onClick(v)
                dismiss()
                if (cancleIsFinish) {
                    baseActivity.finish()
                }
            }
            tvSure.id -> {
                OnClickListener?.onClick(v)
                dismiss()
            }
        }
    }

    override fun setTitle(title: CharSequence?) {
        tvTitle.text = title
    }

    fun setDesc(desc: String) {
        tvDesc.visibility = View.VISIBLE
        tvDesc.text = desc
    }

    fun setDescColor(color:Int){
        tvDesc.setTextColor(color)
    }


    fun setPositiveText(posiText: String) {
        tvSure.text = posiText
    }

    fun setCancelText(posiText: String) {
        tvCancel.text = posiText
    }

    fun setPostion(position: Int) {
        tvSure.tag = position
    }

    fun singleButton() {
        tvCancel.visibility = View.GONE
        ivLine.visibility = View.GONE
    }

    /**
     * 网络错误的dialog
     */
    fun showNetWorkError() {
        setTitle("网络连接失败")
        singleButton()
        OnClickListener = View.OnClickListener {
            baseActivity.finish()
        }
        show()
    }

    fun showUpdataError(isFinish: Boolean) {
        setTitle("上传失败")
        if (isFinish) {
            singleButton()
            OnClickListener = View.OnClickListener {
                baseActivity.finish()
            }
        }
    }

    //跳转登录弹窗
    fun showLogin() {
        val intent = Intent(baseActivity, LoginActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            baseActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(baseActivity).toBundle())
        } else {
            baseActivity.startActivity(intent)
        }
//        setTitle("前往登录")
//        setPositiveText("去登录")
//        OnClickListener = View.OnClickListener {
//            val intent = Intent(baseActivity, LoginActivity::class.java)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                baseActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(baseActivity).toBundle())
//            } else {
//                baseActivity.startActivity(intent)
//            }
//        }
//        show()
        dismiss()
    }

    /**
     * 短信修改密码确认弹窗
     */
    fun showSmsMotifyPassword(photo: String, password: String, token: String, onResult: ApiManager.OnResult<BaseModel<String>>) {
        setTitle(baseActivity.resources.getString(R.string.surePass))
        setDesc(password)
        OnClickListener = View.OnClickListener {
            val map = HashMap<String, String>()
            map["userName"] = photo
            map["userPwd"] = password
            map["token"] = token
            ApiManager.post(baseActivity, map, Constant.SET_NEW_PSD, onResult)
        }
        show()
    }

    fun showSmsMotifyPassword(password: String, OnClickListener: View.OnClickListener) {
        setTitle(context.resources.getString(R.string.surePass))
        setDesc(password)
        this.OnClickListener = OnClickListener
        show()
    }




    //取消按钮销毁页面
    fun cancleFinish() {
        cancleIsFinish = true
    }


    //跳转完善档案
    fun showArchive() {
        setTitle("请完善档案")
        setPositiveText("现在前往")
        setCancelText("稍后")
        OnClickListener = View.OnClickListener {
            val intent = Intent(baseActivity, ArchiveActivity::class.java)
            baseActivity.startActivity(intent)
        }
        show()
    }

    //显示定位失败弹窗
    fun showLocationError(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            setTitle("未获取到定位信息，等待定位成功后刷新")
        }else{
            setTitle("请检查定位权限")
        }
        show()
    }
}
