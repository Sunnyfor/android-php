package com.cocosh.shmstore.zxing.camera

import android.content.Context
import android.hardware.Camera
import android.os.AsyncTask
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.SharedUtil
import com.cocosh.shmstore.zxing.common.executor.AsyncTaskExecInterface
import com.cocosh.shmstore.zxing.common.executor.AsyncTaskExecManager
import java.util.*

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class AutoFocusManager(context: Context, private val camera: Camera) : Camera.AutoFocusCallback, AutoFocusActionListener {
    private var active: Boolean = false
    private val useAutoFocus: Boolean
    private var outstandingTask: AutoFocusTask? = null
    private val taskExec: AsyncTaskExecInterface = AsyncTaskExecManager().build()

    init {
        val currentFocusMode = camera.parameters.focusMode
        useAutoFocus = SharedUtil.getBoolean(KEY_AUTO_FOCUS,true) && FOCUS_MODES_CALLING_AF.contains(currentFocusMode)
        LogUtil.i("Current focus mode '$currentFocusMode'; use auto focus? $useAutoFocus")
        start()
    }

    @Synchronized
    override fun onAutoFocus(success: Boolean, theCamera: Camera) {
        if (active) {
            outstandingTask = AutoFocusTask()
            outstandingTask?.let {
                taskExec.execute(it)
            }

        }
    }

    @Synchronized
    fun start() {
        if (useAutoFocus) {
            active = true
            try {
                camera.autoFocus(this)
            } catch (re: RuntimeException) {
                // Have heard RuntimeException reported in Android 4.0.x+; continue?
                LogUtil.w("Unexpected exception while focusing", re)
            }

        }
    }

    @Synchronized
    fun stop() {
        if (useAutoFocus) {
            try {
                camera.cancelAutoFocus()
            } catch (re: RuntimeException) {
                // Have heard RuntimeException reported in Android 4.0.x+; continue?
                LogUtil.w("Unexpected exception while cancelling focusing", re)
            }

        }

        outstandingTask?.cancel(true)
        outstandingTask = null
        active = false
    }


    companion object {
        private const val AUTO_FOCUS_INTERVAL_MS = 2000L
        private val FOCUS_MODES_CALLING_AF: MutableCollection<String>
        const val KEY_AUTO_FOCUS = "preferences_auto_focus"
        init {
            FOCUS_MODES_CALLING_AF = ArrayList(2)
            FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO)
            FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO)
        }
    }


    private class AutoFocusTask : AsyncTask<Any, Any, Any>() {

        var autoFocusActionListener: AutoFocusActionListener? = null

        override fun doInBackground(vararg voids: Any): Any? {
            try {
                Thread.sleep(AUTO_FOCUS_INTERVAL_MS)
            } catch (e: InterruptedException) {
                // continue
            }

            synchronized(this) {
                autoFocusActionListener?.autoFocusResult()
            }
            return null
        }
    }

    override fun autoFocusResult() {
        if (active) {
            start()
        }
    }
}



