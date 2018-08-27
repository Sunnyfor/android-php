package com.cocosh.shmstore.newCertification.ui

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.coco_sh.shmstore.mine.adapter.AddressShiAdapter
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.enterpriseCertification.ui.CorporateAccountActivty
import com.cocosh.shmstore.newCertification.adapter.AddressAdapter
import com.cocosh.shmstore.newCertification.adapter.AddressListAdapter
import com.cocosh.shmstore.newCertification.contrat.AddressContrat
import com.cocosh.shmstore.newCertification.model.AddressServiceModel
import com.cocosh.shmstore.newCertification.model.ApplyPartner
import com.cocosh.shmstore.newCertification.presenter.AddressPresenter
import com.cocosh.shmstore.newCertification.ui.fragment.CertificationAddressShengFragment
import com.cocosh.shmstore.newCertification.ui.fragment.CertificationAddressShiFragment
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_certification_address.*


/**
 *
 * Created by cjl on 2018/2/5.
 */
class CertificationAddressActivity : BaseActivity(), AddressContrat.IView {
    override fun reTryGetData() {
        presenter.requestAddress(1, 0)
    }

    var list = ArrayList<AddressServiceModel>()
    private var shiList = ArrayList<AddressServiceModel>()
    var adapter: AddressAdapter? = null
    var shiAdapter: AddressShiAdapter? = null
    private var isCity = 0
    var address = ""
    var shengIndex = -1
    var shiIndex = -1
    var openType = -1
    var prov:Int = 0
    var city:Int = 0
    private lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList: ArrayList<String>
    private lateinit var mAdapter: AddressListAdapter
    // fragment事务类
    private lateinit var fragmentSheng: CertificationAddressShengFragment
    private lateinit var fragmentShi: CertificationAddressShiFragment

    override fun addressFacResult(prov: Int,result: BaseBean<ArrayList<AddressServiceModel>>) {
        //服务商
        com.cocosh.shmstore.utils.LogUtil.d(result.toString())
            if (prov == 0) {
                list.clear()
                if (result.message != null) {
                    list.addAll(result.message!!)
                }
                fragmentSheng.notifyData()
            } else {
                shiList.clear()
                if (result.message != null) {
                    shiList.addAll(result.message!!)
                }
                fragmentShi.notifyData()
            }
    }

    override fun commitAddressResult(result: BaseBean<ApplyPartner>) {
        LogUtil.d(result.toString())
//        if (result.code == 200 && result.success) {
//            val bizCode = result.entity?.bizCode
//            val profit = result.entity?.profit
//            if (bizCode != null && profit != null) {
//                SmApplication.getApp().setData("bizCode", bizCode)
//                SmApplication.getApp().setData("profit", profit)
//                ConfirmInformationActivity.start(this)
//            } else {
//                ToastUtil.show("信息提交失败")
//            }
//        } else {
//            ToastUtil.show(result.message)
//        }
    }

    val presenter = AddressPresenter(this, this)
    override fun addressResult(prov:Int,result: BaseBean<ArrayList<AddressServiceModel>>?) {
        com.cocosh.shmstore.utils.LogUtil.d(result.toString())
            if (prov == 0) {
                list.clear()
                if (result?.message != null) {
                    list.addAll(result.message!!)
                }
                fragmentSheng.notifyData()
            } else {
                shiList.clear()
                if (result?.message != null) {
                    shiList.addAll(result.message!!)
                }
                fragmentShi.notifyData()
            }
    }

    override fun setLayout(): Int = R.layout.activity_certification_address

