package com.leozhi.common

import android.content.Context
import android.widget.Toast

/**
 * @author leozhi
 * @date 2021/3/6
 */
fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}