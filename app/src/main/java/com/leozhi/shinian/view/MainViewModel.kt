package com.leozhi.shinian.view

import androidx.lifecycle.ViewModel
import com.leozhi.shinian.model.repo.MainRepository

/**
 * @author leozhi
 * @date 2021/3/8
 */
class MainViewModel(private val repo: MainRepository) : ViewModel() {
    fun saveRootPath(path: String) {
        repo.saveRootPath(path)
    }

    fun getSavedRootPath() = repo.getSavedRootPath()

    fun isRootPathSaved() = repo.isRootPathSaved()
}