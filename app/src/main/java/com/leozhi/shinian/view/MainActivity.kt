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
import com.leozhi.shinian.Preference
import com.leozhi.shinian.R
import com.leozhi.shinian.databinding.ActivityMainBinding
import com.leozhi.shinian.util.FileUtil
import com.leozhi.shinian.util.PermissionUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewbind()
    val viewModel: MainViewModel by viewModel()

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
                "按钮被点击了".showToast(this)
            }
        }
    }

    // 用于 Android 11 以下系统获取权限后回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> Preference.rootPath = FileUtil.getRootPath()
        }
    }

    // 用于 Android 11 获取权限后回调
    private val contract = object : ActivityResultContract<Any, Any>() {
        @RequiresApi(Build.VERSION_CODES.R)
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        }

        override fun parseResult(resultCode: Int, intent: Intent?) {}
    }

    private val launcher = registerForActivityResult(contract) {
        Preference.rootPath = FileUtil.getRootPath()
    }
}