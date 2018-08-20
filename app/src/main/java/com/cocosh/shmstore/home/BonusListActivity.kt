package com.cocosh.shmstore.home

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.adapter.BonusListAdapter
import com.cocosh.shmstore.home.model.Bonus2
import com.cocosh.shmstore.home.model.BonusAction
import com.cocosh.shmstore.home.model.BonusModel
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.ui.AuthActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.SMSwipeRefreshLayout
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.layout_bonus_list.*


/**
 * 红包列表
 * Created by zhangye on 2018/4/19.
 */
class BonusListActivity : BaseActivity() {
    var currentPage = "1"
    var type = 1
    var title: String? = null
    var adapter: BonusListAdapter? = null
    var selectIndex = -2
    var mList = ArrayList<Bonus2>()

    override fun setLayout(): Int = R.layout.layout_bonus_list

    override fun initView() {
        title = intent.getStringExtra("title")
        title?.let {
            titleManager.defaultTitle(it)
            if (it == "大众红包") {
                type = 1

                tvSend.visibility = View.VISIBLE
            }

            if (it == "精准红包") {
                type = 2
            }

            if (it == "粉丝红包") {
                type = 3
            }

            if (it == "媒体扶贫") {
                type = 4
            }

            if (it == "消费扶贫") {
                type = 5
            }
        }
        refreshLayout.recyclerView.layoutManager = LinearLayoutManager(this)

        refreshLayout.recyclerView.addItemDecoration(RecycleViewDivider(this,
                LinearLayoutManager.VERTICAL,
                resources.getDimension(R.dimen.h42).toInt(),
                ContextCompat.getColor(this, R.color.white)))
        tvSend.setOnClickListener(this)

        refreshLayout.onRefreshResult = object : SMSwipeRefreshLayout.OnRefreshResult {
            override fun onUpdate(page: Int) {
                currentPage = page.toString()
                loadData()
            }

            override fun onLoadMore(page: Int) {
                currentPage = mList.last().no
                loadData()
            }

        }

        adapter = BonusListAdapter(mList)

        loadData()

    }


    override fun onResume() {
        super.onResume()
        SmApplication.getApp().getData<BonusAction>(DataCode.BONUS, true)?.let {
            refreshLayout.onRefreshResult?.onUpdate(1)
        }
    }


    override fun onListener(view: View) {
        when (view.id) {
            tvSend.id -> {
                if (UserManager2.isLogin()) {
                    startActivity(
                            Intent(this, SendBonusActivity::class.java)
                                    .putExtra("type", type.toString()))
                } else {
                    SmediaDialog(this).showLogin()
                }
            }
        }
    }

    override fun reTryGetData() {
        isShowLoading = true
        hideReTryLayout()
        loadData()
    }

    private fun showDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("请先成为新媒人")
        dialog.setDesc("只有新媒人才能发红包，前往新媒人认\n" +
                "证！")
        dialog.singleButton()
        dialog.setPositiveText("下一步")
        dialog.OnClickListener = View.OnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        dialog.show()
    }


    fun loadData() {
        val params = HashMap<String, String>()
        params["type"] = type.toString()
        if (currentPage != "1") {
            params["no"] = currentPage
        }
        params["num"] = "20"

        ApiManager2.post(1, this, params, Constant.RP_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Bonus2>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Bonus2>>) {

                data.message?.let {
                    if (currentPage == "1") {
                        mList.clear()
                    }
                    mList.addAll(it)
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ArrayList<Bonus2>>) {
            }

        })

    }

