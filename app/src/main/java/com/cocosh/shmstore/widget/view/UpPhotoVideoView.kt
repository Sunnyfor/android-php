package com.cocosh.shmstore.widget.view

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.newhome.adapter.UpPhotoVideoAdapter
import com.cocosh.shmstore.utils.CameraPhotoUtils
import com.cocosh.shmstore.widget.dialog.BottomPhotoDialog
import kotlinx.android.synthetic.main.layout_photo_video.view.*
import java.io.File

class UpPhotoVideoView : FrameLayout, BottomPhotoDialog.OnItemClickListener, CameraPhotoUtils.OnResultListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var file: File? = null

    val dialog: BottomPhotoDialog by lazy {
        BottomPhotoDialog(context).apply {
            setOnItemClickListener(this@UpPhotoVideoView)
        }
    }

    private val cameraUtils: CameraPhotoUtils by lazy {
        CameraPhotoUtils(context as BaseActivity).apply {
            setAspectXY(200,150)
            onResultListener = this@UpPhotoVideoView
        }
    }

    var showLoading: (() -> Unit)? = null
    var hideLoading: (() -> Unit)? = null

    val list = arrayListOf<String>()
    private val upPhotoVideoAdapter: UpPhotoVideoAdapter by lazy {
        UpPhotoVideoAdapter(list,dialog)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_photo_video, this, true)
        recl_photo.setHasFixedSize(true)
        recl_photo.isNestedScrollingEnabled = false
        recl_photo.layoutManager = GridLayoutManager(context, 3)
        recl_photo.adapter = upPhotoVideoAdapter
    }


    fun setData(mList: ArrayList<String>) {
        list.clear()
        list.addAll(mList)
        upPhotoVideoAdapter.notifyDataSetChanged()
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        cameraUtils.onActivityResult(requestCode, resultCode, data)
    }

    override fun onTopClick() {
        cameraUtils.startCamera()
    }

    override fun onBottomClick() {
        cameraUtils.startPoto()
    }

    override fun onResult(file: File) {
        this.file = file
        updatePhoto()
    }


    //上传头像
    private fun updatePhoto() {
        file?.let { file ->
            showLoading?.invoke()
            ApiManager2.postImage(context as BaseActivity, file.path, Constant.COMMON_UPLOADS, object : ApiManager2.OnResult<BaseBean<ArrayList<String>>>() {
                override fun onSuccess(data: BaseBean<ArrayList<String>>) {
                    hideLoading?.invoke()
                    data.message?.let {
                        list.add(it[0])
                        upPhotoVideoAdapter.notifyDataSetChanged()
                    }

                }

                override fun onFailed(code: String, message: String) {
                    hideLoading?.invoke()
                }

                override fun onCatch(data: BaseBean<ArrayList<String>>) {

                }

            })
        }

    }
}