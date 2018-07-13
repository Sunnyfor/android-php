package com.cocosh.shmstore.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import com.cocosh.shmstore.base.BaseActivity
import java.io.File


/**
 * 相机相册工具类
 * Created by zhangye on 2018/4/10.
 */
class CameraPhotoUtils(var activity: BaseActivity) {
    var onResultListener: OnResultListener? = null
    private var messageMap = hashMapOf<String, String>()

    private var file: File? = null
    private var uri: Uri? = null
    private var type = IntentCode.IS_CAMERA
    private var missPermission = ""
    private var aspectX = 1
    private var aspectY = 1


    init {
        messageMap[Manifest.permission.CAMERA] = "相机权限"
        messageMap[Manifest.permission.READ_EXTERNAL_STORAGE] = "存储卡读取权限"
        messageMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = "存储卡写入权限"
    }

    fun startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (checkPermission(permissions)) {
                //有权限
                intentCamera()
            } else {
                //没有权限
                ActivityCompat.requestPermissions(activity, permissions, PermissionCode.CAMERA.type)
            }
        } else {
            intentCamera()
        }
    }


    fun startPoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            if (checkPermission(permissions)) {
                //有权限
                intentPhoto()
            } else {
                //没有权限
                ActivityCompat.requestPermissions(activity, permissions, PermissionCode.STORAGE.type)
            }
        } else {
            intentPhoto()
        }
    }


    //跳转相机
    private fun intentCamera() {
        type = IntentCode.IS_CAMERA
        initFile()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)// 更改系统默认存储路径
        activity.startActivityForResult(intent, IntentCode.IS_CAMERA)
    }


    //跳转相册
    private fun intentPhoto() {
        type = IntentCode.IS_PHOTO
        initFile()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        activity.startActivityForResult(intent, IntentCode.IS_PHOTO)
    }


    /**
     * 结果回调需要在Activity回调中调用
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IntentCode.IS_CAMERA -> {
                    uri?.let {
                        startPhotoZoom(it)
                    }
                }
                IntentCode.IS_PHOTO -> {
                    data?.let {
                        startPhotoZoom(it.data)
                    }
                }

                IntentCode.CROP_PICTURE -> {
                    file?.let {
                        onResultListener?.onResult(it)
                    }
                }
            }
        }
    }

    /**
     * 权限回调，需要在Activity权限回调中调用
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionCode.CAMERA.type -> {
                if (checkPermission(permissions)) {
                    intentCamera()
                } else {
                    ToastUtil.show("没有${messageMap[missPermission]}权限")
                }
            }

            PermissionCode.STORAGE.type -> {
                if (checkPermission(permissions)) {
                    if (type == IntentCode.IS_CAMERA) {
                        intentCamera()
                    } else {
                        intentPhoto()
                    }

                } else {
                    ToastUtil.show("没有${messageMap[missPermission]}权限")
                }
            }

        }
    }

    //裁剪图片
    private fun startPhotoZoom(mUri: Uri) {
        val intent = Intent("com.android.camera.action.CROP")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        intent.setDataAndType(mUri, "image/*")
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true")
        intent.putExtra("scale", true)

        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
        if (aspectX != 0 && aspectY != 0){
            intent.putExtra("outputX", aspectX)
            intent.putExtra("outputY", aspectY)
        }
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true) // no face detection
        activity.startActivityForResult(intent, IntentCode.CROP_PICTURE)
    }


    fun setAspectXY(aspectX: Int, aspectY: Int) {
        this.aspectX = aspectX
        this.aspectY = aspectY
    }


    //裁剪结果回调
    interface OnResultListener {
        fun onResult(file: File)
    }


    //初始化图片文件及URI
    private fun initFile() {
        //有权限
        file = FileUtlis().getFile("${System.currentTimeMillis()}.jpg")
        file?.let {
            if (!it.parentFile.exists()) {
                it.parentFile.mkdirs()
            }
            if (!it.exists()) {
                it.createNewFile()
            }
            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(activity, "${activity.packageName}.provider", file)
            } else {
                Uri.fromFile(file)
            }
        }

    }

    //检查所有权限是否拥有
    private fun checkPermission(array: Array<String>): Boolean {
        array.forEach {
            if (ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED) {
                missPermission = it
                return false
            }
        }
        return true
    }
}