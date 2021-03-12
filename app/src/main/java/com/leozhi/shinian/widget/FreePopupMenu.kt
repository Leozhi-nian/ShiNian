package com.leozhi.shinian.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leozhi.shinian.R
import com.leozhi.shinian.util.DisplayUtil

/**
 * @author leozhi
 * @date 2021/3/10
 */
@Suppress("unused")
class FreePopupMenu(private val context: Context) : PopupWindow(context) {
    companion object {
        private const val ANCHORED_GRAVITY = Gravity.TOP or Gravity.START
        private const val DEFAULT_ITEM_HEIGHT = 48
        private const val DEFAULT_MENU_WIDTH = 150
        private const val X_OFFSET = 10
    }

    @SuppressLint("RestrictedApi")
    private var mMenu: Menu = MenuBuilder(context)
    private var mRecyclerView: RecyclerView
    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mMenuAdapter: MenuAdapter by lazy { MenuAdapter() }
    private var mOnMenuItemClickListener: OnMenuItemClickListener? = null
    private val mMenuItems = ArrayList<MenuItem>()
    private var mScreenPoint: Point = DisplayUtil.getScreenMetrics(context)
    private var mMenuWidth: Int = DisplayUtil.dip2px(context, DEFAULT_MENU_WIDTH)

    init {
        contentView = mInflater.inflate(R.layout.layout_free_popup_menu, LinearLayout(context))
        mRecyclerView = contentView.findViewById(R.id.recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mMenuAdapter
        width = mMenuWidth
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(null)
        isOutsideTouchable = true
        elevation = 15f
    }

    fun show(anchorView: View) {
        val location = IntArray(2)
        anchorView.getLocationInWindow(location)
        if (!tryShow(anchorView, 0, location[1])) {
            throw IllegalStateException("FloatMenu cannot be used without an anchor")
        }
    }

    fun show(anchorView: View, x: Int, y: Int) {
        if (!tryShow(anchorView, x, y)) {
            throw IllegalStateException("FloatMenu cannot be used without an anchor")
        }
    }

    private fun tryShow(anchorView: View?, x: Int, y: Int): Boolean {
        if (isShowing) {
            return true
        }

        if (anchorView == null) {
            return false
        }

        showPopup(anchorView, x, y)
        return true
    }

    private fun showPopup(anchorView: View, x: Int, y: Int) {
        if (!mMenu.hasVisibleItems()) {
            return
        }

        // 设置菜单项可见性
        val size = mMenu.size()
        mMenuItems.clear()
        for (i in 0 until size) {
            val item = mMenu.getItem(i)
            if (item.isVisible) {
                mMenuItems.add(item)
            }
        }
        mMenuAdapter.notifyItemRangeChanged(0, size)
        // show
        val menuHeight = DisplayUtil.dip2px(context, DEFAULT_ITEM_HEIGHT * mMenuItems.size)
        if (x <= mScreenPoint.x / 2) {
            if (y + menuHeight < mScreenPoint.y) {
                animationStyle = R.style.top_for_left
                showAtLocation(anchorView, ANCHORED_GRAVITY, x + X_OFFSET, y)
            } else {
                animationStyle = R.style.bottom_for_left
                showAtLocation(anchorView, ANCHORED_GRAVITY, x + X_OFFSET, y - menuHeight)
            }
        } else {
            if (y + menuHeight < mScreenPoint.y) {
                animationStyle = R.style.top_for_right
                showAtLocation(anchorView, ANCHORED_GRAVITY, x - mMenuWidth - X_OFFSET, y)
            } else {
                animationStyle = R.style.bottom_for_right
                showAtLocation(anchorView, ANCHORED_GRAVITY, x - mMenuWidth + X_OFFSET, y - menuHeight)
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        mMenuItems.clear()
    }

    fun getMenu():Menu{
        return mMenu
    }

    @NonNull
    private fun getMenuInflater(): MenuInflater {
        return MenuInflater(context)
    }

    fun inflate(@MenuRes menuRes: Int) {
        getMenuInflater().inflate(menuRes, mMenu)
    }

    /**
     * 设置菜单点击监听
     *
     * @param onMenuItemClickListener 菜单项点击监听器
     */
    fun setOnMenuItemClickListener(onMenuItemClickListener: OnMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener
    }

    inner class MenuAdapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(mInflater.inflate(R.layout.layout_free_popup_menu_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemText.text = mMenuItems[position].title
            holder.itemText.setOnClickListener {
                mOnMenuItemClickListener?.onMenuItemClick(mMenuItems[position])
                dismiss()
            }
        }

        override fun getItemCount() = mMenuItems.size

    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var itemText: TextView = itemView.findViewById(R.id.item_text)
}

/**
 * 菜单项点击监听器
 */
interface OnMenuItemClickListener {
    fun onMenuItemClick(item: MenuItem)
}