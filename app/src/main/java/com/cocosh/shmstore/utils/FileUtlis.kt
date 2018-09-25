package com.cocosh.shmstore.utils

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat

/**
 * 文件操作相关工具类
 */
class FileUtlis {

    var caChePath = getFile(null)


    /**
     * 获取文件路径
     */
    fun getFile(name: String?): File {
        val file: File
        if (name != null) {
            file = File(getDir(), name)
            if (!file.exists()) {
                file.createNewFile()
            }
        } else {
            file = File(getDir())
            if (!file.exists()) file.mkdirs()
        }
        return file
    }

        private fun getDir(): String = (Environment.getExternalStorageDirectory().path + "/temp/sm")


    fun getFileSize(): String {
        var size = 0L
        caChePath.listFiles()?.forEach {
            size += getFileSize(it)
        }
        return formetFileSize(size)
    }

    /**
     * 获取指定文件大小
     *
     * @return
     */
    private fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            val fis = FileInputStream(file)
            size = fis.available().toLong()
        }
        return size
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private fun formetFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        val fileSizeString: String
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        fileSizeString = when {
            fileS < 1024 -> df.format(fileS.toDouble()) + "B"
            fileS < 1048576 -> df.format(fileS.toDouble() / 1024) + "KB"
            fileS < 1073741824 -> df.format(fileS.toDouble() / 1048576) + "MB"
            else -> df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    /**
     * 删除所有缓存文件
     */
     fun deleteAllFile() {
        caChePath.listFiles()?.forEach {
            it.delete()
        }
    }
}