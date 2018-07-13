package com.cocosh.shmstore.zxing.decode

import android.app.Activity
import android.content.DialogInterface

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class FinishListener(private val activityToFinish: Activity) : DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    override fun onCancel(dialogInterface: DialogInterface) {
        run()
    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        run()
    }

    private fun run() {
        activityToFinish.finish()
    }

}