package com.cocosh.shmstore.zxing.common

import android.annotation.SuppressLint
import android.os.Build
import com.cocosh.shmstore.utils.LogUtil
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 *
 * Created by zhangye on 2018/4/26.
 */
abstract class PlatformSupportManager<out T> protected constructor(private val managedInterface: Class<T>, private val defaultImplementation: T) {
    private val implementations: SortedMap<Int, String>

    init {
        if (!managedInterface.isInterface) {
            throw IllegalArgumentException()
        }
        if (!managedInterface.isInstance(defaultImplementation)) {
            throw IllegalArgumentException()
        }
        this.implementations = TreeMap(Collections.reverseOrder<Any>())
    }

    protected fun addImplementationClass(minVersion: Int, className: String) {
        implementations[minVersion] = className
    }

    @SuppressLint("ObsoleteSdkInt")
    fun build(): T {
        for (minVersion in implementations.keys) {
            if (Build.VERSION.SDK_INT >= minVersion) {
                val className = implementations[minVersion]
                try {
                    val clazz = Class.forName(className).asSubclass(managedInterface)
                    LogUtil.i("Using implementation $clazz of $managedInterface for SDK $minVersion")
                    return clazz.getConstructor().newInstance()
                } catch (cnfe: ClassNotFoundException) {
                    LogUtil.i("sdk版本高于11不在执行兼容")
                } catch (iae: IllegalAccessException) {
                    LogUtil.w(iae)
                } catch (ie: InstantiationException) {
                    LogUtil.w(ie)
                } catch (nsme: NoSuchMethodException) {
                    LogUtil.w(nsme)
                } catch (ite: InvocationTargetException) {
                    LogUtil.w(ite)
                }

            }
        }
        return defaultImplementation
    }


}