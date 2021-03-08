package com.leozhi.shinian.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hi.dhl.binding.viewbind
import com.leozhi.shinian.R
import com.leozhi.shinian.adapter.FileAdapter.ViewHolder
import com.leozhi.shinian.databinding.LayoutFileItemBinding
import com.leozhi.shinian.model.bean.FileBean
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author leozhi
 * @date 2021/3/7
 */
class FileAdapter : ListAdapter<FileBean, ViewHolder>(FileBean.DIFF_CALLBACK) {
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(position) ?: false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.layout_file_item
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: LayoutFileItemBinding by viewbind()
        fun bindData(data: FileBean?) {
            binding.apply {
                data?.let { it ->
                    fileName.text = it.name
                    fileSize.text = it.size
                    fileDate.text = it.modifyDate.let { date ->
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(date)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    /**
     * 项目点击监听器
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    /**
     * 项目长按监听器
     */
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int): Boolean
    }
}