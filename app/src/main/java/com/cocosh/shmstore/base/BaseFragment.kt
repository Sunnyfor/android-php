package com.cocosh.shmstore.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.cocosh.shmstore.R
import com.cocosh.shmstore.utils.TitleManager
import kotlinx.android.synthetic.main.fragment_base.view.*
import kotlinx.android.synthetic.main.layout_error.view.*


/**
 * Fragment父类
 * Created by 张野 on 2017/10/13.
 */
abstract class BaseFragment : Fragment(), View.OnClickListener {
    private lateinit var mView: View
    private var savedInstanceState: Bundle? = null
    private lateinit var noDoubleClickListener: NoDoubleClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        this.savedInstanceState = savedInstanceState
        mView = inflater.inflate(R.layout.fragment_base, container, false)
        mView.iframeBody.addView(inflater.inflate(setLayout(), container, false))
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noDoubleClickListener = NoDoubleClickListener(this)
        initView()
        getLayoutView().btnTryAgain.setOnClickListener(this)
    }

    /**
     * 设置布局
     *
     * @return 资源ID
     */
    abstract fun setLayout(): Int

    /**
     * 初始化View组件
     */
    abstract fun initView()


    //重试
    abstract fun reTryGetData()

    /**
     * 组件点击事件
     */
    abstract fun onListener(view: View)


    abstract fun close()


    override fun onDestroy() {
        super.onDestroy()
        close()
    }


    override fun onClick(view: View) {
        if (view.id == getLayoutView().btnTryAgain.id) {
            reTryGetData()
        }
        noDoubleClickListener.onClick(view)
    }

    fun getLayoutView(): View = mView


    fun showTitle(fragment: BaseFragment, isImmersion: Boolean) {
        mView.iframeTitle.visibility = View.VISIBLE
        childFragmentManager.beginTransaction().add(mView.iframeTitle.id, fragment).commit()

        if (isImmersion) {
            mView.iframeTitle.setBackgroundResource(R.color.transparent)
            (mView.iframeBody.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.BELOW, 0)
        } else {
            mView.ivTitleLine.visibility = View.VISIBLE
        }

    }

    fun showReTryLayout() {
        getLayoutView().frameFragmentError.visibility = View.VISIBLE
    }

    fun showReTryLayout(desc: String,isShowBtn:Boolean) {
        if (isShowBtn){
            getLayoutView().btnTryAgain.visibility = View.GONE
        }
        getLayoutView().frameFragmentError.visibility = View.VISIBLE
        getLayoutView().tvMessage.text = desc
    }


    fun hideReTryLayout() {
        getLayoutView().frameFragmentError.visibility = View.GONE
    }

    fun showTitle(fragment: BaseFragment) {
        showTitle(fragment, false)
    }

    fun getBundle(): Bundle? = this.savedInstanceState


    fun getBaseActivity(): BaseActivity = (activity as BaseActivity)

    fun getTitleManager(): TitleManager = getBaseActivity().titleManager
}