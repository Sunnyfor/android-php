package com.cocosh.shmstore.zxing.camera

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.hardware.Camera
import android.preference.PreferenceManager
import android.view.WindowManager
import com.cocosh.shmstore.utils.LogUtil
import java.util.*

/**
 *
 * Created by zhangye on 2018/4/26.
 */
internal class CameraConfigurationManager(private val context: Context) {
    var screenResolution: Point? = null
        private set
    var cameraResolution: Point? = null
        private set

    /**
     * Reads, one time, values from the camera that are needed by the app.
     */
    fun initFromCameraParameters(camera: Camera) {
        val parameters = camera.parameters
        val manager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val width = display.width
        val height = display.height
        // We're landscape-only, and have apparently seen issues with display
        // thinking it's portrait
        // when waking from sleep. If it's not landscape, assume it's mistaken
        // and reverse them:

        /* 竖屏修改 */
        // if (width < height) {
        // Log.i(TAG,
        // "Display reports portrait orientation; assuming this is incorrect");
        // int temp = width;
        // width = height;
        // height = temp;
        // }

        screenResolution = Point(width, height)
        LogUtil.i("Screen resolution: " + screenResolution!!)

        /* 竖屏修改 ，图形拉伸处理*/
        val screenResolutionForCamera = Point()
        screenResolutionForCamera.x = screenResolution!!.x
        screenResolutionForCamera.y = screenResolution!!.y
        if (screenResolution!!.x < screenResolution!!.y) {
            screenResolutionForCamera.x = screenResolution!!.y
            screenResolutionForCamera.y = screenResolution!!.x
        }

        cameraResolution = findBestPreviewSizeValue(parameters,
                screenResolutionForCamera)

        LogUtil.i("Camera resolution: " + cameraResolution!!)
    }

    fun setDesiredCameraParameters(camera: Camera, safeMode: Boolean) {
        val parameters = camera.parameters

        if (parameters == null) {
            LogUtil.w(
                    "Device error: no camera parameters are available. Proceeding without configuration.")
            return
        }

        LogUtil.i( "Initial camera parameters: " + parameters.flatten())

        if (safeMode) {
            LogUtil.w(
                    "In camera config safe mode -- most settings will not be honored")
        }

        val prefs = PreferenceManager
                .getDefaultSharedPreferences(context)

        initializeTorch(parameters, prefs, safeMode)

        var focusMode: String? = null

        /* 配置修改 */
        focusMode = findSettableValue(parameters.supportedFocusModes,
                Camera.Parameters.FOCUS_MODE_AUTO)
        // if (prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true)) {
        // if (safeMode ||
        // prefs.getBoolean(PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS,
        // false)) {
        // focusMode = findSettableValue(parameters.getSupportedFocusModes(),
        // Camera.Parameters.FOCUS_MODE_AUTO);
        // } else {
        // focusMode = findSettableValue(parameters.getSupportedFocusModes(),
        // "continuous-picture", //
        // Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE in 4.0+
        // "continuous-video", // Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
        // in 4.0+
        // Camera.Parameters.FOCUS_MODE_AUTO);
        // }
        // }

        // Maybe selected auto-focus but not available, so fall through here:
        if (!safeMode && focusMode == null) {
            focusMode = findSettableValue(parameters.supportedFocusModes,
                    Camera.Parameters.FOCUS_MODE_MACRO, "edof") // Camera.Parameters.FOCUS_MODE_EDOF
            // in 2.2+
        }
        if (focusMode != null) {
            parameters.focusMode = focusMode
        }

        /* 配置修改 */
        // 反向扫描
        // if (prefs.getBoolean(PreferencesActivity.KEY_INVERT_SCAN, false)) {
        // String colorMode =
        // findSettableValue(parameters.getSupportedColorEffects(),
        // Camera.Parameters.EFFECT_NEGATIVE);
        // if (colorMode != null) {
        // parameters.setColorEffect(colorMode);
        // }
        // }

        parameters.setPreviewSize(cameraResolution!!.x, cameraResolution!!.y)

        /* 竖屏修改 */
        camera.setDisplayOrientation(90)

        camera.parameters = parameters
    }

    fun getTorchState(camera: Camera?): Boolean {
        if (camera != null) {
            val parameters = camera.parameters
            if (parameters != null) {
                val flashMode = camera.parameters.flashMode
                return flashMode != null && (Camera.Parameters.FLASH_MODE_ON == flashMode || Camera.Parameters.FLASH_MODE_TORCH == flashMode)
            }
        }
        return false
    }

