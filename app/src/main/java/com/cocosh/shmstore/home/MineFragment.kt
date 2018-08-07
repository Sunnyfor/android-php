package com.cocosh.shmstore.home

import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.mine.adapter.MineBottomNavAdapter
import com.cocosh.shmstore.mine.adapter.MineTopNavAdapter
import com.cocosh.shmstore.mine.model.AuthenStatus
import com.cocosh.shmstore.mine.model.MemberEntrance2
import com.cocosh.shmstore.mine.model.MineTopNavEntity
import com.cocosh.shmstore.mine.ui.*
import com.cocosh.shmstore.mine.ui.authentication.CertificationInComeActivity
import com.cocosh.shmstore.mine.ui.authentication.FacilitatorInComeActivity
import com.cocosh.shmstore.mine.ui.authentication.SendPackageActivity
import com.cocosh.shmstore.mine.ui.enterprisewallet.EnterPriseWalletActivity
import com.cocosh.shmstore.mine.ui.mywallet.MyWalletActivity
import com.cocosh.shmstore.term.ServiceTermActivity
import com.cocosh.shmstore.utils.*
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.fragment_mine.view.*
import kotlinx.android.synthetic.main.layout_top_head.view.*


class MineFragment : BaseFragment(), OnItemClickListener {
    var bitmap_topbg: Bitmap? = null
    private lateinit var topTitles: ArrayList<MineTopNavEntity>
    private lateinit var bottomTitles: ArrayList<MineTopNavEntity>

    override fun reTryGetData() {

    }

    override fun setLayout(): Int = R.layout.fragment_mine

