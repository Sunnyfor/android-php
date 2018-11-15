package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ad_recharge_give.*

class ADRechargeGiveActivity : BaseActivity() {
    override fun setLayout(): Int = R.layout.activity_ad_recharge_give

    override fun initView() {
        titleManager.defaultTitle("赠送")

        editMoney.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start == 0 && s =="0"){
                    editMoney.text = null
                }
            }

        })

        llGiveCoupon.setOnClickListener {
            cbCoupon.isChecked = !cbCoupon.isChecked
        }

        llGiveMoney.setOnClickListener {
            cbMoney.isChecked = !cbMoney.isChecked
        }

        rlCoupon.setOnClickListener(this)

        cbCoupon.setOnCheckedChangeListener { _, isChecked ->
            rlCoupon.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        cbMoney.setOnCheckedChangeListener { _, isChecked ->
            rlMoney.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

    }

    override fun onListener(view: View) {
        when (view.id) {
            rlCoupon.id -> {
                startActivity(Intent(this,ADRechargeSelectListActivity::class.java))
            }
        }
    }

    override fun reTryGetData() {
    }
}