package com.cocosh.shmstore.home

import android.content.Intent
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.View
import android.widget.LinearLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.utils.PermissionCode
import com.cocosh.shmstore.utils.PermissionUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.ShareDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.cocosh.shmstore.zxing.QrCodeActivity
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.include_menu.*

/**
 * 首页
 * Created by zhangye on 2018/1/26.
 */
class HomeActivity : BaseActivity() {
    lateinit var homeFragment: HomeFragment
    var mineFragment: MineFragment? = null
    override fun reTryGetData() {
    }

    var permissionUtil = PermissionUtil(this)
    private val fragments = arrayListOf<Fragment>()
    private var currentPage = -1
    var position = 0
    override fun setLayout(): Int = R.layout.activity_home

    override fun initView() {
        isShowLoading = false
        rlHome.setOnClickListener(this)
        rlInCome.setOnClickListener(this)
        rlShoumei.setOnClickListener(this)
        rlMessage.setOnClickListener(this)
        rlMine.setOnClickListener(this)

        iv_home.boldText()
        iv_income.boldText()
        iv_message.boldText()
        iv_mine.boldText()

        //初始化所有标题
        titleManager.goneTitle()


        //初始化所有页面
        homeFragment = HomeFragment()
        fragments.add(homeFragment)

        val talkFragment = IncomeFragment()
        fragments.add(talkFragment)

        val shoumeiFragment = ShoumeiFragment()
        fragments.add(shoumeiFragment)

        val messageFragment = MessageFragment()
        fragments.add(messageFragment)

        mineFragment = MineFragment()
        fragments.add(mineFragment!!)
        selectTab(0)
        getMsgCount()
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.rlHome -> {
                position = 0
                selectTab(position)
            }
            R.id.rlInCome -> {
                if (!UserManager2.isLogin()) {
                    SmediaDialog(this).showLogin()
                    return
                }
                position = 1
                selectTab(position)
            }

            R.id.rlShoumei -> {
                if (!UserManager2.isLogin()) {
                    SmediaDialog(this).showLogin()
                    return
                }
                position = 2
                selectTab(position)
            }

            R.id.rlMessage -> {
                if (!UserManager2.isLogin()) {
                    SmediaDialog(this).showLogin()
                    return
                }
                position = 3
                selectTab(position)
            }
            R.id.rlMine -> {
                position = 4
                selectTab(position)
            }

        }
    }

    override fun onListener(view: View) {
    }

    private fun selectTab(index: Int) {
        updateMenu(index)
        if (currentPage != -1) {
            supportFragmentManager.beginTransaction().hide(fragments[currentPage]).commit()
        }

        if (supportFragmentManager.findFragmentByTag(fragments[index].javaClass.simpleName) == null) {
            supportFragmentManager.beginTransaction().add(flBottomTabFragmentContainer.id, fragments[index], fragments[index].javaClass.simpleName).commit()
        } else {
            supportFragmentManager.beginTransaction().show(fragments[index]).commit()
        }

        currentPage = index

    }

    private fun updateMenu(index: Int) {
        for (i in 0 until (llgroup as LinearLayout).childCount) {
            (llgroup as LinearLayout).getChildAt(i).isSelected = i == index
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCode.CAMERA.type) {
            if (permissionUtil.checkPermission(permissions)) {
                startActivity(Intent(this, QrCodeActivity::class.java))
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this)?.onActivityResult(requestCode, resultCode, data)//完成回调
    }

    /**
     * 显示分享APP对话框
     */
    fun showShareDialg(url: String) {
        val shareDialog = ShareDialog(this)
        shareDialog.showShareApp(url)
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

    private fun getMsgCount() {
//        val map = HashMap<String, String>()
//        ApiManager.get(0, this, map, Constant.MSG_UNREAD, object : ApiManager.OnResult<BaseModel<MsgUnReadCount>>() {
//            override fun onSuccess(data: BaseModel<MsgUnReadCount>) {
//                if (data.success && data.code == 200) {
//                    //更改消息按钮
//                    if (data.entity?.unreadRedPacketMessageCount == 0 && data.entity?.unreadReplyMessageCount == 0 && data.entity?.unreadSystemMessageCount == 0) {
//                        //隐藏
//                        isShowMsgPoint(false)
//                    } else {
//                        //显示
//                        isShowMsgPoint(true)
//                    }
//                } else {
//
//                }
//            }
//
//            override fun onFailed(e: Throwable) {
//
//            }
//
//            override fun onCatch(data: BaseModel<MsgUnReadCount>) {
//
//            }
//        })
    }

    fun isShowMsgPoint(isShow: Boolean) {
        if (isShow) {
            viewMsgPoint.visibility = View.VISIBLE
        } else {
            viewMsgPoint.visibility = View.GONE
        }
    }
}