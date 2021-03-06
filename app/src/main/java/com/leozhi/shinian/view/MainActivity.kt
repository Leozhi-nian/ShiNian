package com.leozhi.shinian.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.hi.dhl.binding.viewbind
import com.leozhi.common.showToast
import com.leozhi.shinian.MyApp
import com.leozhi.shinian.R
import com.leozhi.shinian.databinding.ActivityMainBinding
import com.leozhi.shinian.util.FileUtil
import com.leozhi.shinian.util.LogUtil
import com.leozhi.shinian.util.PermissionUtil
import java.io.File

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewbind()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Navigation 导航
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavBar, navController)

        binding.fab.setOnClickListener {
            // 获取访问所有文件的权限
            PermissionUtil.filesAccessPermission(this, launcher)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                val rootPath = FileUtil.getRootPath()
                rootPath?.showToast(this)
                if (rootPath != null) {
                    val rootFile = File(rootPath)
                    val files = rootFile.listFiles()
                    if (files == null) {
                        LogUtil.d("MainActivity", "空目录")
                    } else {
                        for (file in files) {
                            LogUtil.d("MainActivity", file.absolutePath)
                        }
                    }
                }
            }
        }
    }

    private val launcher = registerForActivityResult(object : ActivityResultContract<Any, String>() {
        @RequiresApi(Build.VERSION_CODES.R)
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        }

        @RequiresApi(Build.VERSION_CODES.R)
        override fun parseResult(resultCode: Int, intent: Intent?): String {
            return if (!Environment.isExternalStorageManager()) {
                "存储权限获取失败"
            } else {
                "存储权限获取成功"
            }
        }
    }) { it.showToast(MyApp.context) }
}