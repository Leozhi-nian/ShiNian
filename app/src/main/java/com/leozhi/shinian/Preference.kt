package com.leozhi.shinian

import android.annotation.SuppressLint
import com.leozhi.common.Preference

/**
 * @author leozhi
 * @date 2021/3/7
 */
@SuppressLint("StaticFieldLeak")
object Preference {
    var rootPath: String by Preference(MyApp.context, "root_path", "", "file_setting")
}