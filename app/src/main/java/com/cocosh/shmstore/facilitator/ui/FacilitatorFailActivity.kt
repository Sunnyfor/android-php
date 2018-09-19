package com.cocosh.shmstore.facilitator.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.utils.PermissionCode
import com.cocosh.shmstore.utils.PermissionUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_facilitator_fail.*


/**
 * 失败页
 * Created by lmg on 2018/4/2.
 */
class FacilitatorFailActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    var openType = -1  //777 对应审核失败  其他对应打款失败

    override fun initView() {
        titleManager.facTitle().setRightOnClickListener(View.OnClickListener {
            showDialog()
        })
        permissionUtil = PermissionUtil(this)
        openType = intent.getIntExtra("OPEN_TYPE", -1)
        if (openType == 777) {
            //认证中
            btn_again.visibility = View.GONE
            tv_red2.text = ("该企业已完成服务商认证\n" + "如有疑问联系首媒客服：400-966-1168")
        } else {
            tv_red2.text = "打款金额有误，钱款已退回"
        }
        btn_again.setOnClickListener(this)
    }

    override fun setLayout(): Int = R.layout.activity_facilitator_fail

    override fun onListener(view: View) {
        when (view.id) {
            btn_again.id -> {
                next()
            }
        }
    }

    private lateinit var permissionUtil: PermissionUtil
    private fun showDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("首媒客服电话")
        dialog.setDesc("400-966-1168")
        dialog.setPositiveText("呼叫")
        dialog.OnClickListener = View.OnClickListener {
            if (permissionUtil.callPermission()) {
                callPhone()
            }
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCode.PHONE.type) {
            if (permissionUtil.checkPermission(permissions)) {
                callPhone()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:400-966-1168"))
        startActivity(intent)
    }

    companion object {
        fun start(mContext: Context, openType: Int) {
            val intent = Intent(mContext, FacilitatorFailActivity::class.java)
            intent.putExtra("OPEN_TYPE", openType)
            mContext.startActivity(intent)
        }
    }


    fun next() {
        ApiManager2.post(this, hashMapOf(), Constant.SVC_CERT_UPDATE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                //跳转 A
                PayFranchiseFeeActivity.start(this@FacilitatorFailActivity, 666)
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<String>) {

            }
        })
    }
}