    override fun initView() {
        titleManager.defaultTitle("确认服务城市")
        openType = intent.getIntExtra("FACILITATOR_TYPE", -1)
        initList()
        bt_next.setOnClickListener(this)
        bt_next2.setOnClickListener(this)
        iv_close.setOnClickListener(this)
        if (openType == 333) {
            bt_next2.visibility = View.GONE
            rl.visibility = View.VISIBLE
            ll.visibility = View.VISIBLE
            presenter.requestFacilitatorAddress(1, 0)
        } else {
            bt_next2.visibility = View.VISIBLE
            rl_btnNext.visibility = View.GONE
            presenter.requestAddress(1, 0)
        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            R.id.bt_next -> {

                if (address == "") {
                    return
                }
                //对公账户页 并且存储
                val map = SmApplication.getApp().getData<HashMap<String, String>>(DataCode.FACILITATOR_KEY_MAP, false)
                map?.let {
                    it["province"] = prov.toString()
                    it["city"] = city.toString()
                    it["profit"] = money_text.text.toString()
//                    it["areaName"] = main_tab.getTabAt(0)?.text.toString() + "-" + main_tab.getTabAt(1)?.text.toString()
                    SmApplication.getApp().setData(DataCode.FACILITATOR_KEY_MAP, map)
                    CorporateAccountActivty.start(this, 444)
                }
            }

            R.id.bt_next2 -> {

                if (address == "") {
                    return
                }

                //先择服务器地址
                val it = Intent(this, SelectServiceActivity::class.java)
                it.putExtra("prov", prov)
                it.putExtra("city",city)
                startActivityForResult(it, IntentCode.FINISH)
            }

            R.id.iv_close -> {
                rl.visibility = View.GONE
            }
        }
    }

    private fun request(position: Int) {
//        val code = list[position].code?:0
//        isCity = 1
//        if (openType == 333) {
//            presenter.requestFacilitatorAddress(1, code)
//        } else {
//            presenter.requestAddress(1, code)
//        }
    }

    private fun initList() {
        fragmentSheng = CertificationAddressShengFragment(list)
        fragmentShi = CertificationAddressShiFragment(shiList)
        //设置ViewPager里面也要显示的图片
        fragmentList = arrayListOf()
        fragmentList.add(fragmentSheng)
        fragmentList.add(fragmentShi)

        //设置标题
        titleList = arrayListOf()
        titleList.add("选择省直辖市")
        titleList.add(" ")

        //设置tab的模式
        main_tab.tabMode = TabLayout.MODE_FIXED
        //添加tab选项卡
        titleList.forEach {
            main_tab.addTab(main_tab.newTab().setText(it))
        }
        //实例化adapter
        mAdapter = AddressListAdapter(supportFragmentManager, fragmentList, titleList)
        //给ViewPager绑定Adapter
        main_viewpager.adapter = mAdapter
        //把TabLayout和ViewPager关联起来
        main_tab.setupWithViewPager(main_viewpager)
        main_viewpager.setCurrentItem(0, false)
        main_viewpager.setOnTouchListener { _, _ -> true }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CertificationAddressActivity::class.java))
        }
    }

    fun chooseSheng(shengStr: String, code: Int, index: Int) {
        this.prov = code
        main_tab.getTabAt(0)?.text = shengStr
        main_tab.getTabAt(1)?.text = "选择市/区"
        bt_next2.isClickable = false
        bt_next2.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_line))
        bt_next.isClickable = false
        bt_next.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_line))
        money_text.text = ""
        request(index)
        main_viewpager.setCurrentItem(1, true)
        if (openType == 333) {
            presenter.requestFacilitatorAddress(1,code)
        }else{
            presenter.requestAddress(1,code)
        }

    }

    fun chooseShi(shiStr: String, money: String, code: Int) {
        this.city = code
        bt_next.isClickable = true
        bt_next.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        bt_next2.isClickable = true
        bt_next2.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        service_address_order_rl.visibility = View.VISIBLE
        bt_next.setBackgroundColor(resources.getColor(R.color.red))
        money_text.text = money
        main_tab.getTabAt(1)?.text = shiStr
        address = code.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentCode.FINISH && requestCode == IntentCode.FINISH) {
            setResult(requestCode)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}