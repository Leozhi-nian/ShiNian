package com.leozhi.shinian.view.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.hi.dhl.binding.viewbind
import com.leozhi.common.backPressed
import com.leozhi.common.showToast
import com.leozhi.shinian.databinding.FragmentMineBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MineFragment : Fragment() {
    private val binding: FragmentMineBinding by viewbind()
    private val viewModel: MineViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 监听返回按钮
        requireActivity().backPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}