package com.leozhi.shinian.view.home

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hi.dhl.binding.viewbind
import com.leozhi.common.convert
import com.leozhi.common.showToast
import com.leozhi.shinian.MyApp
import com.leozhi.shinian.Preference
import com.leozhi.shinian.R
import com.leozhi.shinian.adapter.FileAdapter
import com.leozhi.shinian.databinding.FragmentHomeBinding
import com.leozhi.shinian.model.bean.FileBean
import com.leozhi.shinian.util.FileUtil
import com.leozhi.shinian.util.LogUtil
import com.leozhi.shinian.widget.FreePopupMenu
import com.leozhi.shinian.widget.OnMenuItemClickListener
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by viewbind()
    private val viewModel: HomeViewModel by viewModel(owner = { ViewModelOwner.Companion.from(requireActivity()) })
    private val adapter: FileAdapter by lazy { FileAdapter() }
    private val freePopupMenu by lazy { FreePopupMenu(requireActivity()) }
    private val layoutManager: LinearLayoutManager by lazy { object : LinearLayoutManager(context) {
        override fun canScrollVertically(): Boolean {
            return super.canScrollVertically() && viewModel.recyclerViewScrollable
        }
    } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isBackPressed = false
        // 监听返回按钮
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (freePopupMenu.isShowing) {
                    freePopupMenu.dismiss()
                    return
                }
                if (viewModel.getCurrentPath() != Preference.rootPath) {
                    viewModel.getListFiles(File(viewModel.getCurrentPath()).parent!!)
                    // 获取父目录之前所在位置和偏移量
                    viewModel.positionAndOffset = viewModel.removePositionAndOffset()
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

        // 设置文件项长按弹出菜单
        freePopupMenu.inflate(R.menu.popup_menu_file_item)
        freePopupMenu.setOnMenuItemClickListener(object : OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem) {
                when (item.itemId) {
                    R.id.popup_menu_copy -> item.title.toString().showToast(MyApp.context)
                    R.id.popup_menu_move -> item.title.toString().showToast(MyApp.context)
                    R.id.popup_menu_rename -> item.title.toString().showToast(MyApp.context)
                    R.id.popup_menu_delete -> item.title.toString().showToast(MyApp.context)
                    R.id.popup_menu_property -> item.title.toString().showToast(MyApp.context)
                }
                viewModel.popupMenuIsShowing = false
            }
        })

        // 观察 LiveData 数据变化
        viewModel.fileLiveData.observe(requireActivity()) { result ->
            viewModel.fileList.clear()
            result?.let {
                // 将文件数组映射为 FileBean 集合
                FileUtil.filterListFiles(it).map { file ->
                    FileBean(file.name, file.path, file.length().convert(), file.lastModified())
                }
            }?.let {
                viewModel.fileList.addAll(it)
                adapter.submitList(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
        // 关闭 RecyclerView 刷新动画
        binding.recyclerview.itemAnimator = null

        // 滑动至销毁前所在位置和偏移量
        viewModel.positionAndOffset.let {
            layoutManager.scrollToPositionWithOffset(it.first, it.second)
        }

        // RecyclerView 刷新后滑动至之前的位置和偏移量
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                viewModel.positionAndOffset.let {
                    layoutManager.scrollToPositionWithOffset(it.first, it.second)
                }
            }
        })

        // 监听点击事件
        adapter.setOnItemClickListener(object : FileAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                LogUtil.d("MainActivity", "onClick")
                if (viewModel.itemClickable) {
                    val onClickPath = viewModel.fileList[position].path
                    // 判断点击的是文件还是目录
                    if (File(onClickPath).isDirectory) {
                        viewModel.getListFiles(onClickPath)
                        // 将当前目录所在的位置和偏移量存入栈中
                        viewModel.addPositionAndOffset(getPositionAndOffset())
                        // 设置子目录初始化时所在的位置和偏移量分别为0，0
                        viewModel.positionAndOffset = Pair(0, 0)
                    } else {
                        "这是一个文件：$onClickPath".showToast(MyApp.context)
                    }
                }
            }
        })

        // 监听长按事件
        adapter.setOnItemLongClickListener(object : FileAdapter.OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                val (x, y) = viewModel.onLongClickCoordinate
                freePopupMenu.show(layoutManager.findViewByPosition(position)!!, x, y)
                viewModel.recyclerViewScrollable = false
                return true
            }
        })

        binding.recyclerview.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                viewModel.onLongClickCoordinate = Pair(e.rawX.toInt(), e.rawY.toInt())
                if (e.action == MotionEvent.ACTION_DOWN) {
                    viewModel.itemClickable = !viewModel.popupMenuIsShowing
                }
                if (e.action == MotionEvent.ACTION_UP) {
                    viewModel.recyclerViewScrollable = true
                }
                viewModel.popupMenuIsShowing = freePopupMenu.isShowing
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.getCurrentPath().isEmpty()) FileUtil.getRootPath() else viewModel.getCurrentPath().let { path ->
            viewModel.getListFiles(path)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.positionAndOffset = getPositionAndOffset()
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
            res = Pair(layoutManager.getPosition(firstView), firstView.top - 28)
        }
        return res
    }
}