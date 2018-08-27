package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.PayPassworType
import com.cocosh.shmstore.mine.ui.CheckPayPwdMessage
import com.cocosh.shmstore.mine.ui.SetPayPwdActivity
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.dialog_security_psd.*
import java.util.*

/**
 *
 * Created by lmg on 2018/4/16.
 */
class SercurityDialog<T> : Dialog, View.OnClickListener {
    private lateinit var mContext: Context

    constructor(context: Context?) : this(context, 0) {
        mContext = context!!
    }

    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        mContext = context!!
    }

    /**
     * 0-9,数字按钮
     */

    private val pwd_s = ArrayList<ImageView>()
    private val mTv: TextView? = null
    private var mPwdCountNum: Int = 0
    private var mInputCompleteListener: InputCompleteListener<T>? = null


    interface InputCompleteListener<T> {
        fun inputComplete(pwdLString: String)
        fun result(boolean: Boolean, resultData: T?)
    }

    fun setOnInputCompleteListener(inputCompleteListener: InputCompleteListener<T>) {
        this.mInputCompleteListener = inputCompleteListener
    }


    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //通过window设置获取dialog参数
        val window = window
        window.decorView.setPadding(0, 0, 0, 0)
        val attributes = window.attributes
        attributes.width = RelativeLayout.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window.attributes = attributes
//        window.setBackgroundDrawableResource(R.color.transparent)
        //给dialog设置布局
        setContentView(R.layout.dialog_security_psd)
        initView()
        initListener()
    }

    private fun initListener() {
        button0!!.setOnClickListener(this)
        button1!!.setOnClickListener(this)
        button2!!.setOnClickListener(this)
        button3!!.setOnClickListener(this)
        button4!!.setOnClickListener(this)
        button5!!.setOnClickListener(this)
        button6!!.setOnClickListener(this)
        button7!!.setOnClickListener(this)
        button8!!.setOnClickListener(this)
        button9!!.setOnClickListener(this)
        cancel.setOnClickListener(this)
        button_del!!.setOnClickListener(this)
        forget_psd.setOnClickListener(this)
    }


    private fun initView() {
        //洗牌
        val num = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        shuffleSort(num)
        button0!!.text = num[0].toString()
        button1!!.text = num[1].toString()
        button2!!.text = num[2].toString()
        button3!!.text = num[3].toString()
        button4!!.text = num[4].toString()
        button5!!.text = num[5].toString()
        button6!!.text = num[6].toString()
        button7!!.text = num[7].toString()
        button8!!.text = num[8].toString()
        button9!!.text = num[9].toString()

        pwd_s.add(pwd_1)
        pwd_s.add(pwd_2)
        pwd_s.add(pwd_3)
        pwd_s.add(pwd_4)
        pwd_s.add(pwd_5)
        pwd_s.add(pwd_6)
    }

    //洗牌算法
    private fun shuffleSort(data: IntArray) {
        for (i in 0 until data.size - 1) {
            val j = (data.size * Math.random()).toInt()
            swap(data, i, j)
        }
    }

    private fun swap(data: IntArray, i: Int, j: Int) {
        if (i == j) {
            return
        }
        data[i] = data[i] + data[j]
        data[j] = data[i] - data[j]
        data[i] = data[i] - data[j]
    }

    private var mPassWord = ""

    override fun onClick(view: View) {
        if (view.id == R.id.forget_psd) {
            SmApplication.getApp().activityName = mContext::class.java as Class<BaseActivity>?
            CheckPayPwdMessage.start(mContext, SMSType.FORGOT_PAYPASS)
            this@SercurityDialog.dismiss()
            return
        }

        //删除 如果输入密码个数是0 return ，要不就mPwdCountNum 减1
        if (view.id == R.id.button_del) {
            if (mPwdCountNum == 0) {
                return
            } else {
                mPwdCountNum -= 1

                if (mPwdCountNum == 0) {
                    mPassWord = mPassWord.substring(0, 0)
                } else {
                    mPassWord = mPassWord.substring(0, mPassWord.length - 1)
                }

                showPwdImages(mPwdCountNum)
            }
        } else if (view.id == R.id.cancel) {
            dismiss()
        } else {
            if (mPwdCountNum < 6) {
                mPwdCountNum++
                showPwdImages(mPwdCountNum)
                inputPwd(view)
            }
            /**
             * 当统计的当前输入的密码位数大于等于6，表示输入完成，数据处理在UI线程中处理
             */
            if (mPwdCountNum == 6) {
                //隐藏键盘，显示密码校验过程
                showLoading()
//            requestIsPwdRightData(mPassWord)
                mInputCompleteListener?.inputComplete(mPassWord)
                return
            }
        }
    }

    /**
     * 通过获取Button上的数字字符来合成字符串
     *
     * @param view
     */
    private fun inputPwd(view: View) {
//        if (mPwdCountNum > 1) {
//            mPassWord += ","
//        }

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

    fun showLoading() {
        loadingRl.visibility = View.VISIBLE
        inputLl.visibility = View.INVISIBLE
    }

    fun hideLoading() {
        loadingRl.visibility = View.INVISIBLE
        inputLl.visibility = View.VISIBLE
    }

    fun clearNum() {
        showPwdImages(-1)
        mPwdCountNum = 0
        mPassWord = ""
    }

    fun showErrorDialog(str: String, data: T?) {
        val dialog = SmediaDialog(mContext)
        dialog.singleButton()
        dialog.setTitle(str)
        dialog.setPositiveText("知道了")
        dialog.OnClickListener = View.OnClickListener {
            mInputCompleteListener?.result(false, data)
        }
        dialog.show()
    }

    fun showAgainDialog(str: String) {
        val dialog = SmediaDialog(mContext)
        dialog.setTitle(str)
        dialog.setCancelText("重新输入")
        dialog.setPositiveText("忘记密码")
        dialog.cancelOnClickListener = View.OnClickListener {
            //重新输入
            clearNum()
        }

        dialog.OnClickListener = View.OnClickListener {
            //忘记密码
            SmApplication.getApp().activityName = mContext::class.java as Class<BaseActivity>?
            CheckPayPwdMessage.start(mContext, SMSType.FORGOT_PAYPASS)
            this@SercurityDialog.dismiss()
        }
        dialog.show()
    }


    /**
     * 设置返回数据
     */
    fun getResult(data: BaseBean<T>) {
        hideLoading()
        mInputCompleteListener?.result(true, data.message)
        dismiss()
    }

    /**
     * 校验支付密码
     */
    fun requestIsPwdRightData(pwd: String) {
//        var map = HashMap<String, String>()
//        map["pwd"] = pwd
//        ApiManager.post((mContext as BaseActivity), map, Constant.CHECK_PWD, object : ApiManager.OnResult<BaseModel<String>>() {
//            override fun onSuccess(data: BaseModel<String>) {
//                hideLoading()
//                if (data.success && data.code == 200) {
//                    mInputCompleteListener?.result(true, data.entity)
//                    dismiss()
//                } else if (data.code == 4015) {
//                    showErrorDialog(data.message!!)
////                    mInputCompleteListener?.result()
//                } else if (data.code == 4001) {
//                    showAgainDialog(data.message!!)
////                    mInputCompleteListener?.result()
//                } else {
//                    ToastUtil.show(data.message)
//                    mInputCompleteListener?.result(false)
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//                hideLoading()
//                LogUtil.d(e.message.toString())
//            }
//
//            override fun onCatch(data: BaseModel<String>) {
//                LogUtil.d(data.toString())
//            }
//        })
    }
}