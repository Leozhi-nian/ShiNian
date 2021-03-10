package com.leozhi.shinian.model.repo

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.io.File

/**
 * @author leozhi
 * @date 2021/3/6
 */
class HomeRepository {
    fun getListFiles(path: String) = liveData(Dispatchers.IO) {
        emit(File(path).listFiles())
    }
}