    fun setTorch(camera: Camera, newSetting: Boolean) {
        val parameters = camera.parameters
        doSetTorch(parameters, newSetting, false)
        camera.parameters = parameters
    }

    private fun initializeTorch(parameters: Camera.Parameters,
                                prefs: SharedPreferences, safeMode: Boolean) {
        val currentSetting = FrontLightMode.readPref(prefs) === FrontLightMode.ON
        doSetTorch(parameters, currentSetting, safeMode)
    }

    private fun doSetTorch(parameters: Camera.Parameters, newSetting: Boolean,
                           safeMode: Boolean) {
        val flashMode: String? = if (newSetting) {
            findSettableValue(parameters.supportedFlashModes,
                    Camera.Parameters.FLASH_MODE_TORCH,
                    Camera.Parameters.FLASH_MODE_ON)
        } else {
            findSettableValue(parameters.supportedFlashModes,
                    Camera.Parameters.FLASH_MODE_OFF)
        }
        if (flashMode != null) {
            parameters.flashMode = flashMode
        }

        /*
		 * SharedPreferences prefs =
		 * PreferenceManager.getDefaultSharedPreferences(activity); if
		 * (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_EXPOSURE, false))
		 * { if (!safeMode) { ExposureInterface exposure = new
		 * ExposureManager().build(); exposure.setExposure(parameters,
		 * newSetting); } }
		 */
    }

    private fun findBestPreviewSizeValue(parameters: Camera.Parameters,
                                         screenResolution: Point): Point {

        val rawSupportedSizes = parameters
                .supportedPreviewSizes
        if (rawSupportedSizes == null) {
            LogUtil.w(
                    "Device returned no supported preview sizes; using default")
            val defaultSize = parameters.previewSize
            return Point(defaultSize.width, defaultSize.height)
        }

        // Sort by size, descending
        val supportedPreviewSizes = ArrayList(
                rawSupportedSizes)
        Collections.sort<Camera.Size>(supportedPreviewSizes, Comparator<Camera.Size> { a, b ->
            val aPixels = a.height * a.width
            val bPixels = b.height * b.width
            if (bPixels < aPixels) {
                return@Comparator -1
            }
            if (bPixels > aPixels) {
                1
            } else 0
        })

        var bestSize: Point? = null
        val screenAspectRatio = screenResolution.x.toFloat() / screenResolution.y.toFloat()

        var diff = java.lang.Float.POSITIVE_INFINITY
        for (supportedPreviewSize in supportedPreviewSizes) {
            val realWidth = supportedPreviewSize.width
            val realHeight = supportedPreviewSize.height
            val pixels = realWidth * realHeight
            if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
                continue
            }
            val isCandidatePortrait = realWidth < realHeight
            val maybeFlippedWidth = if (isCandidatePortrait)
                realHeight
            else
                realWidth
            val maybeFlippedHeight = if (isCandidatePortrait)
                realWidth
            else
                realHeight
            if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
                val exactPoint = Point(realWidth, realHeight)
                LogUtil.i("Found preview size exactly matching screen size: " + exactPoint)
                return exactPoint
            }
            val aspectRatio = maybeFlippedWidth.toFloat() / maybeFlippedHeight.toFloat()
            val newDiff = Math.abs(aspectRatio - screenAspectRatio)
            if (newDiff < diff) {
                bestSize = Point(realWidth, realHeight)
                diff = newDiff
            }
        }

        if (bestSize == null) {
            val defaultSize = parameters.previewSize
            bestSize = Point(defaultSize.width, defaultSize.height)
            LogUtil.i("No suitable preview sizes, using default: " + bestSize)
        }

        LogUtil.i("Found best approximate preview size: " + bestSize)
        return bestSize
    }

    companion object {
        private const val MIN_PREVIEW_PIXELS = 470 * 320 // normal screen
        private const val MAX_PREVIEW_PIXELS = 1280 * 800

        private fun findSettableValue(supportedValues: Collection<String>?,
                                      vararg desiredValues: String): String? {
            LogUtil.i("Supported values: " + supportedValues)
            var result: String? = null
            if (supportedValues != null) {
                for (desiredValue in desiredValues) {
                    if (supportedValues.contains(desiredValue)) {
                        result = desiredValue
                        break
                    }
                }
            }
            LogUtil.i("Settable value: " + result!!)
            return result
        }
    }

}