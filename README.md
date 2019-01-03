#新人上手指南（Android 小红娘APP）

>开发语言 Kotlin

>Android兼容最低为API 16 (4.1版本)

## 核心目录
1. app (项目主工程)

2. faceplatform-release (人脸识别SDK)

3. faceplatform-ui (人脸识别UI库)

4. ocr_ui (图像识别UI库)

5. pingpp (P++支付SDK)


>## APP目录结构
> * base 存储父类用户减少重复代码
>   1. [BaseActivity.kt](app/src/main/java/com/cocosh/shmstore/base/BaseActivity.kt)
>   2. [BaseFragment.kt](app/src/main/java/com/cocosh/shmstore/base/BaseFragment.kt)
>   3. [BaseModel.kt](app/src/main/java/com/cocosh/shmstore/base/BaseModel.kt)
>   4. [BaseRecyclerAdapter.kt](app/src/main/java/com/cocosh/shmstore/base/BaseRecyclerAdapter.kt)
>   5. [BaseRecyclerViewHolder.kt](app/src/main/java/com/cocosh/shmstore/base/BaseRecyclerViewHolder.kt)
>   6. [IBasePresenter.kt](app/src/main/java/com/cocosh/shmstore/base/IBasePresenter.kt)
>   7. [IBaseView.kt](app/src/main/java/com/cocosh/shmstore/base/IBaseView.kt)
>   8. [NoDoubleClickListener.kt](app/src/main/java/com/cocosh/shmstore/base/NoDoubleClickListener.kt)
>   9. [OnitemClickListener.kt](app/src/main/java/com/cocosh/shmstore/base/OnitemClickListener.kt)




