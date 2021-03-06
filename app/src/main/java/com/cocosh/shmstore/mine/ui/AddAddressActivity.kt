package com.cocosh.shmstore.mine.ui

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.Address
import com.cocosh.shmstore.mine.presenter.AddRessPresenter
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.PickerViewUtils
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_add_address.*

/**
 * Created by lmg on 2018/4/25.
 * 增加收货地址
 */
class AddAddressActivity : BaseActivity(), MineContrat.IAddAddressView {
    var mPresenter = AddRessPresenter(this, this)
    var mAddress:Address? = null
    var addressCode = ""
    var default = "0"
    var id = "0"

    private lateinit var mPickerViewUtils: PickerViewUtils
    override fun setLayout(): Int = R.layout.activity_add_address

    override fun addAddress(result: BaseBean<String>) {
        ToastUtil.show("添加成功")
        if (intent.getStringExtra("type") ?: "" == "web") {
            setResult(IntentCode.IS_INPUT)
        }
        finish()
    }

    override fun initView() {
        mPickerViewUtils = PickerViewUtils(this)
        chooseAddress.setOnClickListener(this)
        save.setOnClickListener(this)

        mAddress = intent.getSerializableExtra("mAddress") as Address?

//        nameIn = intent.getStringExtra("name")
//        phoneIn = intent.getStringExtra("phone")
//        addressCode = intent.getStringExtra("area")
//        areaDescIn = intent.getStringExtra("areaDesc")
//        comment_id = intent.getStringExtra("comment_id")
//        areaNameIn = intent.getStringExtra("areaName")

        mAddress?.let {
            name.setText(it.receiver)
            phone.setText(it.phone)
            chooseAddress.setText(it.district)
            address.setText(it.addr)
            addressCode = it.province+"-"+it.city+"-"+it.town
            id = it.id
            default = it.default
        }

        initlistener()

        if (mAddress != null) {
            titleManager.defaultTitle("编辑收货地址")
            checkData()
        } else {
            titleManager.defaultTitle("新增收货地址")
        }
    }

    private fun initlistener() {
        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkData()
            }
        })
        phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkData()
            }
        })
        chooseAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkData()
            }
        })
        address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkData()
            }
        })
    }

    fun checkData() {
        if (name.text.isNullOrEmpty() || name.text.length < 2) {
            save.setBackgroundResource(R.color.grayBtn)
            return
        }
        if (phone.text.isNullOrEmpty() || phone.text.length != 11) {
            save.setBackgroundResource(R.color.grayBtn)
            return
        }
        if (chooseAddress.text.isNullOrEmpty() || chooseAddress.text.length < 2) {
            save.setBackgroundResource(R.color.grayBtn)
            return
        }
        if (address.text.isNullOrEmpty() || address.text.length < 2) {
            save.setBackgroundResource(R.color.grayBtn)
            return
        }
        save.setBackgroundResource(R.color.red)
    }

    override fun onListener(view: View) {
        when (view.id) {
            chooseAddress.id -> {
                mPickerViewUtils.showAddress(object : PickerViewUtils.OnAddressResultListener {
                    override fun onPickerViewResult(address: String, code: String) {
                        chooseAddress.setText(address)
                        addressCode = code
                    }
                })
            }
            save.id -> {
                if (name.text.isNullOrEmpty() || name.text.length < 2) {
                    ToastUtil.show("请输入正确的姓名")
                    return
                }
                if (phone.text.isNullOrEmpty() || phone.text.length != 11) {
                    ToastUtil.show("请输入正确的电话号")
                    return
                }
                if (chooseAddress.text.isNullOrEmpty() || chooseAddress.text.length < 2) {
                    ToastUtil.show("请选择地区")
                    return
                }
                if (address.text.isNullOrEmpty() || address.text.length < 2) {
                    ToastUtil.show("请输入正确的详细地址")
                    return
                }

                 addressCode.split("-").let {
                    //通过
                    mPresenter.requestAddAddress(id, name.text.toString(), phone.text.toString(), it[0],
                            it[1],it[2], address.text.toString(),default)
                }

            }
        }
    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, AddAddressActivity::class.java))
        }

        fun start(mContext: Context,address:Address) {
            mContext.startActivity(Intent(mContext, AddAddressActivity::class.java).putExtra("mAddress", address))
        }
    }
}