//    private fun loadData(boolean: Boolean) {
//        val params = hashMapOf<String, String>()
//        if (UserManager.isLogin()){
//            if(SmApplication.getApp().getData<Location>(DataCode.LOCATION, false)?.let {
//                        params["reqUserLocationVO.lat"] = it.latitude
//                        params["reqUserLocationVO.lng"] = it.longitude
//                        params["reqUserLocationVO.locationName"] = it.city
//                        params["reqUserLocationVO.countyName"] = it.district
//                    } == null){
//                if (type == 1){
//                    SmediaDialog(this).showLocationError() //定位信息错误弹窗
//                }
//            }
//        }
//        params["currentPage"] = currentPage.toString()
//        params["showCount"] = refreshLayout.pageCount.toString()
//        params["type"] = type.toString()
//        ApiManager.get(1, this, params, Constant.BONUS_LIST, object : ApiManager.OnResult<BaseModel<BonusModel>>() {
//            override fun onSuccess(data: BaseModel<BonusModel>) {
//                if (data.success) {
//
//                    data.entity?.let {
//                        isShowLoading = false
//                        if (boolean) {
//                            BonusListAdapter(it.list).let {
//                                adapter = it
//                                refreshLayout.recyclerView.adapter = adapter
//                                it.setOnItemClickListener(object : OnItemClickListener {
//                                    override fun onItemClick(v: View, index: Int) {
//                                        if (type == 1 || type ==2 || type ==3 ){
//                                            if (SmApplication.getApp().getData<Location>(DataCode.LOCATION,false) == null){
//                                                SmediaDialog(this@BonusListActivity).showLocationError()
//                                                return
//                                            }
//                                            it.getData(index).let {
//                                                //精准红包和粉丝红包需要完善资料
//                                                if (it.typeInfo == 2 || it.typeInfo == 3) {
//                                                    UserManager.getArchivalCompletion()?.let {
//                                                        if (it.toFloat() < 0.22) {
//                                                            SmediaDialog(this@BonusListActivity).showArchive()
//                                                            return
//                                                        }
//                                                    }
//                                                }
//
//
//                                                when {
//                                                    it.redPacketStatus == 1 -> {
//                                                        intentWeb(it, "RECEIVE")
//                                                    }
//                                                    it.redPacketStatus == 2 -> {
//                                                        intentWeb(it, "NONE")
//                                                    }
//                                                    else -> {
//                                                        hitBonus(it)
//                                                    }
//                                                }
//                                            }
//                                        }else{
//                                            if (UserManager.isLogin()){
//                                                //扶贫需要绑定微信
//                                                if (type == 4 || type ==5){
//                                                    checkWx(it.getData(index),"")
//                                                }
//                                            }else{
//                                                SmediaDialog(this@BonusListActivity).showLogin()
//                                            }
//                                        }
//                                    }
//                                })
//                                refreshLayout.update(it.list)
//                            }
//                        } else {
//                            refreshLayout.loadMore(it.list)
//                        }
//                    }
//                }else{
//                    ToastUtil.show(data.message)
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//
//            }
//
//            override fun onCatch(data: BaseModel<BonusModel>) {
//
//            }
//
//        })
//    }

    /**
     * 抢红包（占位）
     */
    fun hitBonus(bonus: BonusModel.Data) {
        if (!UserManager2.isLogin()) {
            SmediaDialog(this).showLogin()
            return
        }

        isShowLoading = true
        val params = hashMapOf<String, String>()
        params["redPacketOrderId"] = bonus.redPacketOrderId.toString()
        ApiManager.post(this, params, Constant.BONUS_HIT, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success) {
//                    抢红包 RECEIVE("已领取"),SEIZE("已占位"),NONE("已抢光")
                    data.entity?.let {
                        intentWeb(bonus, it)
                    }

                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<String>) {

            }
        })
    }

    fun intentWeb(it: BonusModel.Data, state: String) {
        val intentWeb = Intent(this@BonusListActivity, BonusWebActivity::class.java)
        intentWeb.putExtra("comment_id", it.redPacketOrderId.toString())
        intentWeb.putExtra("title", it.redPacketName)
        intentWeb.putExtra("htmUrl", it.htmlUrl)
        intentWeb.putExtra("downUrl", it.androidUrl)
        intentWeb.putExtra("state", state)
        intentWeb.putExtra("typeInfo", type.toString())
        intentWeb.putExtra("companyLogo", it.companyLogo)
        intentWeb.putExtra("companyName", it.companyName)
        intentWeb.putExtra("advertisementBaseType", it.advertisementBaseType)
        startActivity(intentWeb)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initView()
    }

    //检查是否绑定过微信
    fun checkWx(it: BonusModel.Data, state: String) {
        ApiManager.get(this, null, Constant.CHECK_WX, object : ApiManager.OnResult<BaseModel<Boolean>>() {
            override fun onSuccess(data: BaseModel<Boolean>) {
                if (data.entity == true) {
                    intentWeb(it, state)
                } else {
                    ToastUtil.show("请在登录页面进行微信绑定")
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<Boolean>) {

            }
        })

    }
}