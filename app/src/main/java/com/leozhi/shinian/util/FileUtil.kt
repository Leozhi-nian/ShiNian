package com.leozhi.shinian.util

import android.os.Build
import android.os.Environment
import com.leozhi.shinian.MyApp
import java.io.File

/**
 * @author leozhi
 * @date 2021/3/7
 */
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
}