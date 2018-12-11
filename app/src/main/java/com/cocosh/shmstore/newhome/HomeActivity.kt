package com.cocosh.shmstore.newhome

import android.content.Intent
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.View
import android.widget.LinearLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.home.MineFragment
import com.cocosh.shmstore.home.NewHomeFragment
import com.cocosh.shmstore.home.ShoumeiFragment
import com.cocosh.shmstore.login.ui.activity.LoginActivity
import com.cocosh.shmstore.mine.ui.LoginBusinessHelpActivity
import com.cocosh.shmstore.newhome.fragment.GoodsClazzFragment
import com.cocosh.shmstore.newhome.fragment.ShoppingFragment
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.CertificationDialog
import kotlinx.android.synthetic.main.activity_new_home.*
import kotlinx.android.synthetic.main.include_new_menu.*

class HomeActivity : BaseActivity() {
    var position = 0

    private var fragments = ArrayList<Fragment>()

    private val homeFragment: NewHomeFragment by lazy {
        NewHomeFragment()
    }

    private val goodsClazzFragment: GoodsClazzFragment by lazy {
        GoodsClazzFragment()
    }

    private val shoumeiFragment: ShoumeiFragment by lazy {
        ShoumeiFragment()
    }

    private val shoppingFragment: ShoppingFragment by lazy {
        ShoppingFragment()
    }

    private val mineFragment: MineFragment by lazy {
        MineFragment()
    }

    override fun setLayout(): Int = R.layout.activity_new_home

    override fun initView() {
        //初始化所有标题
        titleManager.goneTitle()

        fragments.add(homeFragment)
        fragments.add(goodsClazzFragment)
        fragments.add(shoumeiFragment)
        fragments.add(shoppingFragment)
        fragments.add(mineFragment)

        rlHome.setOnClickListener(this)
        rlInCome.setOnClickListener(this)
        rlShoumei.setOnClickListener(this)
        rlMessage.setOnClickListener(this)
        rlMine.setOnClickListener(this)
        showFragment(homeFragment)
    }

    override fun onClick(view: View) {
        when (view.id) {
            rlHome.id -> {
                showFragment(homeFragment)
            }
            rlInCome.id -> {
                showFragment(goodsClazzFragment)
            }
            rlShoumei.id -> {
                if (!isLogin()){
                    return
                }
                showFragment(shoumeiFragment)
            }
            rlMessage.id -> {
                if (!isLogin()){
                    return
                }
                showFragment(shoppingFragment)
            }
            rlMine.id -> {
                showFragment(mineFragment)
            }
        }

    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }


    private fun updateMenu(index: Int) {
        for (i in 0 until (llParent as LinearLayout).childCount) {
            (llParent as LinearLayout).getChildAt(i).isSelected = i == index
        }
    }

    private fun showFragment(fragment: BaseFragment) {

        var position = 0

        fragments.forEachIndexed { index, it ->
            if (it == fragment) {
                position = index
            }
            if (it.isAdded) {
                supportFragmentManager.beginTransaction().hide(it).commit()
            }
        }

        if (fragment.isAdded) {
            supportFragmentManager.beginTransaction().show(fragment).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.flBottomTabFragmentContainer, fragment).commit()
        }
        updateMenu(position)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getStringExtra("type")?.let {
            if (it == "Login") {
                autEnt()
//                couponIndex()
            }

            if (it == "Forum") {
                rlShoumei.performClick()
                shoumeiFragment.selectPage(1)
            }
        }
    }

    fun isLogin():Boolean{
        if (!UserManager2.isLogin()){
            startActivity(Intent(this,LoginActivity::class.java))
            return false
        }
        return true
    }

    /**
     * 认证引导弹窗
     */
    private fun autEnt() {
        UserManager2.getLogin()?.invitee?.let {
            val mDialog = CertificationDialog(this)
            mDialog.show(it)
        }
    }



    override fun onBackPressed() {
        if (doubleClick()) {
            super.onBackPressed()
            System.exit(0)
        }
    }


    private var mHits = LongArray(2)
    private fun doubleClick(): Boolean {
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()
        if (mHits[0] >= SystemClock.uptimeMillis() - 800) {
            return true
        } else {
            ToastUtil.show("再按一次退出程序")
        }
        return false
    }
}