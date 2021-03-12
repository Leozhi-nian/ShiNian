package com.leozhi.shinian.util

import com.leozhi.common.showToast
import com.leozhi.shinian.MyApp
import com.leozhi.shinian.model.bean.FileType
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author leozhi
 * @date 2021/3/7
 */
@Suppress("unused")
object FileUtil {
    /**
     * @return rootPath 根目录路径
     * 获取根目录的路径
     */
    fun getRootPath(): String {
        var appRootDir = MyApp.context.getExternalFilesDir(null)!!
        do {
            appRootDir = appRootDir.parentFile!!
        } while (appRootDir.absolutePath.contains("/Android"))
        return if (PermissionUtil.hasStoragePermission(MyApp.context)) appRootDir.absolutePath else ""
    }

    /**
     * 按照文件名正序排列
     */
    val sortedByName = Comparator<File> { file1, file2 ->
        if (file1.isDirectory.xor(file2.isDirectory)) {
            if (file1.isDirectory) -1 else 1
        } else {
            file1.name.toLowerCase(Locale.ROOT).compareTo(file2.name.toLowerCase(Locale.ROOT))
        }
    }

    /**
     * 按照文件名逆序排列
     */
    val sortedByNameDescending = Comparator<File> { file1, file2 ->
        if (file1.isDirectory.xor(file2.isDirectory)) {
            if (file1.isDirectory) -1 else 1
        } else {
            file2.name.toLowerCase(Locale.ROOT).compareTo(file1.name.toLowerCase(Locale.ROOT))
        }
    }

    /**
     * 获取文件的子文件个数
     * @return 子文件个数
     */
    fun File.childrenCount(): Int {
        return listFiles()?.filterNot { it.isHidden }?.size ?: 0
    }

    /**
     * 获取文件类型
     * @return FileType
     */
    fun File.type(): FileType {
        return if (this.isDirectory) {
            FileType.Directory
        } else {
            when (this.extension) {
                "txt", "text" -> FileType.Txt
                "md", "markdown" -> FileType.Markdown
                else -> FileType.Other
            }
        }
    }

    /**
     * 文件大小转换
     */
    fun File.sizeConvert(): String {
        var time = 0
        var size = this.length().toDouble()
        while (size >= 1024) {
            ++time
            size /= 1024
        }
        return when (time) {
            0 -> String.format("%.2f B", size)
            1 -> String.format("%.2f K", size)
            2 -> String.format("%.2f M", size)
            3 -> String.format("%.2f G", size)
            else -> String.format("%.2f T", size)
        }
    }

    fun File.rename(newFile: File): Boolean {
        if (this.renameTo(newFile)) {
            return true
        }
        return false
    }

    fun File.rename(newName: String): Boolean {
        val parent = this.parent
        val newFile = File("$parent/$newName")
        return this.rename(newFile)
    }

    fun File.createFile(): Boolean {
        if (this.exists()) {
            "文件已存在".showToast(MyApp.context)
            return false
        }
        try {
            if (this.createNewFile()) {
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun File.createDir(): Boolean {
        if (this.exists()) {
            "文件已存在".showToast(MyApp.context)
            return false
        }
        if (this.mkdirs()) {
            return true
        }
        return false
    }

    fun deleteDirectory(dir: File) {
        val files = dir.listFiles()
        if (files == null) {
            dir.delete()
            return
        }
        for (file in files) {
            if (file.isFile) {
                file.delete()
            } else {
                deleteDirectory(file)
            }
        }
        dir.delete()
    }

    fun renameFile(oldFile: File, newFile: File): File {
        oldFile.renameTo(newFile)
        return newFile
    }
}