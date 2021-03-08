package com.leozhi.shinian.model.bean

import androidx.recyclerview.widget.DiffUtil

/**
 * @author leozhi
 * @date 2021/3/7
 */
data class FileBean(
    var name: String,
    val path: String,
    var size: String,
    var modifyDate: Long
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FileBean>() {
            // 判断两个Objects 是否代表同一个item对象
            override fun areItemsTheSame(oldItem: FileBean, newItem: FileBean): Boolean =
                oldItem.path == newItem.path

            // 判断两个Objects 是否有相同的内容。
            override fun areContentsTheSame(oldItem: FileBean, newItem: FileBean): Boolean = true
        }
    }
}
