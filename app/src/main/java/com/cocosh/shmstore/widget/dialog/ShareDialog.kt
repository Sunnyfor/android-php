package com.cocosh.shmstore.widget.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.ToastUtil
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import kotlinx.android.synthetic.main.include_share.*

/**
 *
 * Created by lmg on 2018/4/8.
 */
class ShareDialog(var baseActivity: BaseActivity) : Dialog(baseActivity), View.OnClickListener {
    private lateinit var shareApi: UMShareAPI
    private var url: String? = null
    var isFinish = false

    fun initView() {
        shareApi = UMShareAPI.get(baseActivity)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 让dialog置于底部
        val window = window!!
        window.decorView.setPadding(0, 0, 0, 0)
        val attributes = window.attributes
        attributes.width = RelativeLayout.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window.attributes = attributes
        window.setBackgroundDrawableResource(R.color.transparent)
        setContentView(R.layout.dialog_share)
        rlWeixin.setOnClickListener(this)
        rlWeixinCircle.setOnClickListener(this)
        rlQQ.setOnClickListener(this)
        rlWeibo.setOnClickListener(this)
        rlCopy.setOnClickListener(this)


        setCancelable(true)
    }

    override fun onClick(v: View) {
        dismiss()
        when (v.id) {
            rlWeixin.id -> {

                if (!shareApi.isInstall(baseActivity, SHARE_MEDIA.WEIXIN)) {
                    ToastUtil.show(context.getString(R.string.notWeixin))
                    return
                }
//                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onItemClick(rlWeixin.id)
                }
            }

            rlWeixinCircle.id -> {

                if (!shareApi.isInstall(baseActivity, SHARE_MEDIA.WEIXIN)) {
                    ToastUtil.show(context.getString(R.string.notWeixin))
                    return
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onItemClick(rlWeixinCircle.id)
                }
            }

            rlQQ.id -> {
                if (!shareApi.isInstall(baseActivity, SHARE_MEDIA.QQ)) {
                    ToastUtil.show(context.getString(R.string.notQQ))
                    return
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onItemClick(rlQQ.id)
                }
            }

            rlWeibo.id -> {
                if (!shareApi.isInstall(baseActivity, SHARE_MEDIA.SINA)) {
                    ToastUtil.show(context.getString(R.string.notSina))
                    return
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onItemClick(rlWeibo.id)
                }
            }

            rlCopy.id -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener?.onItemClick(rlCopy.id)
                }
            }
        }
    }

    //分享
    private fun onlyWxAndQQ() {
        rlWeixinCircle.visibility = View.GONE
        rlWeibo.visibility = View.GONE
        rlCopy.visibility = View.GONE
    }

    interface OnItemClickListener {
        fun onItemClick(index: Int)
    }

    var mOnItemClickListener: OnItemClickListener? = null


    private fun shareDownloadLink(index: Int, shareAction: ShareAction) {
        val umShareListener = object : UMShareListener {
            override fun onStart(p0: SHARE_MEDIA?) {}
            override fun onResult(platform: SHARE_MEDIA) {
                ToastUtil.show("分享成功")
                if (isFinish) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("type", DataCode.BONUS_GIVE)
                    baseActivity.setResult(IntentCode.IS_INPUT, resultIntent)
                    baseActivity.finish()
                }
            }

            override fun onError(platform: SHARE_MEDIA, t: Throwable) {
                ToastUtil.show("分享失败")
            }

            override fun onCancel(platform: SHARE_MEDIA) {
            }
        }

        when (index) {
            rlWeixin.id -> shareAction.setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).share()

            rlWeixinCircle.id -> shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).share()

            rlQQ.id -> shareAction.setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).share()

            rlWeibo.id -> shareAction.setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener).share()

            rlCopy.id -> {
                copyFromText()
                ToastUtil.show("复制成功")
            }
        }

    }

    init {
        initView()
    }


    /**
     * 拷贝链接
     */
    private var mClipboard: ClipboardManager? = null

    private fun copyFromText() {
        if (mClipboard == null) {
            mClipboard = baseActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        }
        val url = Uri.encode(url, "-![.:/,%?&=]")
        val clip = ClipData.newPlainText("share", url)
        mClipboard?.primaryClip = clip
    }


    /**
     * 通用分享
     */
    fun showShareBase(title: String, description: String, mUrl: String) {
        showShareBase(title, description, mUrl, null)
    }


    /**
     * 通用分享
     */
    fun showShareBase(title: String, description: String, mUrl: String, imageUrl: String?) {
        isFinish = false
        url = mUrl
        val shareAction = ShareAction(baseActivity)
        val web = UMWeb(Uri.encode(url, "-![.:/,%?&=]"))
        web.title = title
        web.description = description
        if (imageUrl == null) {
            web.setThumb(UMImage(baseActivity, R.drawable.ic_share))
        } else {
            web.setThumb(UMImage(baseActivity, imageUrl))
        }
        shareAction.withMedia(web)
        mOnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(index: Int) {
                shareDownloadLink(index, shareAction)
            }
        }
        show()
    }


    //分享APP
    fun showShareApp(url: String) {
        showShareBase("想投广告任你投，想抢红包任你抢！", " 全球广告自主投放平台，广告投放成本低至免费。平台红包抢不停，现金红包最大千元。", url)
    }


    //赠送红包
    fun showGiveBouns(no: String, token: String,redpacketId:String?) {
        isFinish = true
        onlyWxAndQQ()
        mOnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(index: Int) {
                val dialog = ShareBonusDetailDialog(baseActivity)
                dialog.onClickResult = object : OnDialogResult {
                    override fun onResult(result: Any) {
                        (result as String).let {
                            bonusGive(result, no, token, index,redpacketId)
                        }
                    }
                }
                dialog.show()
            }
        }
        show()
    }

    //分享红包
    fun showShareBonus(url: String) {
        showShareBase("喜从天降，抢到一个红包", "现金红包抢不完!", url)

    }


    //赠送红包
    private fun bonusGive(desc: String, no: String, token: String, index: Int,redpacketId:String?) {

        val url = if (redpacketId != null){
            Constant.RP_FAV_TO_GIVE
        }else{
            Constant.RP_DO_GIVE
        }

        val params = hashMapOf<String, String>()
        params["no"] = no
        params["token"] = token

        ApiManager2.post(baseActivity, params, url, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<String>) {

            }

            override fun onSuccess(data: BaseBean<String>) {
                    SmApplication.getApp().setData(DataCode.BONUS, BonusAction.GIVE)
                    val htmlUrl = data.message
                    val shareAction = ShareAction(baseActivity)
                    val web = UMWeb(Uri.encode(htmlUrl, "-![.:/,%?&=]"))
                    web.description = "抢不完的现金红包等着你!"

                    if (desc.isEmpty()) {
                        web.title = "我送你一个红包，快来领取吧"
                    } else {
                        web.title = desc
                    }
                    web.setThumb(UMImage(baseActivity, R.drawable.ic_share))
                    shareAction.withMedia(web)
                    shareDownloadLink(index, shareAction)
            }
        })

    }
}