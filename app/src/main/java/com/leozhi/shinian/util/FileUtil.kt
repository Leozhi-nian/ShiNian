package com.leozhi.shinian.util

import android.os.Build
import android.os.Environment
import com.leozhi.shinian.MyApp
import java.io.File
import java.util.*

/**
 * @author leozhi
 * @date 2021/3/7
 */
@Suppress("unused")
object FileUtil {
    /**
     * @return rootPath 根目录路径
     * 获取根目录的路径
     */
    fun getRootPath(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            return ""
        }
        var appRootDir = MyApp.context.getExternalFilesDir(null)!!
        do {
            appRootDir = appRootDir.parentFile!!
        } while (appRootDir.absolutePath.contains("/Android"))
        return appRootDir.absolutePath
    }

    /**
     * 按照文件名正序排列
     */
    val sortedByName = Comparator<File> { o1, o2 ->
        o1.name.toLowerCase(Locale.ROOT).compareTo(o2.name.toLowerCase(Locale.ROOT))
    }

    /**
     * 按照文件名逆序排列
     */
    val sortedByNameDescending = Comparator<File> { o1, o2 ->
        o2.name.toLowerCase(Locale.ROOT).compareTo(o1.name.toLowerCase(Locale.ROOT))
    }

    fun deleteDirectory(dir: File) {
        val files = dir.listFiles()
        if (files == null) {
            dir.delete()
            return
        }
        for (file in files) {
            if (file.isFile) {
                file.delete()
            } else {
                deleteDirectory(file)
            }
        }
        dir.delete()
    }

    fun renameFile(oldFile: File, newFile: File): File {
        oldFile.renameTo(newFile)
        return newFile
    }

    fun isExist(parent: File, child: File): Boolean {
        return parent.listFiles()?.contains(child) ?: false
    }
}