    override fun initView() {

        showTitle(getTitleManager().mineTitle(), true)

        topTitles = arrayListOf(
                MineTopNavEntity(resources.getString(R.string.iconMineCollection), "收藏"),
                MineTopNavEntity(resources.getString(R.string.iconMineWatch), "关注"),
//                MineTopNavEntity(resources.getString(R.string.iconMineFan), "粉丝")),
                MineTopNavEntity(resources.getString(R.string.iconMineHome), "档案"))

        bottomTitles = arrayListOf(
                MineTopNavEntity(resources.getString(R.string.iconMineAuthen), "认证"),
                MineTopNavEntity(resources.getString(R.string.iconMinePurse), "钱包"),
//                MineTopNavEntity(resources.getString(R.string.iconMineOrder), "订单"),
                MineTopNavEntity(resources.getString(R.string.iconMineOrder), "发出的红包"),
                MineTopNavEntity(resources.getString(R.string.iconMineHelp), "帮助中心"),
                MineTopNavEntity(resources.getString(R.string.iconAddress), "地址管理")
        )

        getLayoutView().recycleTopNav.layoutManager = GridLayoutManager(context, topTitles.size)
        getLayoutView().recycleTopNav.setHasFixedSize(true)
        getLayoutView().recycleTopNav.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, resources.getDimension(R.dimen.h3).toInt(), ContextCompat.getColor(context, R.color.lineGray)))
        val topNavAdapter = MineTopNavAdapter(topTitles)
        getLayoutView().recycleTopNav.adapter = topNavAdapter
        topNavAdapter.setOnItemClickListener(this)

        getLayoutView().recycleBottomNav.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        getLayoutView().recycleBottomNav.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, resources.getDimension(R.dimen.h3).toInt(), ContextCompat.getColor(context, R.color.lineGray)))

        getLayoutView().ivHead.setOnClickListener(this)

        defaultMenu() //加载默认菜单
        loadDate() //发起网络请求
    }

    override fun onListener(view: View) {

        if (!UserManager2.isLogin()) {
            SmediaDialog(activity).showLogin()
            return
        }

        when (view.id) {
            getLayoutView().ivHead.id -> startActivity(Intent(context, ArchiveActivity::class.java))
        }
    }


    override fun onItemClick(v: View, index: Int) {
        if (!UserManager2.isLogin()) {
            SmediaDialog(activity).showLogin()
            return
        }

        when (v.tag.toString()) {
            "档案" -> startActivity(Intent(activity, ArchiveActivity::class.java))
            "认证" -> AuthActivity.start(activity)
            "钱包" -> MyWalletActivity.start(activity)
            "邀请码" -> InviteCodeActivity.start(activity)
            "新媒人" -> CertificationInComeActivity.start(activity)
            "服务商" -> FacilitatorInComeActivity.start(activity)
            "服务商钱包" -> EnterPriseWalletActivity.start(activity)
            "地址管理" -> AddressMangerActivity.start(activity)
            "关注" -> FollowActivity.start(activity)
            "订单" -> OrderListActivity.start(activity)
            "收藏" -> CollectionActivity.start(activity)
            "帮助中心" -> {
                val intent = Intent(context, ServiceTermActivity::class.java).apply {
                    putExtra("title", "帮助中心")
                    putExtra("OPEN_TYPE", OpenType.Help.name)
                }
                startActivity(intent)
//              startActivity(Intent(context, HelpActivity::class.java))
            }
            "发出的红包" -> SendPackageActivity.start(activity)
        }
    }


    /**
     * 加载数据
     */
    fun loadDate() {
        if (!UserManager2.isLogin()) {
            UserManager2.loadBg(null, getLayoutView().ivBg)
            getLayoutView().tvName.text = "未登录"
            getLayoutView().ivHead.setImageResource(R.drawable.bg_update_head)
            getLayoutView().tvNo.visibility = View.GONE
            defaultMenu()
            return
        }


        UserManager2.loadMemberEntrance(getBaseActivity(), object : ApiManager2.OnResult<BaseBean<MemberEntrance2>>() {
            override fun onSuccess(data: BaseBean<MemberEntrance2>) {
                data.message?.let {
                    UserManager2.setMemberEntrance(it)
                    updateInfo(it)
                }
            }

            override fun onFailed(code: String, message: String) {

            }

            override fun onCatch(data: BaseBean<MemberEntrance2>) {

            }
        })

    }

    private fun defaultMenu() {
        val bottomNavAdapter = MineBottomNavAdapter(bottomTitles)
        getLayoutView().recycleBottomNav.adapter = bottomNavAdapter
        bottomNavAdapter.setOnItemClickListener(this)
    }

    /**
     * 根据认证状态修改菜单顺序
     */
    private fun motifyMenu(cityOpertorsStatus: String, partnerStatus: String) {
        if (cityOpertorsStatus == "" && partnerStatus == "") {
            return
        }
        val newBottomTitles = arrayListOf(
                MineTopNavEntity(resources.getString(R.string.iconMinePurse), "钱包"),
//                MineTopNavEntity(resources.getString(R.string.iconMineOrder), "订单"),
                MineTopNavEntity(resources.getString(R.string.iconMineOrder), "发出的红包"),
                MineTopNavEntity(resources.getString(R.string.iconAddress), "地址管理"),
                MineTopNavEntity(resources.getString(R.string.iconMineAuthen), "认证"),
                MineTopNavEntity(resources.getString(R.string.iconMineHelp), "帮助中心"))

        if (cityOpertorsStatus == AuthenStatus.PRE_PASS.type) {
            newBottomTitles.add(0, MineTopNavEntity(resources.getString(R.string.iconServices), "服务商"))
            val index = newBottomTitles.indexOfFirst {
                it.title == "钱包"
            }
            newBottomTitles.add(index + 1, MineTopNavEntity(resources.getString(R.string.iconServices), "服务商钱包"))
        }

        if (partnerStatus == AuthenStatus.PRE_PASS.type) {
            newBottomTitles.add(0, MineTopNavEntity(resources.getString(R.string.iconCertification), "新媒人"))
        }

        if (cityOpertorsStatus == AuthenStatus.PRE_PASS.type || partnerStatus == AuthenStatus.PRE_PASS.type) {
            newBottomTitles.add(0, MineTopNavEntity(resources.getString(R.string.iconInvite), "邀请码"))
        }

        val bottomNavAdapter = MineBottomNavAdapter(newBottomTitles)
        getLayoutView().recycleBottomNav.adapter = bottomNavAdapter
        bottomNavAdapter.setOnItemClickListener(this)
    }


    override fun close() {
    }

    /**
     * 设置首媒号码
     */
    private fun setNo(no: String?) {
        getLayoutView().tvNo.text = (getString(R.string.no) + no)
    }

    override fun onResume() {
        super.onResume()
        loadDate()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            loadDate()
        }
    }

    private fun updateInfo(memberEntrance: MemberEntrance2) {
        setNo(UserManager2.getLogin()?.code)
        getLayoutView().tvName.text = memberEntrance.nickname
        UserManager.loadBg(memberEntrance.avatar, getLayoutView().ivBg) //加载背景图

        memberEntrance.avatar.let {
            if (it != "") {
                GlideUtils.loadHead(context, it, getLayoutView().ivHead)
            } else {
                getLayoutView().ivHead.setImageResource(R.drawable.bg_update_head)
            }
//            motifyMenu(it.cityOpertorsStatus ?: "", it.partnerStatus ?: "")
            getLayoutView().tvNo.visibility = View.VISIBLE

        }

    }
}