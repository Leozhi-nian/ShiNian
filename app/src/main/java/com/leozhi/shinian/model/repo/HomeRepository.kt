package com.leozhi.shinian.model.repo

import androidx.lifecycle.liveData
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.util.FileUtil
import com.leozhi.shinian.util.FileUtil.childrenCount
import com.leozhi.shinian.util.FileUtil.sizeConvert
import com.leozhi.shinian.util.FileUtil.type
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
                listFiles()?.filterNot { it.isHidden }?.sortedWith(comparator).let { files ->
                    files?.forEach { file ->
                        val fileBean = FileBean(
                            name = file.name,
                            path = file.absolutePath,
                            childrenCount = file.childrenCount(),
                            fileType = file.type(),
                            size = file.sizeConvert(),
                            modifyDate = file.lastModified()
                        )
                        fileBeanList.add(fileBean)
                    }
                }
            }
        }
        emit(fileBeanList)
    }
}