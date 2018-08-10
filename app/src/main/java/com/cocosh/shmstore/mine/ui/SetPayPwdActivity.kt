package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.mine.ui.mywallet.BankCardMangerActivity
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.utils.UserManager2
import kotlinx.android.synthetic.main.activity_set_pay_pwd.*
import java.util.*
import java.util.regex.Pattern

/**
 * Created by lmg on 2018/5/4.
 * 设置密码
 */
class SetPayPwdActivity : BaseActivity() {
    private val pwd_s = ArrayList<ImageView>()
    private var mPwdCountNum: Int = 0
    private var mPassWord = ""
    var type: String? = null
    override fun setLayout(): Int = R.layout.activity_set_pay_pwd

    override fun initView() {
        titleManager.defaultTitle("设置支付密码").setLeftOnClickListener(View.OnClickListener {
            if (SmApplication.getApp().activityName != null) {
                startActivity(Intent(this, SmApplication.getApp().activityName).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                return@OnClickListener
            }
        })

        initListener()
        pwd_s.add(pwd_1)
        pwd_s.add(pwd_2)
        pwd_s.add(pwd_3)
        pwd_s.add(pwd_4)
        pwd_s.add(pwd_5)
        pwd_s.add(pwd_6)
        showPhoneNumber.text = ("请为账号 " + UserManager2.getCommonData()?.phone)
        type = intent.getStringExtra("type")
    }

    fun input(view: View) {
        if(mPwdCountNum<6){
            mPwdCountNum++
            showPwdImages(mPwdCountNum)
            inputPwd(view)
        }


        if (mPwdCountNum == 6) {
            /**
             * 跳转再次确认页
             */

            val patterns = Pattern.compile("([0-9])\\1{5}")
            val matcher = patterns.matcher(mPassWord)
            val b = matcher.matches()
            if (b) {
                ToastUtil.show("请不要使用重复的数字")
                return
            }

            var pattern = "0123456789"
            var pattern1 = "9876543210"
            if (pattern.contains(mPassWord) || pattern1.contains(mPassWord)) {
                ToastUtil.show("请不要使用连续的数字")
                return
            }

            if (type == "modify") {
                ReInputPayPwdActivity.start(this, "modify", mPassWord)
                return
            }
            ReInputPayPwdActivity.start(this, "reInput", mPassWord)
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
    private fun inputPwd(view: View) {
        mPassWord += (view as Button).text
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

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, SetPayPwdActivity::class.java))
        }

        fun start(mContext: Context, type: String) {
            mContext.startActivity(Intent(mContext, SetPayPwdActivity::class.java).putExtra("type", type))
        }
    }

    fun clearNum() {
        showPwdImages(-1)
        mPwdCountNum = 0
        mPassWord = ""
    }

    private fun initListener() {
        buttons0.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons1.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons2.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons3.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons4.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons5.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons6.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons7.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons8.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons9.setOnClickListener(View.OnClickListener {
            input(it)
        })
        buttons_del.setOnClickListener(View.OnClickListener {
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

    override fun onBackPressed() {
        if (SmApplication.getApp().activityName != null) {
            startActivity(Intent(this, SmApplication.getApp().activityName).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            return
        }
    }
}