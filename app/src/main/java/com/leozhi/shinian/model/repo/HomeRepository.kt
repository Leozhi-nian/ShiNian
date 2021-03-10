package com.leozhi.shinian.model.repo

import androidx.lifecycle.liveData
import com.leozhi.common.convert
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.util.FileUtil
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author leozhi
 * @date 2021/3/6
 */
class HomeRepository {
    fun getListFiles(path: String, comparator: Comparator<File> = FileUtil.sortedByName) = liveData(Dispatchers.IO) {
        val fileBeanList = ArrayList<FileBean>()
        File(path).apply {
            if (isDirectory) {
                listFiles()?.sortedWith(comparator)?.map { file ->
                    if (!file.isHidden) {
                        val fileBean = FileBean(
                            name = file.name,
                            path = file.absolutePath,
                            size = file.length().convert(),
                            modifyDate = file.lastModified())
                        fileBeanList.add(fileBean)
                    }
                }
            }
        }
        emit(fileBeanList)
    }
}