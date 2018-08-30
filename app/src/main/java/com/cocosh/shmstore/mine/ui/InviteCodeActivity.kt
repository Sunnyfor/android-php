package com.cocosh.shmstore.mine.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.InviteCodeContrat
import com.cocosh.shmstore.mine.model.InviteCodeModel
import com.cocosh.shmstore.mine.presenter.InviteCodePresenter
import com.cocosh.shmstore.utils.GlideUtils
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.ShareDialog
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.invite_code_activity.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.model.GlideUrl


/**
 *
 * Created by cjl on 2018/2/1.
 */
class InviteCodeActivity : BaseActivity(), InviteCodeContrat.IView {
    override fun reTryGetData() {

    }

    var type: String? = null
    private var isArgeen = false
    private var infoData: InviteCodeModel? = null
    private val REQUESTCODE = 1
    private var persenter: InviteCodeContrat.IPresenter = InviteCodePresenter(this, this)

    private val url: String by lazy {
        if (type == "2") {
            Constant.MYSELF_MATCHMAKER_INVITATION_IMAGE
        } else {
            Constant.MYSELF_PROVIDER_INVITATION_IMAGE
        }
    }
    private val authorization: GlideUrl by lazy {
        GlideUrl(ApiManager2.getHost() + "/" + url,
                LazyHeaders.Builder().addHeader("authorization", ApiManager2.headerInterceptor.getAuthorization()).build())
    }

    override fun inviteCodeData(result: BaseBean<InviteCodeModel>) {
        //加载数据
        infoData = result.message
        setData(result.message)
    }

    private fun setData(info: InviteCodeModel?) {
        tv_invite_code.text = info?.invite_code
        GlideUtils.loadHead(this, info?.avatar, name_icon)
        loadQRcode()
        if (type == "2") {
            name.text = info?.nickname
            name_nature.text = "新媒人"
        } else {
            name.text = info?.corpFname
            name_nature.text = "服务商"
        }
    }

    override fun setLayout(): Int = R.layout.invite_code_activity

    override fun initView() {
        titleManager.defaultTitle(getString(R.string.invisit_code), "", 0, 0, null)
        type = intent.getStringExtra("type")
        invite_code_btn.setOnClickListener(this)
        tv_conserve_img.setOnClickListener(this)
        initData()
    }

    private fun initData() {
        tv_conserve_img.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
        tv_conserve_img.paint.isAntiAlias = true//抗锯齿
        type?.let {
            persenter.requestInviteCodeData(1, UserManager.getUserId(), it)
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
        //分享
            invite_code_btn.id -> {
                if (infoData != null) {
                    val dialog = ShareDialog(this)
                    dialog.showShareBase("首媒约你一起互联网+", "抢不完的现金红包等着你", infoData?.codeInfo ?: "")
                    dialog.show()
                }
            }

            tv_conserve_img.id -> {
                //保存
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUESTCODE);
                } else {
                    saveImageToGallery()
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            start(context, null)
        }

        fun start(context: Context, type: String?) {
            val intent = Intent(context, InviteCodeActivity::class.java)
            type?.let {
                intent.putExtra("type", it)
            }
            context.startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this)?.onActivityResult(requestCode, resultCode, data)//完成回调
    }


    //保存文件到指定路径
    private fun saveImageToGallery() {
        Glide.with(this).load(authorization).asBitmap().toBytes().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(object : SimpleTarget<ByteArray>() {
            override fun onResourceReady(resource: ByteArray?, glideAnimation: GlideAnimation<in ByteArray>?) {
                try {
                    savaBitmap(resource!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     * 权限回掉
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUESTCODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了
                    saveImageToGallery()
                } else {
                    // 权限被用户拒绝了
                }
                return
            }
        }
    }

    /**
     * 图片写到本地
     */
    fun savaBitmap(bytes: ByteArray) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val filePath: String?
            var fos: FileOutputStream? = null
            try {
                filePath = Environment.getExternalStorageDirectory().canonicalPath + "/MyImg"
                val imgDir = File(filePath)
                if (!imgDir.exists()) {
                    imgDir.mkdirs()
                }
                val imgName = filePath + "/" + System.currentTimeMillis() + ".png"
                fos = FileOutputStream(imgName)
                fos.write(bytes)
                //保存图片后发送广播通知更新数据库
                val uri = Uri.fromFile(File(imgName))
                this@InviteCodeActivity.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                LogUtil.d("图片已保存到" + filePath)
                runOnUiThread({
                    ToastUtil.show("保存成功")
                })
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (fos != null) {
                        fos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            ToastUtil.show("保存失败")
        }
    }


    private fun loadQRcode() {
        Glide.with(this).load(authorization).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.default_content).into(qr_code)
    }
}