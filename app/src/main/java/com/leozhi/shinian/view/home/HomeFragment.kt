package com.leozhi.shinian.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dhl.binding.viewbind
import com.leozhi.common.showToast
import com.leozhi.shinian.MyApp
import com.leozhi.shinian.Preference
import com.leozhi.shinian.adapter.FileAdapter
import com.leozhi.shinian.databinding.FragmentHomeBinding
import com.leozhi.shinian.util.FileUtil
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by viewbind()
    private val viewModel: HomeViewModel by viewModel(owner = { ViewModelOwner.Companion.from(requireActivity()) })
    private val adapter: FileAdapter by lazy { FileAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isBackPressed = false
        // 监听返回按钮
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.getCurrentPath() != Preference.rootPath) {
                    viewModel.getListFiles(File(viewModel.getCurrentPath()).parent!!)
                } else {
                    if (!isBackPressed) {
                        "再按一次退出".showToast(MyApp.context)
                        isBackPressed = true
                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                isBackPressed = false
                            }
                        }, 2000)
                    } else {
                        requireActivity().finish()
                    }
                }
            }
        })

        viewModel.fileLiveData.observe(requireActivity()) {
            viewModel.fileList.clear()
            if (it != null) {
                viewModel.fileList.addAll(it)
            }
            adapter.submitList(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
        // 关闭 RecyclerView 刷新动画
        binding.recyclerview.itemAnimator = null

        // 滚动至销毁前的位置
        val (position, offset) = viewModel.getPosition()
        layoutManager.scrollToPositionWithOffset(position, offset)

        // 监听点击事件
        adapter.setOnItemClickListener(object : FileAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val onClickPath = viewModel.fileList[position].path
                // 判断点击的是文件还是目录
                if (File(onClickPath).isDirectory) {
                    viewModel.getListFiles(onClickPath)
                } else {
                    "这是一个文件：$onClickPath".showToast(MyApp.context)
                }
            }
        })

        // 监听长按事件
        adapter.setOnItemLongClickListener(object : FileAdapter.OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                "长按事件".showToast(MyApp.context)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val path = if (viewModel.getCurrentPath().isEmpty()) FileUtil.getRootPath() else viewModel.getCurrentPath()
        viewModel.getListFiles(path)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setPosition(getPositionAndOffset())
    }

    /**
     * 获取显示的第一个 item 的位置和相对于 top 的偏移量
     *
     * @return (position, offset) 位置，偏移量
     */
    private fun getPositionAndOffset(): Pair<Int, Int> {
        var res = Pair(0, 0)
        val layoutManager = binding.recyclerview.layoutManager as LinearLayoutManager
        val firstView = layoutManager.getChildAt(0)
        firstView?.let {
            res = Pair(layoutManager.getPosition(firstView), firstView.top - 17)
        }
        return res
    }
}