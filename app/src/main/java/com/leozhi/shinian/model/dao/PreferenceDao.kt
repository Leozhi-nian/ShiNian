package com.leozhi.shinian.model.dao

import android.annotation.SuppressLint
import com.leozhi.common.Preference
import com.leozhi.shinian.MyApp

/**
 * @author leozhi
 * @date 2021/3/12
 */
@SuppressLint("StaticFieldLeak")
object PreferenceDao {
    var rootPath: String by Preference(MyApp.context, "root_path", "", "file_setting")

    fun isRootPathSaved(): Boolean {
        return rootPath.isNotEmpty()
    }
}