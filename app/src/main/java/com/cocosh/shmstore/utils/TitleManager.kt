package com.cocosh.shmstore.utils

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseFragment
import com.cocosh.shmstore.mine.ui.SettingActivity
import com.cocosh.shmstore.title.DefaultTitleFragment
import com.cocosh.shmstore.title.HomeTitleFragment
import com.cocosh.shmstore.title.LeftRightTitleFragment
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_base.*

/**
 * 标题管理类
 * Created by 张野 on 2017/10/13.
 */
class TitleManager(private val activity: BaseActivity) {

    private var isFragment = false

    fun goneTitle() {
        activity.frameTitle.visibility = View.GONE
        activity.vTitleLine.visibility = View.GONE
        isFragment = true
    }

    //沉浸式Title
    fun immersionTitle() {
        activity.vTitleLine.visibility = View.GONE
        (activity.frameParent.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.BELOW, 0)
        (activity.frameLoading.layoutParams as FrameLayout.LayoutParams).topMargin = R.dimen.titleHeigit
        activity.frameTitle.setBackgroundResource(R.color.transparent)
    }


    fun homeTitle(): BaseFragment {
        val fragment = HomeTitleFragment()
        addTitleFragment(fragment)
        return fragment
    }

    //默认标题样式
    fun defaultTitle(title: String): DefaultTitleFragment = defaultTitle(title, null, 0, 0, null)


    /**
     * 默认标题样式
     * @iconStr 图标代码
     * @leftMargin 左边图标边距
     * @color 图标颜色
     * @listener 修改图标点击事件
     */
    fun defaultTitle(title: String, iconStr: String?, leftMargin: Int, color: Int, listener: View.OnClickListener?): DefaultTitleFragment {
        val fragment = DefaultTitleFragment()
        fragment.title = title
        fragment.leftIcon(iconStr)
        fragment.leftColor(color)
        fragment.leftMargin = leftMargin
        fragment.setLeftOnClickListener(listener)
        addTitleFragment(fragment)
        return fragment
    }


    //纯文本标题样式
    fun textTitle(title: String): DefaultTitleFragment {
        val fragment = defaultTitle(title)
        fragment.singleText()
        return fragment
    }


    //我的页面单独样式
    fun mineTitle(): LeftRightTitleFragment {
        val fragment = LeftRightTitleFragment()
        fragment.title("我的")
        fragment.setColor(R.color.white)
        fragment.goneLeft()
        fragment.rightIcon(activity.resources.getString(R.string.iconSetting))
        fragment.setRightOnClickListener(View.OnClickListener {

            if (!UserManager2.isLogin()) {
                SmediaDialog(activity).showLogin()
                return@OnClickListener
            }
            activity.startActivity(Intent(activity, SettingActivity::class.java))

        })
        addTitleFragment(fragment)
        return fragment
    }

    //右边为文字样式
    fun rightText(title: String, name: String, onClickListener: View.OnClickListener, isGoneLeft: Boolean): LeftRightTitleFragment {
        val fragment = LeftRightTitleFragment()

        if (isGoneLeft) {
            fragment.goneLeft()
        }
        fragment.title(title)
        fragment.rightIcon(name)
        fragment.setRightOnClickListener(View.OnClickListener {
            onClickListener.onClick(it)
        })
        addTitleFragment(fragment)

        return fragment
    }

    //右边为文字样式
    fun rightText(title: String, name: String, onClickListener: View.OnClickListener, isGoneLeft: Boolean, rightTextSize: Float): LeftRightTitleFragment {
        val fragment = LeftRightTitleFragment()

        if (isGoneLeft) {
            fragment.goneLeft()
        }
        fragment.title(title)
        fragment.rightIcon(name)
        fragment.rightTextSize(rightTextSize)
        fragment.setRightOnClickListener(View.OnClickListener {
            onClickListener.onClick(it)
        })
        addTitleFragment(fragment)

        return fragment
    }

    fun rightText(title: String, name: String, onClickListener: View.OnClickListener): LeftRightTitleFragment =
            rightText(title, name, onClickListener, false)


    //钱包title
    fun facTitle(title: String, name: String): LeftRightTitleFragment {
        val fragment = LeftRightTitleFragment()
        fragment.title(title)
        fragment.setColor(R.color.blackText)
        fragment.leftIcon(activity.getString(R.string.iconBack))
        fragment.rightIcon(name)
        fragment.rightTextSize(activity.resources.getDimension(R.dimen.h40))
        fragment.setLeftOnClickListener(View.OnClickListener {
            activity.finish()
        })
        addTitleFragment(fragment)
        return fragment
    }

    //客服单独样式
    fun facTitle(): LeftRightTitleFragment {
        val fragment = LeftRightTitleFragment()
        fragment.title("支付加盟费")
        fragment.setColor(R.color.blackText)
        fragment.leftIcon(activity.getString(R.string.iconBack))
        fragment.rightIcon("客服")
        fragment.setLeftOnClickListener(View.OnClickListener {
            activity.finish()
        })
        addTitleFragment(fragment)
        return fragment
    }


    fun addTitleFragment(fragment: BaseFragment) {
        if (!isFragment) {
            activity.supportFragmentManager.beginTransaction().add(activity.frameTitle.id, fragment).commit()
        }
    }
}