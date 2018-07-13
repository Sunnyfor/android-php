package com.cocosh.shmstore.utils

import android.support.v4.app.Fragment
import com.cocosh.shmstore.base.BaseActivity

/**
 * 隐藏显示Framgnet的工具类
 * Created by zhangye on 2018/1/4.
 */
class FragmentUtil(private val baseActivity: BaseActivity, val id: Int) {
    var tag: String? = null

    fun loadFragment(fragment: Fragment?) {

        if (fragment == null) {//Fragment为null取消操作
            return
        }

        val tag = fragment.javaClass.simpleName

        if (tag == this.tag) {  //Tag相同取消操作
            return
        }


        if (this.tag != null) {
            hide(this.tag!!)
        }

        if (getFramgnet(tag) == null) {
            add(fragment, tag)
        } else {
            show(tag)
        }

        this.tag = tag
    }

    private fun getFramgnet(tag: String): Fragment? = baseActivity.supportFragmentManager.findFragmentByTag(tag)

    private fun show(tag: String) {
        baseActivity.supportFragmentManager.beginTransaction().show(getFramgnet(tag)).commit()
    }

    private fun hide(tag: String) {
        baseActivity.supportFragmentManager.beginTransaction().hide(getFramgnet(tag)).commit()
    }

    private fun add(fragment: Fragment, tag: String) {
        baseActivity.supportFragmentManager.beginTransaction().add(id, fragment, tag).commit()
    }
}