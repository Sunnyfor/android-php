package com.cocosh.shmstore.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.cocosh.shmstore.R
import com.cocosh.shmstore.utils.TitleManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.layout_error.*


/**
 * 父类
 * Created by 张野 on 2017/10/12.
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener, IBaseView {
    var isShowLoading = true
    val composites = CompositeDisposable()
    lateinit var titleManager: TitleManager
    private var viewDataBinding: ViewDataBinding? = null
    private lateinit var noDoubleClickListener: NoDoubleClickListener
    private var tokenView: View? = null

    /**
     * 设置布局
     *
     * @return 资源ID
     */
    abstract fun setLayout(): Int

    /**
     * 初始化View组件
     */
    abstract fun initView()

    abstract fun onListener(view: View)

    //重试
    abstract fun reTryGetData()


    override fun onClick(view: View) {
        if (view.id == btnTryAgain.id) {
            reTryGetData()
        }
        noDoubleClickListener.onClick(view)
    }

    /**
     * 以下为生命周期
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //强制屏幕
        onBeforeLayout()
        setContentView(R.layout.activity_base)
        titleManager = TitleManager(this)
        viewDataBinding = DataBindingUtil.inflate(layoutInflater, setLayout(), frameBody, false)
        if (viewDataBinding != null) {
            frameBody.addView(viewDataBinding?.root)
        } else {
            val bodyView = LayoutInflater.from(this).inflate(setLayout(), null, false)
            frameBody.addView(bodyView)
        }

        noDoubleClickListener = NoDoubleClickListener(this)

        initView()
        btnTryAgain.setOnClickListener(this)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewDataBinding> getDataBinding(): T = viewDataBinding as T


//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onResume() {
//        super.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//    }


    override fun onDestroy() {
        super.onDestroy()
        composites.dispose()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.windowToken)
            }
        }
        return try {
            super.dispatchTouchEvent(ev)
        }catch (e:Exception){
            false
        }
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }


    /**
     * 隐藏输入法键盘
     */
    fun hideKeyboard(token: IBinder) {
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(token, 0)

    }


    /**
     * 显示输入法键盘
     */
    fun showKeyboard(view: View?) {
        view?.let {
            tokenView = it
            it.post({
                val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.showSoftInput(it, InputMethodManager.HIDE_NOT_ALWAYS)
            })
        }
    }


    open fun onBeforeLayout() {

    }


    override fun showError(type: Int) {

    }

    override fun showLoading() {
        if (isShowLoading) {
            frameLoading.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
//        if (isShowLoading) {
            frameLoading.visibility = View.GONE
//        }
    }

    override fun showReTryLayout() {
        frameError.visibility = View.VISIBLE
    }

    override fun hideReTryLayout() {
        frameError.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (frameLoading.visibility != View.VISIBLE) {
            super.onBackPressed()
        }
    }
}