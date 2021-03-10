package com.leozhi.shinian.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.model.repo.HomeRepository
import com.leozhi.shinian.util.FileUtil
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(private val repo: HomeRepository) : ViewModel() {
    private val pathLiveData = MutableLiveData<String>()
    private val positionAndOffsetLiveData: MutableLiveData<Stack<Pair<Int, Int>>> by lazy {
        MutableLiveData<Stack<Pair<Int, Int>>>().also {
            it.value = Stack<Pair<Int, Int>>()
        }
    }
    val fileList = ArrayList<FileBean>()
    var positionAndOffset = Pair(0, 0)
    var onLongClickCoordinate = Pair(0, 0)
    var popupMenuIsShowing = false
    var recyclerViewScrollable = true
    var itemClickable = true
    var checkedItemPosition = 0

    val getListFiles = Transformations.switchMap(pathLiveData) { path ->
        repo.getListFiles(path)
    }

    /**
     * 获取目录下的子目录或文件
     * 通过改变 pathLiveData 的值，fileLiveData 识别到变化也会作出相应变化
     * @param path 给定目录
      */
    fun setCurrentPath(path: String) {
        pathLiveData.value = path
    }

    /**
     * 获取当前目录
     * @return 当前目录
     */
    fun getCurrentPath(): String {
        return pathLiveData.value ?: FileUtil.getRootPath()
    }

    /**
     * 向栈中添加位置和偏移量的数对
     */
    fun addPositionAndOffset(pair: Pair<Int, Int>) {
        positionAndOffsetLiveData.value!!.push(pair)
    }

    /**
     * 从栈中移除栈顶的位置和偏移量的数对
     */
    fun removePositionAndOffset(): Pair<Int, Int> {
        return positionAndOffsetLiveData.value!!.pop()
    }
}