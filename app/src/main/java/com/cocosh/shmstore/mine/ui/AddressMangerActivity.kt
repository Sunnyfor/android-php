package com.cocosh.shmstore.mine.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.RadioButton
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.mine.adapter.AddressListAdapter
import com.cocosh.shmstore.mine.adapter.SpaceHItem
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.AddressListModel
import com.cocosh.shmstore.mine.presenter.AddRessPresenter
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_address_manger.*

/**
 * Created by lmg on 2018/4/25.
 * 关注
 */
class AddressMangerActivity : BaseActivity(), MineContrat.IAddressView {
    var type: String? = null
    var id = ""
    var defaultId = ""
    var mPresenter = AddRessPresenter(this, this)
    var adapter: AddressListAdapter? = null
    var list = ArrayList<AddressListModel>()
    var isShow = false //判断是否需要直接弹窗
    override fun setLayout(): Int = R.layout.activity_address_manger
    override fun deleteAddress(result: BaseModel<String>) =
            if (result.success && result.code == 200) {
                val data = list.first {
                    it.idUserAddressInfo == id
                }
                list.remove(data)
                recyclerView.adapter.notifyDataSetChanged()
            } else {
                ToastUtil.show(result.message)
            }

    override fun getAddress(result: BaseModel<ArrayList<AddressListModel>>) {
        if (result.success && result.code == 200) {
            if (result.entity != null) {
                list.clear()
                list.addAll(result.entity!!)
                recyclerView.adapter.notifyDataSetChanged()

                if (isShow) {
                    isShow = false
                    SmediaDialog(this@AddressMangerActivity).let {
                        it.setTitle("当前选择的收货地址")
                        it.setDesc(list.last().areaName + "-" + list.last().address)
                        it.OnClickListener = View.OnClickListener {
                            val data = Intent()
                            data.putExtra("idUserAddressInfo", list.last().idUserAddressInfo)
                            data.putExtra("addressName", list.last().addressName)
                            data.putExtra("addressPhone", list.last().addressPhone)
                            data.putExtra("address", list.last().address)
                            data.putExtra("areaName", list.last().areaName)
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                        it.show()
                    }
                }

            }
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun defaultAddress(result: BaseModel<String>) {
        if (result.success && result.code == 200) {
            list.forEach {
                if (it.idUserAddressInfo == defaultId) {
                    it.isDefault = "1"
                } else {
                    it.isDefault = "0"
                }
            }
            recyclerView.adapter.notifyDataSetChanged()
        } else {
            ToastUtil.show(result.message)
        }
    }

    override fun initView() {
        titleManager.defaultTitle("地址管理")
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h35).toInt(), resources.getDimension(R.dimen.h35).toInt()))
        add.setOnClickListener(this)
        adapter = AddressListAdapter(this, list)
        recyclerView.adapter = adapter

        type = intent.getStringExtra("type")


        adapter?.setOnSelectItemListener(object : AddressListAdapter.OnSelectItemListener {
            override fun itemSelect(index: Int) {
                val data = Intent()
                if (type != null && type == "web") {
                    SmediaDialog(this@AddressMangerActivity).let {
                        it.setTitle("当前选择的收货地址")
                        it.setDesc(list[index].areaName + "-" + list[index].address)
                        it.OnClickListener = View.OnClickListener {
                            data.putExtra("idUserAddressInfo", list[index].idUserAddressInfo)
                            data.putExtra("addressName", list[index].addressName)
                            data.putExtra("addressPhone", list[index].addressPhone)
                            data.putExtra("address", list[index].address)
                            data.putExtra("areaName", list[index].areaName)
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                        it.show()
                    }
                }
            }

            override fun checkedChange(index: Int) {
                //default
                defaultId = list[index].idUserAddressInfo ?: ""
                mPresenter.requestDefaultAddress(list[index].idUserAddressInfo ?: "")
            }

            override fun onEdit(position: Int) {
                AddAddressActivity.start(this@AddressMangerActivity, list[position].addressName
                        ?: "", list[position].addressPhone ?: ""
                        , list[position].areaCode ?: "", list[position].address
                        ?: "", list[position].idUserAddressInfo ?: ""
                        , list[position].areaName ?: "")
            }

            override fun onDelete(position: Int) {
                showDeleteDialog(list[position].idUserAddressInfo ?: "")
            }
        })
    }

    override fun onListener(view: View) {
        when (view.id) {
            add.id -> {
                val it = Intent(this, AddAddressActivity::class.java)
                it.putExtra("type", type)
                startActivityForResult(it, IntentCode.IS_INPUT)
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {
        mPresenter.requestGetAddress(1)
    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, AddressMangerActivity::class.java))
        }
    }

    fun showDeleteDialog(id: String) {
        val dialog = SmediaDialog(this)
        dialog.setTitle("是否确认删除")
        dialog.OnClickListener = View.OnClickListener {
            this.id = id
            mPresenter.requestDeleteAddress(0, id)
        }
        dialog.show()
    }

    override fun onResume() {
        mPresenter.requestGetAddress(1)
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_INPUT && resultCode == IntentCode.IS_INPUT) {
            isShow = true
        }
    }
}

