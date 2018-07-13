package com.cocosh.shmstore.widget.imageview

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import kotlinx.android.synthetic.main.view_img_upload.view.*

/**
 * Created by cjl on 2018/2/2.
 */
class MyUploadImageView : RelativeLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var myview_img: ImageView
     var myview_img_layout: RelativeLayout
     var myview_img_error: ImageView
     var myview_img_loading: ProgressBar
      init {
          val inflate = LayoutInflater.from(context).inflate(R.layout.view_img_upload, this, true)
            myview_img=inflate.myview_img
          myview_img_layout=inflate.myview_img_layout
          myview_img_error=inflate.myview_img_error
          myview_img_loading=inflate.myview_img_loading
      }

    /**
     * 上传成功
     */
    fun getImageView(): ImageView {
        return myview_img
    }

    /**
     * 上传成功
     */
    fun upLoadSuccess() {
        myview_img_layout.visibility = View.GONE
        myview_img_loading.visibility = View.VISIBLE
        myview_img_error.visibility = View.GONE
    }

    /**
     * 上传失败
     */
    fun upLoadFail() {
        myview_img_loading.visibility = View.GONE
        myview_img_error.visibility = View.VISIBLE
    }

    /**
     * 上传失败
     */
    fun startUpLoad() {
        myview_img_layout.visibility = View.VISIBLE
    }

    /**
     * 设置背景
     */
    fun setBgRes(@DrawableRes resid: Int) {
        myview_img.setBackgroundResource(resid)
    }

    /**
     * 设置图片
     */
    fun setImgRes(@DrawableRes resid: Int) {
        myview_img.setImageResource(resid)
    }
}