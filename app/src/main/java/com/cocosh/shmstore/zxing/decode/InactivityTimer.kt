package com.cocosh.shmstore.zxing.decode

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.BatteryManager
import android.util.Log
import com.cocosh.shmstore.zxing.camera.AutoFocusActionListener
import com.cocosh.shmstore.zxing.common.executor.AsyncTaskExecInterface
import com.cocosh.shmstore.zxing.common.executor.AsyncTaskExecManager

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class InactivityTimer(private val activity: Activity):AutoFocusActionListener {
    override fun autoFocusResult() {
        activity.finish()
    }

    private val taskExec: AsyncTaskExecInterface = AsyncTaskExecManager().build()
    private val powerStatusReceiver: BroadcastReceiver
    private var inactivityTask: InactivityAsyncTask? = null

    init {
        powerStatusReceiver = PowerStatusReceiver()
        onActivity()
    }

    @Synchronized
    fun onActivity() {
        cancel()
        inactivityTask = InactivityAsyncTask()
        inactivityTask?.let {
            taskExec.execute(it)
        }

    }

    fun onPause() {
        cancel()
        activity.unregisterReceiver(powerStatusReceiver)
    }

    fun onResume() {
        activity.registerReceiver(powerStatusReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        onActivity()
    }

    @Synchronized
    private fun cancel() {
        val task = inactivityTask
        if (task != null) {
            task.cancel(true)
            inactivityTask = null
        }
    }

    fun shutdown() {
        cancel()
    }

    private inner class PowerStatusReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_BATTERY_CHANGED == intent.action) {
                // 0 indicates that we're on battery
                val onBatteryNow = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) <= 0
                if (onBatteryNow) {
                    this@InactivityTimer.onActivity()
                } else {
                    this@InactivityTimer.cancel()
                }
            }
        }
    }


    companion object {

        private val TAG = InactivityTimer::class.java.simpleName

        private const val INACTIVITY_DELAY_MS = 5 * 60 * 1000L
    }


    private class InactivityAsyncTask : AsyncTask<Any, Any, Any>() {

        var autoFocusActionListener:AutoFocusActionListener? = null

        override fun doInBackground(vararg objects: Any): Any? {
            try {
                Thread.sleep(INACTIVITY_DELAY_MS)
                Log.i(TAG, "Finishing activity due to inactivity")
                autoFocusActionListener?.autoFocusResult()
            } catch (e: InterruptedException) {
                // continue without killing
            }

            return null
        }
    }
}