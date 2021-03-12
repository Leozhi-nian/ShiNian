package com.leozhi.shinian.model.repo

import com.leozhi.shinian.model.dao.PreferenceDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author leozhi
 * @date 2021/3/12
 */
class MainRepository {
    fun saveRootPath(path: String) = CoroutineScope(Dispatchers.IO).launch {
        PreferenceDao.rootPath = path
    }

    fun getSavedRootPath() = PreferenceDao.rootPath

    fun isRootPathSaved() = CoroutineScope(Dispatchers.IO).launch {
        PreferenceDao.isRootPathSaved()
    }
}