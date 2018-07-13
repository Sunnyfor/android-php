package com.cocosh.shmstore.zxing.decode

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.cocosh.shmstore.R
import com.cocosh.shmstore.zxing.QrCodeActivity
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.io.ByteArrayOutputStream

/**
 *
 * Created by zhangye on 2018/4/26.
 */
internal class DecodeHandler(private val activity: QrCodeActivity, hints: Map<DecodeHintType, Any>) : Handler() {
    private val multiFormatReader: MultiFormatReader = MultiFormatReader()
    private var running = true

    init {
        multiFormatReader.setHints(hints)
    }

    override fun handleMessage(message: Message) {
        if (!running) {
            return
        }
        when (message.what) {
            R.id.decode -> decode(message.obj as ByteArray, message.arg1, message.arg2)
            R.id.quit -> {
                running = false
                Looper.myLooper()!!.quit()
            }
        }
    }

    /**
     * Decode the mData within the viewfinder rectangle, and time how long it
     * took. For efficiency, reuse the same reader objects from one decode to
     * the next.
     *
     * @param mData
     * The YUV preview frame.
     * @param mWidth
     * The mWidth of the preview frame.
     * @param mHeight
     * The mHeight of the preview frame.
     */
    private fun decode(mData: ByteArray, mWidth: Int, mHeight: Int) {
        var data = mData
        var width = mWidth
        var height = mHeight
        val start = System.currentTimeMillis()
        var rawResult: Result? = null

        /* 竖屏修改 */
        val rotatedData = ByteArray(data.size)
        for (y in 0 until height) {
            for (x in 0 until width)
                rotatedData[x * height + height - y - 1] = data[x + y * width]
        }
        val tmp = width
        width = height
        height = tmp
        data = rotatedData

        val source = activity.getCameraManager()?.buildLuminanceSource(data, width, height)
        if (source != null) {
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap)
            } catch (re: ReaderException) {
                // continue
            } finally {
                multiFormatReader.reset()
            }
        }

        val handler = activity.getHandler()
        if (rawResult != null) {
            // Don't log the barcode contents for security.
            val end = System.currentTimeMillis()
            Log.d(TAG, "Found barcode in " + (end - start) + " ms")
            if (handler != null) {
                val message = Message.obtain(handler,
                        R.id.decode_succeeded, rawResult)
                val bundle = Bundle()
                if (source != null) {
                    bundleThumbnail(source, bundle)
                }
                message.data = bundle
                message.sendToTarget()
            }
        } else {
            if (handler != null) {
                val message = Message.obtain(handler, R.id.decode_failed)
                message.sendToTarget()
            }
        }
    }

    companion object {

        private val TAG = DecodeHandler::class.java.simpleName

        private fun bundleThumbnail(source: PlanarYUVLuminanceSource,
                                    bundle: Bundle) {
            val pixels = source.renderThumbnail()
            val width = source.thumbnailWidth
            val height = source.thumbnailHeight
            val bitmap = Bitmap.createBitmap(pixels, 0, width, width, height,
                    Bitmap.Config.ARGB_8888)
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
            bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray())
            bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, width.toFloat() / source.width)
        }
    }

}
