package com.cocosh.shmstore.mine.ui.mywallet

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.R
import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.mine.adapter.BankMangerListAdapter
import com.cocosh.shmstore.mine.adapter.SpaceVItem
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.BankModel
import com.cocosh.shmstore.mine.model.PayPassworType
import com.cocosh.shmstore.mine.presenter.BankListPresenter
import com.cocosh.shmstore.mine.presenter.IsSetPwdPresenter
import com.cocosh.shmstore.mine.ui.CheckPayPwdMessage
import com.cocosh.shmstore.sms.type.SMSType
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager2
import com.cocosh.shmstore.widget.dialog.BankRemoveDialog
import com.cocosh.shmstore.widget.dialog.SercurityDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import kotlinx.android.synthetic.main.activity_bankcard_manger.*

/**
 *
 * Created by lmg on 2018/4/13.
 */
class BankCardMangerActivity : BaseActivity(), MineContrat.IBankListView {
    var mPresenter = BankListPresenter(this, this)
    var listData = ArrayList<BankModel>()
    var position = 0
    var isSet: Boolean? = null

    override fun bankListData(result: BaseBean<ArrayList<BankModel>>) {
        listData.clear()
        result.message?.let {
            listData.addAll(it)
        }
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun deleteBank(result: BaseBean<String>) {
        mDialog?.getResult(result)
    }

    override fun wouldDeleteBankData(result: BaseModel<Boolean>) {
        if (result.success && result.code == 200) {
            if (result.entity!!) {
                showRemoveDialog(listData[position].bank_logo, listData[position].bank_name, listData[position].bank_kind, listData[position].card_no)
            } else {
                showErrorDialog(result.message)
            }
        } else {
            showErrorDialog(result.message)
        }
    }

    override fun initView() {
        titleManager.defaultTitle("银行卡管理")
        addBankCard.setOnClickListener(this)
        initList()
    }

    private fun initList() {
        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?

        // 设置监听器。
        recyclerView.setSwipeMenuCreator { _, swipeRightMenu, viewType ->
            if (viewType == 1) {
                val width = resources.getDimension(R.dimen.w250)
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                run {
                    val setItem = SwipeMenuItem(this@BankCardMangerActivity)
                            .setBackground(R.drawable.shape_bg_round_right_red)
                            .setText("解绑")
                            .setTextColor(Color.WHITE)
                            .setWidth(width.toInt())
                            .setHeight(height)
                    swipeRightMenu?.addMenuItem(setItem) // 添加菜单到右侧。
                }
            }
        }
        // 菜单点击监听。
        recyclerView.setSwipeMenuItemClickListener {
            val position = it.adapterPosition
            if (position != listData.size) {
                this.position = position
//                mPresenter.requestWouldDeleteBankData(0)
                showRemoveDialog(listData[position].bank_logo, listData[position].bank_name, listData[position].bank_kind, listData[position].card_no)
            }

        }

        recyclerView.isLongPressDragEnabled = true
        recyclerView.setSwipeItemLongClickListener { itemView, position ->
            if (position != listData.size) {
                this.position = position
//                mPresenter.requestWouldDeleteBankData(0)
                showRemoveDialog(listData[position].bank_logo, listData[position].bank_name, listData[position].bank_kind, listData[position].card_no)
            }
        }

        recyclerView.addItemDecoration(SpaceVItem(resources.getDimension(R.dimen.h35).toInt(), resources.getDimension(R.dimen.h35).toInt()))
        recyclerView.adapter = BankMangerListAdapter(this, listData)

    }

    override fun onListener(view: View) {
        when (view.id) {
            addBankCard.id -> {
                //判断是否实设置支付密码
                if (UserManager2.getCommonData()?.paypass == 1) {
                    AddBankCardActivity.start(this)
                    return
                }
                //实人认证
                showEntDialog()
            }
            else -> {
            }
        }
    }

    override fun reTryGetData() {
        mPresenter.requestBankListData(1)
//        mPwdPresenter.requestIsSetPwdData(1)
    }

    override fun setLayout(): Int = R.layout.activity_bankcard_manger

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, BankCardMangerActivity::class.java))
        }
    }

    private fun showRemoveDialog(pic: String?, name: String?, type: String?, num: String?) {
        val dialog = BankRemoveDialog(this)
        dialog.setData(this, pic, name, num)
        dialog.show()
        dialog.OnClickListener = View.OnClickListener {
            //确认
            SmApplication.getApp().isDelete = true
            showImputPsdDialog()
            dialog.dismiss()
        }
    }

    var mDialog: SercurityDialog<String>? = null
    private fun showImputPsdDialog() {
        mDialog = SercurityDialog(this, R.style.SercurityDialogTheme)
        mDialog?.show()
        mDialog?.setOnInputCompleteListener(object : SercurityDialog.InputCompleteListener<String> {
            override fun inputComplete(pwd: String) {
                mPresenter.requestDeleteBankData(listData[position].id ?: "", pwd)
            }

            override fun result(boolean: Boolean, data: String?) {
                if (boolean) {

                    //解绑成功 跳转解绑结果页
                    RemovedResultActivity.start(this@BankCardMangerActivity, listData[position].bank_logo
                            ?: "", listData[position].bank_name ?: "", listData[position].card_no
                            ?: "", true)
                } else {
                    //解绑失败 跳转解绑结果页
                    RemovedResultActivity.start(this@BankCardMangerActivity, false)
                }
            }
        })
    }

    private fun showErrorDialog(str: String?) {
        val dialog = SmediaDialog(this)
        dialog.setTitle(str)
        dialog.setPositiveText("知道了")
        dialog.singleButton()
        dialog.show()
    }


    private fun showEntDialog() {
        val dialog = SmediaDialog(this)
        dialog.setTitle("您未设置过支付密码，设置前将验证您的身份，即将发送验证码到" + UserManager2.getCryptogramPhone())
        dialog.OnClickListener = View.OnClickListener {
            SmApplication.getApp().isDelete = false
            SmApplication.getApp().activityName = this@BankCardMangerActivity.javaClass
            CheckPayPwdMessage.start(this@BankCardMangerActivity, SMSType.INIT_PAYPASS)
        }
        dialog.show()
    }

    override fun onResume() {
        mPresenter.requestBankListData(1)
//        mPwdPresenter.requestIsSetPwdData(1)
        super.onResume()
    }

    override fun onNewIntent(intent: Intent?) {
        setIntent(intent)
        mPresenter.requestBankListData(1)
//        mPwdPresenter.requestIsSetPwdData(1)
        super.onNewIntent(intent)
    }

    override fun onBackPressed() {
        mDialog?.clearNum()
        super.onBackPressed()
    }

    override fun onDestroy() {
        SmApplication.getApp().activityName = null
        SmApplication.getApp().isDelete = false
        super.onDestroy()
    }
}