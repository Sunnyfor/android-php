package com.cocosh.shmstore.zxing.decode

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.cocosh.shmstore.zxing.QrCodeActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.ResultPointCallback
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 *
 * Created by zhangye on 2018/4/26.
 */
internal class DecodeThread(private val activity: QrCodeActivity,
                            decodeFormats: Collection<BarcodeFormat>?,
                            baseHints: Map<DecodeHintType, Any>?, characterSet: String?,
                            resultPointCallback: ResultPointCallback) : Thread() {
    private val hints: MutableMap<DecodeHintType, Any>
    private var handler: Handler? = null
    private val handlerInitLatch: CountDownLatch

    init {
        var mDecodeFormats = decodeFormats
        handlerInitLatch = CountDownLatch(1)

        hints = EnumMap(DecodeHintType::class.java)
        baseHints?.let {
            hints.putAll(it)
        }

        // The prefs can't change while the thread is running, so pick them up
        // once here.
        if (mDecodeFormats == null || mDecodeFormats.isEmpty()) {
            // SharedPreferences prefs = PreferenceManager
            // .getDefaultSharedPreferences(activity);
            mDecodeFormats = EnumSet.noneOf(BarcodeFormat::class.java)

            /* 配置修改 */
            // if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_1D, false)) {
            // decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            // }
            // if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_QR, false)) {
            // decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            // }
            // if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_DATA_MATRIX,
            // false)) {
            // decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
            // }

            mDecodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS)//支持一维条码
            mDecodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS)
            mDecodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS)
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = mDecodeFormats

        if (characterSet != null) {
            hints[DecodeHintType.CHARACTER_SET] = characterSet
        }
        hints[DecodeHintType.NEED_RESULT_POINT_CALLBACK] = resultPointCallback
        Log.i("DecodeThread", "Hints: " + hints)
    }

    fun getHandler(): Handler? {
        try {
            handlerInitLatch.await()
        } catch (ie: InterruptedException) {
            // continue?
        }

        return handler
    }

    override fun run() {
        Looper.prepare()
        //handler = new DecodeHandler(activity, hints);
        handler = DecodeHandler(activity, hints)
        handlerInitLatch.countDown()
        Looper.loop()
    }

    companion object {

        val BARCODE_BITMAP = "barcode_bitmap"
        val BARCODE_SCALED_FACTOR = "barcode_scaled_factor"
    }

}