package com.cocosh.shmstore.zxing.common.executor

import android.os.AsyncTask

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class DefaultAsyncTaskExecInterface : AsyncTaskExecInterface {

    override fun <T> execute(task: AsyncTask<T, *, *>, vararg args: T) {
        task.execute(*args)
    }

}