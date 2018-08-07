package com.cocosh.shmstore.login.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.forgetPsd.ui.activity.IdentifyMobileActivity
import com.cocosh.shmstore.home.HomeActivity
import com.cocosh.shmstore.login.ILoginContract
import com.cocosh.shmstore.login.LoginPresenter
import com.cocosh.shmstore.login.model.Login
import com.cocosh.shmstore.login.model.Login2
import com.cocosh.shmstore.login.model.LoginHistory
import com.cocosh.shmstore.register.RegisterActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.PhonePopUpWindow
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareConfig
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_login.*

/**
 *
 * Created by 张野 on 2017/10/20.
 */
class LoginActivity : BaseActivity(), ILoginContract.IView {
    override fun reTryGetData() {

    }

    private var isShow = false
    private var phone = ""
    private var phoneOK = false
    private var passOK = false
    private lateinit var popUpWindow: PhonePopUpWindow
    private var permissionUtil = PermissionUtil(this)

    var mPresenter: ILoginContract.IPresenter = LoginPresenter(this, this)

    override fun setLayout(): Int = R.layout.activity_login

    /**
     * 加载动画特效
     */
    @SuppressLint("NewApi")
    override fun onBeforeLayout() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            val enterTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.explode)
            window.enterTransition = enterTransition
            window.returnTransition = enterTransition

        }
    }

    override fun initView() {
        UserManager.setEmptyUser() //清除本地数据
        titleManager.defaultTitle("", resources.getString(R.string.iconClose), resources.getDimension(R.dimen.w42).toInt(), 0, View.OnClickListener {
            onBackPressed()
        })

        mPresenter.start() //开启历史数据回调

        //配置每次都拉取授权页面，避免尴尬
        val config = UMShareConfig()
        config.isNeedAuthOnGetUserInfo(true)
//        config.qq
        UMShareAPI.get(this).setShareConfig(config)

        itvDown.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        llReg.setOnClickListener(this)
        tvForgetPsd.setOnClickListener(this)
        itvWeChat.setOnClickListener(this)
        itvQQ.setOnClickListener(this)
        itvSina.setOnClickListener(this)

        edtPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    phoneOK = it.length >= 11
                    isOk()
                }
            }
        })

        edtPassword.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    passOK = it.length >= 6
                    isOk()
                }
            }
        })

        //设置光标的位置
        edtPhone.setSelection(edtPhone.text.length)
        //申请内存卡读写权限
        permissionUtil.storagePermission()
    }

    //点击事件
    override fun onListener(view: View) {
        when (view.id) {

            itvDown.id -> {
                isShow = true
                mPresenter.start()
                hideKeyboard(view.windowToken)
            }

            btnLogin.id -> {
                //登录方法
                if (phoneOK && passOK) {
                    phone = edtPhone.text.toString()
                    mPresenter.login(phone, edtPassword.text.toString())
                }

            }
            llReg.id -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

            tvForgetPsd.id -> {
                startActivity(Intent(this, IdentifyMobileActivity::class.java))
            }

            itvWeChat.id -> {
                if (UMShareAPI.get(application).isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    mPresenter.authVerify(SHARE_MEDIA.WEIXIN)
                } else {
                    ToastUtil.show(getString(R.string.notWeixin))
                }
            }

            itvQQ.id -> {
                if (UMShareAPI.get(application).isInstall(this, SHARE_MEDIA.QQ)) {
                    mPresenter.authVerify(SHARE_MEDIA.QQ)
                } else {
                    ToastUtil.show(getString(R.string.notQQ))
                }
            }

            itvSina.id -> {

                if (NetworkUtils.isNetworkAvaliable(this)) {
                    mPresenter.authVerify(SHARE_MEDIA.SINA)
                } else {
                    ToastUtil.show(getString(R.string.networkError))
                }

            }

        }
    }


    //登录信息回调
    override fun loginResult(isOtherLogin: Boolean) {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
//        if (result.success) {
//            SmApplication.getApp().setData(DataCode.CHANGE_USER, true)
//            result.entity?.let {
//                if (!isOtherLogin) {
//                    result.entity?.let {
//                        val history = LoginHistory(phone)
//                        UserManager.setLogin(it)
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        mPresenter.addHistory(history)
//                        finish()
//                        return
//                    }
//                    ToastUtil.show("登录信息获取失败！")
//                } else {
//                    UserManager.setLogin(it) //存储用户信息
//                    if (it.hasUser) {
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
//                    } else {
//                        startActivityForResult(Intent(this, BindingPhoneActivity::class.java), IntentCode.IS_REGIST)
//                    }
//                }
//            }
//        } else {
//            if (result.code == 3015) {
//                val dialog = SmediaDialog(this)
//                dialog.setTitle(getString(R.string.lockPhone))
//                dialog.singleButton()
//                dialog.show()
//            } else {
//                ToastUtil.show(result.message)
//            }
//        }
    }

    //历史记录回调
    override fun onHistory(history: List<LoginHistory>) {

        if (isShow) {
            popUpWindow = PhonePopUpWindow(this, history, R.layout.phone_history_layout, edtPhone, this)
            popUpWindow.setOnDismissListener {

            }

            edtPhone.postDelayed({
                popUpWindow.showAsDropDown(edtPhone)
            }, 100)

        } else {
            if (history.isNotEmpty()) {
                itvDown.visibility = View.VISIBLE
                edtPhone.setText(history[0].phone)
                phoneOK = true
            }
        }
    }


    /**
     * 存储登录凭证
     */
    private fun saveUserinfo(login: Login) {
        UserManager.setLogin(login)
    }


    /**
     * QQ回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_REGIST && resultCode == IntentCode.IS_REGIST) {
            finish()
        }
    }

    fun isOk() {
        if (phoneOK && passOK) {
            btnLogin.setBackgroundResource(R.color.red)
        } else {
            btnLogin.setBackgroundResource(R.color.grayBtn)
        }
    }

    override fun gonePhone(history: LoginHistory) {
        itvDown.visibility = View.INVISIBLE
        if (edtPhone.text.toString() == history.phone) {
            edtPhone.text = null
        }
        popUpWindow.dismiss()
    }
}