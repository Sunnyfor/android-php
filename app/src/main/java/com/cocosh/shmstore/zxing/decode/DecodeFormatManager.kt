package com.cocosh.shmstore.zxing.decode

import android.content.Intent
import android.net.Uri
import com.google.zxing.BarcodeFormat
import java.util.*
import java.util.regex.Pattern

/**
 *
 * Created by zhangye on 2018/4/26.
 */
internal object DecodeFormatManager {

    private val COMMA_PATTERN = Pattern.compile(",")

    private val PRODUCT_FORMATS: Collection<BarcodeFormat>
    val ONE_D_FORMATS: MutableCollection<BarcodeFormat>
    val QR_CODE_FORMATS: Collection<BarcodeFormat> = EnumSet.of(BarcodeFormat.QR_CODE)
    val DATA_MATRIX_FORMATS: Collection<BarcodeFormat> = EnumSet.of(BarcodeFormat.DATA_MATRIX)

    init {
        PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.EAN_13,
                BarcodeFormat.EAN_8,
                BarcodeFormat.RSS_14,
                BarcodeFormat.RSS_EXPANDED)
        ONE_D_FORMATS = EnumSet.of(BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_93,
                BarcodeFormat.CODE_128,
                BarcodeFormat.ITF,
                BarcodeFormat.CODABAR)
        ONE_D_FORMATS.addAll(PRODUCT_FORMATS)
    }

    fun parseDecodeFormats(intent: Intent): Collection<BarcodeFormat>? {
        var scanFormats: List<String>? = null
        val scanFormatsString = intent.getStringExtra(Intents.Scan.FORMATS)
        if (scanFormatsString != null) {
            scanFormats = Arrays.asList(*COMMA_PATTERN.split(scanFormatsString))
        }
        return parseDecodeFormats(scanFormats, intent.getStringExtra(Intents.Scan.MODE))
    }

    fun parseDecodeFormats(inputUri: Uri): Collection<BarcodeFormat>? {
        var formats: List<String>? = inputUri.getQueryParameters(Intents.Scan.FORMATS)
        if (formats != null && formats.size == 1 && formats[0] != null) {
            formats = Arrays.asList(*COMMA_PATTERN.split(formats[0]))
        }
        return parseDecodeFormats(formats, inputUri.getQueryParameter(Intents.Scan.MODE))
    }

    private fun parseDecodeFormats(scanFormats: Iterable<String>?,
                                   decodeMode: String?): Collection<BarcodeFormat>? {
        if (scanFormats != null) {
            val formats = EnumSet.noneOf(BarcodeFormat::class.java)
            try {
                for (format in scanFormats) {
                    formats.add(BarcodeFormat.valueOf(format))
                }
                return formats
            } catch (iae: IllegalArgumentException) {
                // ignore it then
            }

        }
        if (decodeMode != null) {
            if (Intents.Scan.PRODUCT_MODE == decodeMode) {
                return PRODUCT_FORMATS
            }
            if (Intents.Scan.QR_CODE_MODE == decodeMode) {
                return QR_CODE_FORMATS
            }
            if (Intents.Scan.DATA_MATRIX_MODE == decodeMode) {
                return DATA_MATRIX_FORMATS
            }
            if (Intents.Scan.ONE_D_MODE == decodeMode) {
                return ONE_D_FORMATS
            }
        }
        return null
    }

}
