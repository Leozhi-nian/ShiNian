package com.leozhi.shinian.util

import android.content.Context
import android.graphics.Point

/**
 * @author leozhi
 * @date 2021/3/10
 */
object DisplayUtil {
    fun getScreenMetrics(context: Context): Point {
        val dm = context.resources.displayMetrics
        val screenWidth = dm.widthPixels
        val screenHeight = dm.heightPixels
        return Point(screenWidth, screenHeight)
    }

    fun dip2px(context: Context, dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }
}