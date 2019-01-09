#新人上手指南（Android 小红娘APP）

>开发语言 Kotlin

>Android兼容最低为API 16 (4.1版本)

## 核心目录
1. app (项目主工程)

2. faceplatform-release (人脸识别SDK)

3. faceplatform-ui (人脸识别UI库)

4. ocr_ui (图像识别UI库)

5. pingpp (P++支付SDK)


## APP目录结构
### BASE
#### [BaseActivity.kt](app/src/main/java/com/cocosh/shmstore/base/BaseActivity.kt)
> 所有Activity的父类，用于管理并统一Activity的操作和规范

abstract fun <font color="#ff0000">setLayout()</font>: Int 
>返回layout资源ID，用户初始化布局

abstract fun <font color="#ff0000">initView() </font>
>初始化组件操作

abstract fun <font color="#ff0000">onListener(view: View)</font>
>响应组件的点击事件

abstract fun <font color="#ff0000">reTryGetData() </font> 
>重新加载数据方法

fun <T : ViewDataBinding> <font color="#ff0000">getDataBinding()</font>: T = viewDataBinding as T  
>获取MVVM架构的DataBinding对象

fun <font color="#ff0000">showKeyboard(token: IBinder) </font>
>强制弹出输入法

fun <font color="#ff0000">hideKeyboard(token: IBinder) </font>
>强制隐藏输入法

abstract fun <font color="#ff0000">close()</font>
>关闭回调方法用于回收资源


#### [BaseFragment.kt](app/src/main/java/com/cocosh/shmstore/base/BaseFragment.kt)
> 所有Fragment的父类，用于管理并统一Fragment的操作和规范

abstract fun <font color="#ff0000">setLayout()</font>: Int 
>返回layout资源ID，用户初始化布局

abstract fun <font color="#ff0000">initView() </font>
>初始化组件操作

abstract fun <font color="#ff0000">onListener(view: View)</font>
>响应组件的点击事件

abstract fun <font color="#ff0000">reTryGetData() </font> 
>重新加载数据方法

fun <T : ViewDataBinding> <font color="#ff0000">getDataBinding()</font>: T = viewDataBinding as T  
>获取MVVM架构的DataBinding对象

fun <font color="#ff0000">showKeyboard(token: IBinder) </font>
>强制弹出输入法

fun <font color="#ff0000">hideKeyboard(token: IBinder) </font>
>强制隐藏输入法

abstract fun <font color="#ff0000">close()</font>
>关闭回调方法用于回收资源

3. [BaseModel.kt](app/src/main/java/com/cocosh/shmstore/base/BaseModel.kt)
4. [BaseRecyclerAdapter.kt](app/src/main/java/com/cocosh/shmstore/base/BaseRecyclerAdapter.kt)
5. [BaseRecyclerViewHolder.kt](app/src/main/java/com/cocosh/shmstore/base/BaseRecyclerViewHolder.kt)
6. [IBasePresenter.kt](app/src/main/java/com/cocosh/shmstore/base/IBasePresenter.kt)
7. [IBaseView.kt](app/src/main/java/com/cocosh/shmstore/base/IBaseView.kt)
8. [NoDoubleClickListener.kt](app/src/main/java/com/cocosh/shmstore/base/NoDoubleClickListener.kt)
9. [OnitemClickListener.kt](app/src/main/java/com/cocosh/shmstore/base/OnitemClickListener.kt)




