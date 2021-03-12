package com.leozhi.shinian.view.home

import android.graphics.Point
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.model.repo.HomeRepository
import com.leozhi.shinian.util.FileUtil
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(private val repo: HomeRepository) : ViewModel() {
    private val currentPathLiveData by lazy { MutableLiveData<String>() }
    private val positionAndOffsetStackLiveData by lazy { MutableLiveData(Stack<Pair<Int, Int>>()) }

    val getListFiles = Transformations.switchMap(currentPathLiveData) { path -> repo.getListFiles(path) }

    val fileList = ArrayList<FileBean>()
    var positionAndOffset = Pair(0, 0)
    var onLongClickCoordinate = Point(0, 0)
    var popupMenuIsShowing = false
    var recyclerViewScrollable = true
    var itemClickable = true
    var checkedItemPosition = 0


    /**
     * 获取目录下的子目录或文件
     * 通过改变 pathLiveData 的值，fileLiveData 识别到变化也会作出相应变化
     * @param path 给定目录
      */
    fun setCurrentPath(path: String) {
        currentPathLiveData.value = path
    }

    /**
     * 获取当前目录
     * @return 当前目录
     */
    fun getCurrentPath(): String {
        return currentPathLiveData.value ?: FileUtil.getRootPath()
    }

    /**
     * 向栈中添加位置和偏移量的数对
     */
    fun addPositionAndOffset(pair: Pair<Int, Int>) {
        positionAndOffsetStackLiveData.value!!.push(pair)
    }

    /**
     * 从栈中移除栈顶的位置和偏移量的数对
     */
    fun removePositionAndOffset(): Pair<Int, Int> {
        return positionAndOffsetStackLiveData.value!!.pop()
    }
}