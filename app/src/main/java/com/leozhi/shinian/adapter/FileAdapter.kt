package com.leozhi.shinian.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hi.dhl.binding.viewbind
import com.leozhi.shinian.adapter.FileAdapter.ViewHolder
import com.leozhi.shinian.R
import com.leozhi.shinian.databinding.LayoutFileItemBinding
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.util.LogUtil
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author leozhi
 * @date 2021/3/7
 */
class FileAdapter : ListAdapter<FileBean, ViewHolder>(FileBean.DIFF_CALLBACK) {
    private lateinit var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}