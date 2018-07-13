package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_bankcard_remove_result.*

/**
 * 认证成功页面（实人）
 * Created by zhangye on 2018/3/23.
 */
class RemovedResultActivity : BaseActivity() {
    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.activity_bankcard_remove_result

    override fun initView() {
        titleManager.defaultTitle("解绑结果")
        initData()
    }

    private fun initData() {
        val type = intent.getBooleanExtra("type", false)
        if (type) {
            icon.setImageResource(R.drawable.success_s)
            successLl.visibility = View.VISIBLE
            desc.text = "解绑成功"
            val pic = intent.getStringExtra("pic")
            val name = intent.getStringExtra("name")
            val num = intent.getStringExtra("num")
            GlideUtils.loadRound(2, this, pic, rivLogo)
            tvBankName.text = name
            tvBankNum.text = num
        } else {
            icon.setImageResource(R.drawable.error_fail)
            successLl.visibility = View.GONE
            desc.text = "解绑失败"
        }
    }

    override fun onListener(view: View) {

    }

    companion object {
        fun start(mContext: Context, pic: String, name: String, num: String, type: Boolean) {
            mContext.startActivity(Intent(mContext, RemovedResultActivity::class.java)
                    .putExtra("pic", pic)
                    .putExtra("name", name)
                    .putExtra("num", num)
                    .putExtra("type", type))
        }

        fun start(mContext: Context, type: Boolean) {
            mContext.startActivity(Intent(mContext, RemovedResultActivity::class.java)
                    .putExtra("type", type))
        }

    }
}