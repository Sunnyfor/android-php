package com.cocosh.shmstore.home

import android.content.Intent
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.home.model.BonusOpen
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.widget.dialog.ShareDialog
import kotlinx.android.synthetic.main.activity_bonus_open.*

/**
 * 红包开启页面
 * Created by zhangye on 2018/4/24.
 */
class BonusOpenActivity : BaseActivity() {

    private var bonusOpen: BonusOpen? = null

    override fun setLayout(): Int = R.layout.activity_bonus_open

    override fun initView() {

        titleManager.immersionTitle()

        titleManager.rightText("红包", "完成", View.OnClickListener {
            finish()
        }, true).setColor(R.color.white)

        ivRanking.setOnClickListener(this)
        btnShare.setOnClickListener(this)

        bonusOpen = intent.getSerializableExtra(DataCode.BONUS_MONEY) as BonusOpen?

        bonusOpen?.let {
            tvMoney.text = (it.money + " 元")

        }
    }

    override fun onListener(view: View) {
        when (view.id) {
            ivRanking.id -> {
                val intent = Intent(this, BonusRankingActivity::class.java)
                intent.putExtra("id",this.intent.getStringExtra("id"))
                startActivity(intent)
            }
            btnShare.id -> {
                bonusOpen?.url?.let {
                    ShareDialog(this).showShareBonus(it)
                }
            }
        }
    }

    override fun reTryGetData() {

    }

}