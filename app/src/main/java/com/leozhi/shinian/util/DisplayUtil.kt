package com.leozhi.shinian.util

import android.content.Context
import android.graphics.Point
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * @author leozhi
 * @date 2021/3/10
 */
object DisplayUtil {
    /**
     * 获取屏幕尺寸
     */
    fun getScreenMetrics(context: Context): Point {
        return context.resources.displayMetrics.let { Point(it.widthPixels, it.heightPixels) }
    }

    fun dip2px(context: Context, dip: Int) = (dip * context.resources.displayMetrics.density + 0.5f).toInt()

    /**
     * 获取显示的第一个 item 的位置和相对于 top 的偏移量
     *
     * @return (position, offset) 位置，偏移量
     */
    fun getPositionAndOffset(manager: LinearLayoutManager): Pair<Int, Int> {
        return manager.getChildAt(0)?.let { Pair(manager.getPosition(it), it.top - 34) } ?: Pair(0, 0)
    }
}