package com.cocosh.shmstore.zxing.common.executor

import com.cocosh.shmstore.zxing.common.PlatformSupportManager

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class AsyncTaskExecManager : PlatformSupportManager<AsyncTaskExecInterface>(AsyncTaskExecInterface::class.java, DefaultAsyncTaskExecInterface()) {
    init {
        addImplementationClass(11, "com.google.zxing.client.android.common.executor.HoneycombAsyncTaskExecInterface")
    }

}