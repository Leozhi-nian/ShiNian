package com.leozhi.shinian.util

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog

/**
 * @author leozhi
 * @date 2021/3/7
 */
object PermissionUtil {
    /**
     * @param context 上下文
     * @param launcher 跳转权限访问界面的启动器
     *
     * 获取所有文件访问权限
     */
    fun filesAccessPermission(context: Context, launcher: ActivityResultLauncher<Any>) {
        // 判断 Android 版本是否为 R （Android 11）以上且用户未被授予权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            AlertDialog.Builder(context).run {
                setMessage("本程序需要您授予访问所有文件的权限")
                setPositiveButton("确定") { _, _ ->
                    launcher.launch(true)
                }
                show()
            }
        }
    }
}