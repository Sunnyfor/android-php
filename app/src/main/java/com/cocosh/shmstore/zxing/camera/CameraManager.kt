package com.cocosh.shmstore.zxing.camera

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.hardware.Camera
import android.os.Handler
import android.view.SurfaceHolder
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.zxing.camera.open.OpenCameraManager
import com.google.zxing.PlanarYUVLuminanceSource
import java.io.IOException

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class CameraManager(private val context: Context) {
    private val configManager: CameraConfigurationManager = CameraConfigurationManager(context)
    private var camera: Camera? = null
    private var autoFocusManager: AutoFocusManager? = null
    private var framingRect: Rect? = null
    private var framingRectInPreview: Rect? = null
    private var initialized: Boolean = false
    private var previewing: Boolean = false
    private var requestedFramingRectWidth: Int = 0
    private var requestedFramingRectHeight: Int = 0
    /**
     * Preview frames are delivered here, which we pass on to the registered
     * handler. Make sure to clear the handler so it will only receive one
     * message.
     */
    private val previewCallback: PreviewCallback

    val isOpen: Boolean
        @Synchronized get() = camera != null

    init {
        previewCallback = PreviewCallback(configManager)
    }

    /**
     * Opens the camera driver and initializes the hardware parameters.
     *
     * @param holder
     * The surface object which the camera will draw preview frames
     * into.
     * @throws IOException
     * Indicates the camera driver failed to open.
     */
    @Synchronized
    @Throws(IOException::class)
    fun openDriver(holder: SurfaceHolder) {
        var theCamera = camera
        if (theCamera == null) {
            theCamera = OpenCameraManager().build().open()
            camera = theCamera
        }
        theCamera.setPreviewDisplay(holder)

        if (!initialized) {
            initialized = true
            configManager.initFromCameraParameters(theCamera)
            if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
                setManualFramingRect(requestedFramingRectWidth,
                        requestedFramingRectHeight)
                requestedFramingRectWidth = 0
                requestedFramingRectHeight = 0
            }
        }

        var parameters: Camera.Parameters? = theCamera.parameters
        val parametersFlattened = if (parameters == null)
            null
        else
            parameters
                    .flatten() // Save these, temporarily
        try {
            configManager.setDesiredCameraParameters(theCamera, false)
        } catch (re: RuntimeException) {
            // Driver failed
            LogUtil.w(
                    "Camera rejected parameters. Setting only minimal safe-mode parameters")
            LogUtil.i( "Resetting to saved camera params: " + parametersFlattened!!)
            // Reset:
            if (parametersFlattened != null) {
                parameters = theCamera.parameters
                parameters!!.unflatten(parametersFlattened)
                try {
                    theCamera.parameters = parameters
                    configManager.setDesiredCameraParameters(theCamera, true)
                } catch (re2: RuntimeException) {
                    // Well, darn. Give up
                    LogUtil.w(
                            "Camera rejected even safe-mode parameters! No configuration")
                }

            }
        }

    }

    /**
     * Closes the camera driver if still in use.
     */
    @Synchronized
    fun closeDriver() {
        if (camera != null) {
            camera!!.release()
            camera = null
            // Make sure to clear these each time we close the camera, so that
            // any scanning rect
            // requested by intent is forgotten.
            framingRect = null
            framingRectInPreview = null
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    @Synchronized
    fun startPreview() {
        val theCamera = camera
        if (theCamera != null && !previewing) {
            theCamera.startPreview()
            previewing = true
            autoFocusManager = AutoFocusManager(context, camera!!)
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    @Synchronized
    fun stopPreview() {
        if (autoFocusManager != null) {
            autoFocusManager!!.stop()
            autoFocusManager = null
        }
        if (camera != null && previewing) {
            camera!!.stopPreview()
            previewCallback.setHandler(null, 0)
            previewing = false
        }
    }

    /**
     * Convenience method for
     */
    @Synchronized
    fun setTorch(newSetting: Boolean) {
        if (newSetting != configManager.getTorchState(camera)) {
            if (camera != null) {
                if (autoFocusManager != null) {
                    autoFocusManager!!.stop()
                }
                configManager.setTorch(camera!!, newSetting)
                if (autoFocusManager != null) {
                    autoFocusManager!!.start()
                }
            }
        }
    }

    /**
     * A single preview frame will be returned to the handler supplied. The data
     * will arrive as byte[] in the message.obj field, with width and height
     * encoded as message.arg1 and message.arg2, respectively.
     *
     * @param handler
     * The handler to send the message to.
     * @param message
     * The what field of the message to be sent.
     */
    @Synchronized
    fun requestPreviewFrame(handler: Handler, message: Int) {
        val theCamera = camera
        if (theCamera != null && previewing) {
            previewCallback.setHandler(handler, message)
            theCamera.setOneShotPreviewCallback(previewCallback)
        }
    }

    /**
     * Calculates the framing rect which the UI should draw to show the user
     * where to place the barcode. This target helps with alignment as well as
     * forces the user to hold the device far enough away to ensure the image
     * will be in focus.
     *
     * @return The rectangle to draw on screen in window coordinates.
     */
    @Synchronized
    fun getFramingRect(): Rect? {
        if (framingRect == null) {
            if (camera == null) {
                return null
            }
            val screenResolution = configManager.screenResolution
                    ?: // Called early, before init even finished
                    return null

            // int width = findDesiredDimensionInRange(screenResolution.x,
            // MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
            // int height = findDesiredDimensionInRange(screenResolution.y,
            // MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

            /* 扫描框修改 */
            val metrics = context.resources.displayMetrics
            val width = (metrics.widthPixels * 0.6).toInt()
            val height = (width * 0.9).toInt()

            val leftOffset = (screenResolution.x - width) / 2
            val topOffset = (screenResolution.y - height) / 4
            //			framingRect = new Rect(leftOffset, topOffset, leftOffset + width,
            //					topOffset + height);
            framingRect = Rect(leftOffset - 100, topOffset + 100, leftOffset + width + 100,
                    topOffset + height + 300)
            LogUtil.d( "Calculated framing rect: " + framingRect!!)
        }
        return framingRect
    }

    // private static int findDesiredDimensionInRange(int resolution, int
    // hardMin,
    // int hardMax) {
    // int dim = resolution / 2; // Target 50% of each dimension
    // if (dim < hardMin) {
    // return hardMin;
    // }
    // if (dim > hardMax) {
    // return hardMax;
    // }
    // return dim;
    // }

    /**
     * Like [.getFramingRect] but coordinates are in terms of the preview
     * frame, not UI / screen.
     */
    @Synchronized
    fun getFramingRectInPreview(): Rect? {
        if (framingRectInPreview == null) {
            val framingRect = getFramingRect() ?: return null
            val rect = Rect(framingRect)
            val cameraResolution:Point? = configManager.cameraResolution
            val screenResolution:Point? = configManager.screenResolution
            if (cameraResolution == null || screenResolution == null) {
                // Called early, before init even finished
                return null
            }

            /* 竖屏修改 */
            rect.left = rect.left * cameraResolution.y / screenResolution.x
            rect.right = rect.right * cameraResolution.y / screenResolution.x
            rect.top = rect.top * cameraResolution.x / screenResolution.y
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y

            // rect.left = rect.left * cameraResolution.x / screenResolution.x;
            // rect.right = rect.right * cameraResolution.x /
            // screenResolution.x;
            // rect.top = rect.top * cameraResolution.y / screenResolution.y;
            // rect.bottom = rect.bottom * cameraResolution.y /
            // screenResolution.y;

            framingRectInPreview = rect
        }
        return framingRectInPreview
    }

    /**
     * Allows third party apps to specify the scanning rectangle dimensions,
     * rather than determine them automatically based on screen resolution.
     *
     * @param mWidth
     * The mWidth in pixels to scan.
     * @param mHeight
     * The mHeight in pixels to scan.
     */
    @Synchronized
    private fun setManualFramingRect(mWidth: Int, mHeight: Int) {
        var width = mWidth
        var height = mHeight
        if (initialized) {
            val screenResolution: Point? = configManager.screenResolution

            screenResolution?.let {
                if (width > it.x) {
                    width = it.x
                }
                if (height > it.y) {
                    height = it.y
                }
                val leftOffset = (it.x - width) / 2
                val topOffset = (it.y - height) / 2
                framingRect = Rect(leftOffset, topOffset, leftOffset + width,
                        topOffset + height)
                LogUtil.d("Calculated manual framing rect: " + framingRect!!)
                framingRectInPreview = null
            }
        } else {
            requestedFramingRectWidth = width
            requestedFramingRectHeight = height
        }
    }

    /**
     * A factory method to build the appropriate LuminanceSource object based on
     * the format of the preview buffers, as described by Camera.Parameters.
     *
     * @param data
     * A preview frame.
     * @param width
     * The width of the image.
     * @param height
     * The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    fun buildLuminanceSource(data: ByteArray,
                             width: Int, height: Int): PlanarYUVLuminanceSource? {
        val rect = getFramingRectInPreview() ?: return null
// Go ahead and assume it's YUV rather than die.
        return PlanarYUVLuminanceSource(data, width, height, rect.left,
                rect.top, rect.width(), rect.height(), false)
    }

}