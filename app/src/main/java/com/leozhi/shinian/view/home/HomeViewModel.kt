package com.leozhi.shinian.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.model.repo.HomeRepository
import com.leozhi.shinian.util.FileUtil

class HomeViewModel(private val repo: HomeRepository) : ViewModel() {
    private val pathLiveData = MutableLiveData<String>()
    private val positionLiveData = MutableLiveData<Pair<Int, Int>>()
    val fileList = ArrayList<FileBean>()

    val fileLiveData = Transformations.switchMap(pathLiveData) { path ->
        repo.getListFiles(path)
    }

    /**
     * 获取目录下的子目录或文件
     * 通过改变 pathLiveData 的值，fileLiveData 识别到变化也会作出相应变化
     * @param path 给定目录
      */
    fun getListFiles(path: String) {
        pathLiveData.value = path
    }

    /**
     * 获取当前目录
     * @return 当前目录
     */
    fun getCurrentPath(): String {
        return pathLiveData.value ?: FileUtil.getRootPath()
    }

    fun setPosition(value: Pair<Int, Int>) {
        positionLiveData.value = value
    }

    fun getPosition(): Pair<Int, Int> {
        return positionLiveData.value ?: Pair(0, 0)
    }
}