package com.cocosh.shmstore.mine.ui

import android.content.Intent
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cocosh.shmstore.R
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.OnItemClickListener
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.listener.InterestingDataListener
import com.cocosh.shmstore.mine.adapter.InterestingBottomAdapter
import com.cocosh.shmstore.mine.adapter.InterestingTopAdapter
import com.cocosh.shmstore.mine.adapter.SpaceHItem
import com.cocosh.shmstore.mine.model.InterestModel
import com.cocosh.shmstore.utils.IntentCode
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.ToastUtil
import com.cocosh.shmstore.utils.UserManager
import com.cocosh.shmstore.widget.dialog.InterestingDialog
import com.cocosh.shmstore.widget.dialog.SmediaDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_archive_interesting.*
import org.json.JSONArray
import org.json.JSONObject

/**
 *
 * Created by lmg on 2018/4/12.
 */
class ArchiveInterestingActivity : BaseActivity() {
    private var copyTopData = arrayListOf<InterestModel.Data>()
    private var topData = arrayListOf<InterestModel.Data>()
    private var listData = arrayListOf<InterestModel.Data>()
    private lateinit var interestingTopAdapter: InterestingTopAdapter
    private lateinit var interestingBottomAdapter: InterestingBottomAdapter
    override fun setLayout(): Int = R.layout.activity_archive_interesting

    override fun initView() {
        titleManager.defaultTitle("兴趣爱好")
        titleManager.rightText("兴趣爱好", "完成", View.OnClickListener {
            updataData() //更新兴趣爱好

        }).setLeftOnClickListener(View.OnClickListener {
            var isMotify = false
            if (copyTopData.size == listData.filter { it.idUserDescribeGroup != "sm1168" }.size) {
                copyTopData.forEachIndexed { i: Int, data: InterestModel.Data ->
                    if (data.idUserDescribeGroup != "sm1168") {
                        if (listData[i].idUserDescribeGroup != data.idUserDescribeGroup) {
                            isMotify = true
                        }
                    }
                }
            } else {
                isMotify = true
            }

            if (isMotify) {
                val dialog = SmediaDialog(this)
                dialog.setTitle("是否要保存当前所选兴趣")
                dialog.setPositiveText("保存")
                dialog.setCancelText("不了")
                dialog.OnClickListener = View.OnClickListener {
                    updataData() //更新兴趣爱好
                }

                dialog.cancelOnClickListener = View.OnClickListener {
                    finish() //关闭页面
                }
                dialog.show()
            } else {
                finish()
            }
        })

        initList()
        loadMeData()
    }

    private fun initListener() {
        interestingTopAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View, index: Int) {
                if (topData[index].idUserDescribeGroup == "sm1168") {
                    val dialog = InterestingDialog(this@ArchiveInterestingActivity)
                    dialog.onSureClickResult = object : InterestingDialog.OnSureClickResult {
                        override fun onSureClick(value: String) {
                            if (value.isEmpty()) {
                                ToastUtil.show("自定义爱好不能为空!")
                                return
                            }
                            customInteresting(value)
                        }
                    }
                    dialog.show()
                }
            }
        })

        interestingTopAdapter.onDeleteResult = object : InterestingTopAdapter.OnDeleteResult {
            override fun onDeleteResult(data: InterestModel.Data) {
                interestingBottomAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun initList() {
        //top
        recyclerViewTop.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewTop.addItemDecoration(SpaceHItem(resources.getDimension(R.dimen.w48).toInt(), 0))
        //bottom
        recyclerViewBottom.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBottom.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = resources.getDimension(R.dimen.w25).toInt()

                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.left = resources.getDimension(R.dimen.w25).toInt()
                }
            }
        })
    }

    override fun onListener(view: View) {
    }

    override fun reTryGetData() {
    }


    private fun loadMeData() {
        ApiManager.get(1, this, hashMapOf(), Constant.GET_MYINTEREST, object : ApiManager.OnResult<BaseModel<ArrayList<InterestModel.Data>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<InterestModel.Data>>) {
                if (data.success) {
                    data.entity?.let {
                        copyTopData.addAll(it) //对比数据是否改变
                        topData = it.filter { it.status == 2 } as ArrayList<InterestModel.Data> //需求变更只显示自定义标签
                        listData = it.filter { it.status != 2 } as ArrayList<InterestModel.Data>
                        interestingTopAdapter = InterestingTopAdapter(topData)
                        initListener()
                        recyclerViewTop.adapter = interestingTopAdapter
                        loadAllData()
                    }
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<ArrayList<InterestModel.Data>>) {

            }
        })
    }


    private fun loadAllData() {

        ApiManager.get(1, this, hashMapOf(), Constant.GET_ALLINTERESTS, object : ApiManager.OnResult<BaseModel<ArrayList<InterestModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<InterestModel>>) {
                if (data.success) {
                    data.entity?.let {
                        interestingBottomAdapter = InterestingBottomAdapter(it, listData).setOnDataListener(object : InterestingDataListener {
                            override fun dataCallBack(listPosition:Int,itemPosition:Int,data: InterestModel.Data) {
                                if (!listData.contains(data)) {
                                    if (listData.filter { it.idUserDescribeGroup == data.idUserDescribeGroup }.size < 2) {
                                        listData.add(data)
//                                        interestingBottomAdapter.notifyItemChanged(listData.indexOf(data))
                                        interestingBottomAdapter.adapterMap[listPosition]?.notifyItemChanged(itemPosition)
                                    } else {
                                        ToastUtil.show("已达当前分类上限")
                                    }
                                } else {
                                    listData.remove(data)
                                    interestingBottomAdapter.adapterMap[listPosition]?.notifyItemChanged(itemPosition)
                                }
                            }
                        })
                        recyclerViewBottom.adapter = interestingBottomAdapter
                    }

                } else {
                    ToastUtil.show(data.message)
                }
            }

            override fun onFailed(e: Throwable) {
            }

            override fun onCatch(data: BaseModel<ArrayList<InterestModel>>) {
            }
        })

    }

    @Suppress("UNCHECKED_CAST")
    private fun updataData() {
        val dataList = listData.clone() as ArrayList<InterestModel.Data>
        dataList.addAll(topData.filter { it.idUserDescribeGroup != "sm1168" } as ArrayList<InterestModel.Data>)

        val jsonObj = JSONObject()
        jsonObj.put("list", JSONArray(Gson().toJson(dataList)))
        jsonObj.put("userId", UserManager.getUserId())
        ApiManager.postJson(this, jsonObj.toString(), Constant.SET_MYINTEREST, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                if (data.success) {
                    val intent = Intent()
                    intent.putExtra("title", "兴趣爱好")
                    setResult(IntentCode.IS_INPUT, intent)
                    finish()
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


    //自定义兴趣爱好
    fun customInteresting(name: String) {
        if (name.isEmpty()) {
            return
        }
        val params = hashMapOf<String, String>()
        params["interestName"] = name
        ApiManager.post(this, params, Constant.INSERT_PRIVATE_LABEL, object : ApiManager.OnResult<BaseModel<InterestModel.Data>>() {
            override fun onSuccess(data: BaseModel<InterestModel.Data>) {
                if (data.success) {
                    data.entity?.let {
                        it.status = 2
                        interestingTopAdapter.addData(it)
                        interestingTopAdapter.removeDefaultCustemData()
                        interestingTopAdapter.notifyDataSetChanged()

                    }
                } else {
                    ToastUtil.show(data.message)
                }

            }

            override fun onFailed(e: Throwable) {

            }

            override fun onCatch(data: BaseModel<InterestModel.Data>) {

            }

        })
    }

}