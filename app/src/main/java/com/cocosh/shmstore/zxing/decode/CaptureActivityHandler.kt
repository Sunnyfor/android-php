package com.cocosh.shmstore.zxing.decode

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.provider.Browser
import android.util.Log
import com.cocosh.shmstore.R
import com.cocosh.shmstore.zxing.QrCodeActivity
import com.cocosh.shmstore.zxing.camera.CameraManager
import com.cocosh.shmstore.zxing.view.ViewfinderResultPointCallback
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.Result

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class CaptureActivityHandler(private val activity: QrCodeActivity,
                             decodeFormats: Collection<BarcodeFormat>?,
                             baseHints: Map<DecodeHintType, Any>?,
                             characterSet: String?,
                             private val cameraManager: CameraManager?) : Handler() {
    private val decodeThread: DecodeThread = DecodeThread(activity, decodeFormats, baseHints, characterSet,
            ViewfinderResultPointCallback(activity.getViewfinderView()))
    private var state: State? = null

    private enum class State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    init {
        decodeThread.start()
        state = State.SUCCESS
        cameraManager?.startPreview()
        restartPreviewAndDecode()
    }// Start ourselves capturing previews and decoding.

    override fun handleMessage(message: Message) {
        when (message.what) {
            R.id.restart_preview -> {
                Log.d(TAG, "Got restart preview message")
                restartPreviewAndDecode()
            }
            R.id.decode_succeeded -> {
                Log.d(TAG, "Got decode succeeded message")
                state = State.SUCCESS
                val bundle = message.data
                var barcode: Bitmap? = null
                var scaleFactor = 1.0f
                if (bundle != null) {
                    val compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP)
                    if (compressedBitmap != null) {
                        barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.size, null)
                        // Mutable copy:
                        barcode = barcode!!.copy(Bitmap.Config.ARGB_8888, true)
                    }
                    scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR)
                }
                activity.handleDecode(message.obj as Result, barcode, scaleFactor)
            }
            R.id.decode_failed -> {
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = State.PREVIEW
                decodeThread.getHandler()?.let {
                    cameraManager?.requestPreviewFrame(it, R.id.decode)
                }
            }
            R.id.return_scan_result -> {
                Log.d(TAG, "Got return scan result message")
                activity.setResult(Activity.RESULT_OK, message.obj as Intent)
                activity.finish()
            }
            R.id.launch_product_query -> {
                Log.d(TAG, "Got product query message")
                val url = message.obj as String

                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                intent.data = Uri.parse(url)

                val resolveInfo = activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                var browserPackageName: String? = null
                if (resolveInfo.activityInfo != null) {
                    browserPackageName = resolveInfo.activityInfo.packageName
                    Log.d(TAG, "Using browser in package " + browserPackageName!!)
                }

                // Needed for default Android browser / Chrome only apparently
                if ("com.android.browser" == browserPackageName || "com.android.chrome" == browserPackageName) {
                    intent.`package` = browserPackageName
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackageName)
                }

                try {
                    activity.startActivity(intent)
                } catch (ignored: ActivityNotFoundException) {
                    Log.w(TAG, "Can't find anything to handle VIEW of URI " + url)
                }

            }
        }
    }

    fun quitSynchronously() {
        state = State.DONE
        cameraManager?.stopPreview()
        val quit = Message.obtain(decodeThread.getHandler(), R.id.quit)
        quit.sendToTarget()
        try {
            // Wait at most half a second; should be enough time, and onPause() will timeout quickly
            decodeThread.join(500L)
        } catch (e: InterruptedException) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded)
        removeMessages(R.id.decode_failed)
    }

    private fun restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW
            decodeThread.getHandler()?.let {
                cameraManager?.requestPreviewFrame(it, R.id.decode)
            }
            activity.drawViewfinder()
        }
    }

    companion object {

        private val TAG = CaptureActivityHandler::class.java.simpleName
    }

}