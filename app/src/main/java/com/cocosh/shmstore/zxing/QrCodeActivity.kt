package com.cocosh.shmstore.zxing

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.os.Vibrator
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.cocosh.shmstore.zxing.camera.CameraManager
import com.cocosh.shmstore.zxing.control.AmbientLightManager
import com.cocosh.shmstore.zxing.control.BeepManager
import com.cocosh.shmstore.zxing.decode.CaptureActivityHandler
import com.cocosh.shmstore.zxing.decode.FinishListener
import com.cocosh.shmstore.zxing.decode.InactivityTimer
import com.cocosh.shmstore.zxing.view.ViewfinderView
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.Result
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class QrCodeActivity : BaseActivity(), SurfaceHolder.Callback {

    private var btnLamp: LinearLayout? = null
    private var btnHistory: LinearLayout? = null
    private var imageView: ImageView? = null
    private var isTorchOn = false
    private var cameraManager: CameraManager? = null
    private var mHandler: CaptureActivityHandler? = null
    private var savedResultToShow: Result? = null
    private var viewfinderView: ViewfinderView? = null
    private var hasSurface: Boolean = false
    private var decodeFormats: Collection<BarcodeFormat>? = null
    private val decodeHints: Map<DecodeHintType, Any>? = null
    private var characterSet: String? = null
    private var inactivityTimer: InactivityTimer? = null

    private var beepManager: BeepManager? = null
    private var ambientLightManager: AmbientLightManager? = null
    private var vibrate: Boolean = false// 震动

    private var setBtn: Button? = null


    override fun setLayout(): Int = R.layout.activity_qrcode

    override fun initView() {
        titleManager.defaultTitle("扫一扫")
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {

    }


    fun getViewfinderView(): ViewfinderView? = viewfinderView

    fun getHandler(): Handler? = mHandler

    fun getCameraManager(): CameraManager? = cameraManager


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        hasSurface = false
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder)
        }
    }


    /** 结果处理  */
    fun handleDecode(rawResult: Result, barcode: Bitmap?, scaleFactor: Float) {
        inactivityTimer?.onActivity()
        beepManager?.playBeepSoundAndVibrate()

        var msg: String? = rawResult.text
        if (msg == null || "" == msg) {
            msg = "无法识别"
        }
        playBeepSoundAndVibrate()// 扫描后震动提示
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())// 设置日期格式
        val time = simpleDateFormat.format(Date())
        LogUtil.i("识别结果：$msg")
        val dialog = SmediaDialog(this)
        dialog.setTitle(msg)
        dialog.setDesc(time)
        dialog.show()
        dialog.setOnDismissListener {
            onResume() //重置相机扫描
        }

    }

    /**
     * 扫描后震动提示
     */
    private val VIBRATE_DURATION: Long = 50

    private fun playBeepSoundAndVibrate() {

        if (vibrate) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VIBRATE_DURATION)
        }
    }

    private fun initCamera(surfaceHolder: SurfaceHolder?) {
        if (surfaceHolder == null) {
            return
        }

        cameraManager?.let {
            if (it.isOpen) {
                return
            }
            try {
                it.openDriver(surfaceHolder)
                if (mHandler == null) {
                    mHandler = CaptureActivityHandler(this, decodeFormats,
                            decodeHints, characterSet, cameraManager)
                }
                decodeOrStoreSavedBitmap(null, null)
            } catch (ioe: IOException) {
                displayFrameworkBugMessageAndExit()
            } catch (e: RuntimeException) {
                displayFrameworkBugMessageAndExit()
            }
        }

    }

    private fun displayFrameworkBugMessageAndExit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("警告")
        builder.setMessage("抱歉，相机出现问题，您可能需要重启设备")
        builder.setPositiveButton("确定", FinishListener(this))
        builder.setOnCancelListener(FinishListener(this))
        builder.show()
    }

    fun restartPreviewAfterDelay(delayMS: Long) {
        if (mHandler != null) {
            mHandler?.sendEmptyMessageDelayed(R.id.restart_preview, delayMS)
        }
        resetStatusView()
    }

    private fun resetStatusView() {
        viewfinderView?.visibility = View.VISIBLE
    }

    fun drawViewfinder() {
        viewfinderView?.drawViewfinder()
    }

    override fun onResume() {
        super.onResume()

        cameraManager = CameraManager(application)

        viewfinderView = findViewById(R.id.viewfinder_view) as ViewfinderView
        cameraManager?.let {
            viewfinderView?.setCameraManager(it)
        }

        mHandler = null
        resetStatusView()

        val surfaceView = findViewById(R.id.preview_view) as SurfaceView
        val surfaceHolder = surfaceView.holder
        if (hasSurface) {
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(this)
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }

        beepManager?.updatePrefs()
        cameraManager?.let {
            ambientLightManager?.start(it)
        }

        inactivityTimer?.onResume()
        vibrate = true
        decodeFormats = null
        characterSet = null

    }

    private fun playVibrate() {

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VIBRATE_DURATION)

    }

    override fun onPause() {

        mHandler?.let {
            it.quitSynchronously()
            mHandler = null
        }

        inactivityTimer?.onPause()
        ambientLightManager?.stop()
        cameraManager?.closeDriver()
        if (!hasSurface) {
            val surfaceView = findViewById(R.id.preview_view) as SurfaceView
            val surfaceHolder = surfaceView.holder
            surfaceHolder.removeCallback(this)
        }
        super.onPause()
    }

    override fun onDestroy() {
        inactivityTimer?.shutdown()
        viewfinderView?.recycleLineDrawable()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_CAMERA// 拦截相机键
            -> return true
            KeyEvent.KEYCODE_BACK -> {
                finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun decodeOrStoreSavedBitmap(bitmap: Bitmap?, result: Result?) {
        if (mHandler == null) {
            savedResultToShow = result
        } else {
            if (result != null) {
                savedResultToShow = result
            }
            if (savedResultToShow != null) {
                val message = Message.obtain(mHandler,
                        R.id.decode_succeeded, savedResultToShow)
                mHandler?.sendMessage(message)
            }
            savedResultToShow = null
        }
    }
}