package com.cocosh.shmstore.mine.ui.authentication

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.home.model.SendBonus
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.adapter.SendListAdapter
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import kotlinx.android.synthetic.main.activity_send_packages.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 *
 * Created by lmg on 2018/4/20.
 */
class SendPackageActivity : BaseActivity() {
    private var job: Job? = null
    var allUpdateOk = false
    var paymentUpdateOk = false
    val fragmentList = arrayListOf<SendPackageFragment>()
    private var isDestory = false

//    var handler = Handler {
//        val message = it
//        val position = message.obj
//        fragmentList[it.what].let {
//            if (it.userVisibleHint) {
//                it.notifyDataSetChanged(position as Int)
//            }
//        }
//        return@Handler false
//    }


    override fun setLayout(): Int = R.layout.activity_send_packages
    override fun initView() {
        titleManager.defaultTitle("发出的红包")
        initData()
    }

    private fun initData() {
        //设置ViewPager里面也要显示的图片
        fragmentList.add(SendPackageFragment())
        fragmentList.add(SendPackageFragment())
        fragmentList.add(SendPackageFragment())
        fragmentList.add(SendPackageFragment())
        fragmentList.add(SendPackageFragment())

        //0 待付款 2审核中 5投放中 3被驳回
        fragmentList[1].type = "0"
        fragmentList[2].type = "2"
        fragmentList[3].type = "5"
        fragmentList[4].type = "3"

        //设置标题
        val titleList = arrayListOf<String>()
        titleList.add("全部")
        titleList.add("待付款")
        titleList.add("审核中")
        titleList.add("已投放")
        titleList.add("被驳回")


        val linearLayout = tab.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerPadding = resources.getDimension(R.dimen.h40).toInt()
        linearLayout.dividerDrawable = ContextCompat.getDrawable(this, R.drawable.shape_divider_gray)


        //设置tab的模式
        tab.tabMode = TabLayout.MODE_FIXED
        //添加tab选项卡
        titleList.forEach {
            tab.addTab(tab.newTab().setText(it))
        }

        //给ViewPager绑定Adapter
        viewPager.offscreenPageLimit = fragmentList.size
        viewPager.adapter = SendListAdapter(supportFragmentManager, fragmentList, titleList)
        //把TabLayout和ViewPager关联起来
        tab.setupWithViewPager(viewPager)


//        job = launch(CommonPool) {
//            while (!isDestory) {
//                delay(1000)
//
//                fragmentList[0].list
//                        .forEachIndexed { index, bonus ->
//                            if (bonus.orderStatus == "10" && bonus.endTimeStamp > 0) {
//                                bonus.endTimeStamp -= 1000
//                                val message = Message()
//                                message.what = 0
//                                message.obj = index
//                                handler.sendMessage(message)
//                            }
//
//                        }
//
//                fragmentList[1].list.forEachIndexed { index, bonus ->
//                    if (bonus.orderStatus == "10" && bonus.endTimeStamp > 0) {
//                        bonus.endTimeStamp -= 1000
//                        val message = Message()
//                        message.what = 1
//                        message.obj = index
//                        handler.sendMessage(message)
//                    }
//                }
//            }
//        }
    }

    override fun onListener(view: View) {

    }

    override fun reTryGetData() {

    }

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, SendPackageActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestory = true
        job?.cancel()
    }


    //取消投放
    fun cancleRelease(bonus: SendBonus) {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您确定取消投放吗？")
        dialog.OnClickListener = View.OnClickListener {
            val params = hashMapOf<String, String>()
            params["no"] = bonus.no ?: ""
            ApiManager2.post(this, params, Constant.SENDRP_CANCEL, object : ApiManager2.OnResult<BaseBean<String>>() {
                override fun onFailed(code: String, message: String) {

                }

                override fun onSuccess(data: BaseBean<String>) {
                    fragmentList[0].list.remove(bonus)
                    fragmentList[1].list.remove(bonus)
                    fragmentList[0].notifyDataSetChanged()
                    fragmentList[1].notifyDataSetChanged()
                }
                override fun onCatch(data: BaseBean<String>) {

                }
            })
        }
        dialog.show()
    }

    /**
     * 用于支付成功后全部列表是否刷新完成
     */
    fun allUpdateOk(allUpdateOk: Boolean) {
        this.allUpdateOk = allUpdateOk
        if (allUpdateOk && paymentUpdateOk) {
            SmApplication.getApp().removeData(DataCode.BONUS_PAY)
        }
    }

    /**
     * 用于支付成功后待支付列表是否刷新完成
     */
    fun paymentUpdateOk(paymentUpdateOk: Boolean) {
        this.paymentUpdateOk = paymentUpdateOk
        if (allUpdateOk && paymentUpdateOk) {
            SmApplication.getApp().removeData(DataCode.BONUS_PAY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentCode.IS_INPUT) {
            fragmentList[0].update()
            fragmentList[1].update()
        }
    }
}