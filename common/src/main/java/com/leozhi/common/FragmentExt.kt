package com.leozhi.common

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import java.util.*

/**
 * @author leozhi
 * @date 2021/3/7
 */
fun FragmentActivity.backPressed(duration: Long = 2000) {
    var isBackPressed = false
    this.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!isBackPressed) {
                "再按一次退出".showToast(this@backPressed)
                isBackPressed = true
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        isBackPressed = false
                    }
                }, duration)
            } else {
                this@backPressed.finish()
            }
        }
    })
}