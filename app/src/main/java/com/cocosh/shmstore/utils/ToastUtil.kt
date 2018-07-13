package com.cocosh.shmstore.utils

import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import kotlinx.android.synthetic.main.layout_icon_toast.view.*

/**
 * 功能:单例Toast
 * 创建时间: 2016/9/12/03:18
 * Created by 张野 on 2017/10/12.
 */
object ToastUtil {

    private var toast = Toast.makeText(SmApplication.getApp(), "", Toast.LENGTH_SHORT)
    private var layoutToast: Toast = Toast(SmApplication.getApp())
    /**
     * 显示Toast
     * @param content Toast信息
     */
    fun show(content: String?, type: Int) {
        content?.let {
            toast.setText(content)
            toast.duration = type
            toast.show()
        }
    }

    fun show(content: String?) {
        show(content, Toast.LENGTH_SHORT)
    }

    fun showIcon(iconStr: String?, textStr: String) {
        layoutToast.view = View.inflate(SmApplication.getApp(), R.layout.layout_icon_toast, null)
        iconStr?.let {
            layoutToast.view.ivIcon.text = it
        }
        layoutToast.view.tvDesc.text = textStr
        layoutToast.setGravity(Gravity.CENTER, 0, 0)
        layoutToast.show()
    }

}