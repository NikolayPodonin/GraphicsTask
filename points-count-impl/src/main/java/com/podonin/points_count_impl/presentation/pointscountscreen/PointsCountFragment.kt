package com.podonin.points_count_impl.presentation.pointscountscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.podonin.points_count_impl.databinding.FragmentPointsCountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PointsCountFragment : Fragment() {

    private var binding: FragmentPointsCountBinding? = null

    private val viewModel: PointsCountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPointsCountBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding?.letGoButton?.setOnClickListener {
            val inputText = binding?.inputPointsCount?.text?.toString()
            val pointsCount = inputText?.toIntOrNull() ?: return@setOnClickListener
            viewModel.requestPoints(pointsCount)
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showToast.collectLatest { stringRes ->
                    Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showLoader.collectLatest { isShow ->
                    binding?.loader?.isVisible = isShow
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = PointsCountFragment()
    }
}