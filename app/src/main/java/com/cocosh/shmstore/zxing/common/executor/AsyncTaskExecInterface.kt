package com.cocosh.shmstore.zxing.common.executor

import android.os.AsyncTask

/**
 *
 * Created by zhangye on 2018/4/26.
 */
interface AsyncTaskExecInterface {

    fun <T> execute(task: AsyncTask<T, *, *>, vararg args: T)

}