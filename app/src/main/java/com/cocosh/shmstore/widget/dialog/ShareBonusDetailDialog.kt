package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.net.Uri
import android.view.View
import android.view.Window
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.ToastUtil
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import kotlinx.android.synthetic.main.dialog_share_bonus.*

/**
 *
 * Created by zhangye on 2018/5/2.
 */
class ShareBonusDetailDialog(context: BaseActivity) : Dialog(context), View.OnClickListener{

    var onClickResult:OnDialogResult? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_share_bonus)
        window.setBackgroundDrawableResource(R.color.transparent)
        tvCancel.setOnClickListener(this)
        tvSure.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            tvCancel.id -> dismiss()
            tvSure.id -> {
                onClickResult?.onResult(edtTitle.text.toString())
                dismiss()
            }
        }
    }

}