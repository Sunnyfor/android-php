package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.PayPassworType
import com.cocosh.shmstore.mine.model.ResetPass
import com.cocosh.shmstore.mine.presenter.IsPwdRightPresenter
import com.cocosh.shmstore.mine.ui.mywallet.AddBankCardActivity
import com.cocosh.shmstore.mine.ui.mywallet.BankCardMangerActivity
import com.cocosh.shmstore.mine.ui.mywallet.WithDrawActivity
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_set_pay_pwd_again.*
import java.util.*

/**
 * Created by lmg on 2018/5/4.
 * 设置密码
 */
class ReInputPayPwdActivity : BaseActivity(), MineContrat.IIsPwdRightView {
    private val pwd_s = ArrayList<ImageView>()
    private var mPwdCountNum: Int = 0
    private var mPassWord = ""
    var type = ""
    var pwd = ""
    var mPresenter = IsPwdRightPresenter(this, this)
    override fun setLayout(): Int = R.layout.activity_set_pay_pwd_again

    override fun isPwdRight(result: BaseBean<ResetPass>) {
        SetPayPwdActivity.start(this, "modify")
        SmApplication.getApp().setData(DataCode.RESET_PAY_PASS, result.message)
        finish()
//        } else if (result.code == 4015) {
//            showErrorDialog(result.message!!)
//        } else if (result.code == 4018) {
//            showAgainDialog(result.message!!)
//        } else {
//            ToastUtil.show(result.message)
//        }
    }

    override fun savePwdData(result: BaseModel<Boolean>) {
        if (result.success && result.code == 200) {
            ToastUtil.show("支付密码设置成功")
            //更新缓存状态
            UserManager.setPayPwdStatus(true)
            if (SmApplication.getApp().activityName != null) {
                if (SmApplication.getApp().activityName?.simpleName == BankCardMangerActivity::class.java.simpleName ||
                        SmApplication.getApp().activityName?.simpleName == WithDrawActivity::class.java.simpleName) {
                    if (!SmApplication.getApp().isDelete) {
                        //跳转添加银行卡页面
                        AddBankCardActivity.start(this)
                        return
                    }
                }
                startActivity(Intent(this, SmApplication.getApp().activityName).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                return
            }
            finish()
        } else {
            ToastUtil.show(result.message)
            ToastUtil.show("支付密码设置失败")
            if (SmApplication.getApp().activityName != null) {
                startActivity(Intent(this, SmApplication.getApp().activityName).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                return
            }
            finish()
        }
    }

    override fun modifyPwdData(result: BaseBean<String>) {
        showResult("密码修改成功")
    }

    override fun initView() {
        titleManager.defaultTitle("设置支付密码")
        type = intent.getStringExtra("OPEN_TYPE")
        if (type == "reInput" || type == "modify") {
            pwd = intent.getStringExtra("NEW_PWD")
        } else {
            showDesc.text = "输入支付密码，完成身份验证"
        }

        initListener()

        pwd_s.add(pwd_1)
        pwd_s.add(pwd_2)
        pwd_s.add(pwd_3)
        pwd_s.add(pwd_4)
        pwd_s.add(pwd_5)
        pwd_s.add(pwd_6)
    }

    private fun initListener() {
        button0.setOnClickListener(View.OnClickListener {
            input("0")
        })
        button1.setOnClickListener(View.OnClickListener {
            input("1")
        })
        button2.setOnClickListener(View.OnClickListener {
            input("2")
        })
        button3.setOnClickListener(View.OnClickListener {
            input("3")
        })
        button4.setOnClickListener(View.OnClickListener {
            input("4")
        })
        button5.setOnClickListener(View.OnClickListener {
            input("5")
        })
        button6.setOnClickListener(View.OnClickListener {
            input("6")
        })
        button7.setOnClickListener(View.OnClickListener {
            input("7")
        })
        button8.setOnClickListener(View.OnClickListener {
            input("8")
        })
        button9.setOnClickListener(View.OnClickListener {
            input("9")
        })
        button_del.setOnClickListener(View.OnClickListener {
            if (mPwdCountNum == 0) {
                return@OnClickListener
            } else {
                mPwdCountNum -= 1

                if (mPwdCountNum == 0) {
                    mPassWord = mPassWord.substring(0, 0)
                } else {
                    mPassWord = mPassWord.substring(0, mPassWord.length - 1)
                }

                showPwdImages(mPwdCountNum)
            }
        })
    }

    fun input(value: String) {
        if (mPwdCountNum < 6) {
            mPwdCountNum++
            showPwdImages(mPwdCountNum)
            inputPwd(value)
        }
        if (mPwdCountNum == 6) {
            if (type == "reInput") {
                if (mPassWord == pwd) {
                    mPresenter.requestSavePwdData(pwd, mPassWord)
                } else {
                    ToastUtil.show("密码不一致，请重新输入")
//                    onBackPressed()
                }
            } else if (type == "oldPwd") {
                mPresenter.requestIsPwdRightData(mPassWord)
            } else if (type == "modify") {
                if (mPassWord == pwd) {
                    mPresenter.requestModifyPwdData(pwd, mPassWord)
                } else {
                    ToastUtil.show("密码不一致，请重新输入")
//                    onBackPressed()
                }
            }
            return
        }
    }


    override fun onListener(view: View) {

    }

    /**
     * 通过获取Button上的数字字符来合成字符串
     *
     * @param view
     */
    private fun inputPwd(value: String) {
        mPassWord += value
    }

    /**
     * 根据传入的参数来设置显示的密码的图片张数
     *
     * @param pwdCountNum
     */
    private fun showPwdImages(pwdCountNum: Int) {
        for (i in pwd_s.indices) {
            if (i < pwdCountNum) {
                pwd_s[i].visibility = View.VISIBLE
            } else {
                pwd_s[i].visibility = View.GONE
            }
        }
    }

    override fun reTryGetData() {

    }

    fun showErrorDialog(str: String) {
        val dialog = SmediaDialog(this)
        dialog.singleButton()
        dialog.setTitle(str)
        dialog.show()
    }

    fun showAgainDialog(str: String) {
        val dialog = SmediaDialog(this)
        dialog.setTitle(str)
        dialog.setCancelText("重新输入")
        dialog.setPositiveText("忘记密码")
        dialog.cancelOnClickListener = View.OnClickListener {
            //重新输入
            clearNum()
        }

        dialog.OnClickListener = View.OnClickListener {
            //忘记密码
            //
            CheckPayPwdMessage.start(this@ReInputPayPwdActivity,PayPassworType.FORGOT)
            finish()
        }
        dialog.show()
    }

    fun clearNum() {
        showPwdImages(-1)
        mPwdCountNum = 0
        mPassWord = ""
    }

    fun showResult(str: String) {
        val dialog = SmediaDialog(this)
        dialog.setTitle(str)
        dialog.singleButton()
        dialog.OnClickListener = View.OnClickListener {
            startActivity(Intent(this@ReInputPayPwdActivity, SettingActivity::class.java))
            finish()
        }
        dialog.setOnDismissListener {
            startActivity(Intent(this@ReInputPayPwdActivity, SettingActivity::class.java))
            finish()
        }
        dialog.show()
    }


    companion object {
        fun start(mContext: Context, type: String, pwd: String) {
            mContext.startActivity(Intent(mContext, ReInputPayPwdActivity::class.java).putExtra("OPEN_TYPE", type).putExtra("NEW_PWD", pwd))
        }
    }
}