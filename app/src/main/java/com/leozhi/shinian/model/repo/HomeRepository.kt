package com.leozhi.shinian.model.repo

import androidx.lifecycle.liveData
import com.leozhi.common.convert
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.util.LogUtil
import kotlinx.coroutines.Dispatchers
import java.io.File

/**
 * @author leozhi
 * @date 2021/3/6
 */
class HomeRepository {
    fun getListFiles(path: String) = liveData(Dispatchers.IO) {
        val result = File(path).listFiles()?.let {
            it.map { file ->
                FileBean(file.name, file.path, file.length().convert(), file.lastModified())
            }
        }
        emit(result)
    }
}