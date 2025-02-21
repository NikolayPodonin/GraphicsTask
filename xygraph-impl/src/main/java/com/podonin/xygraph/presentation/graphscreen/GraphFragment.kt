package com.podonin.xygraph.presentation.graphscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.podonin.common_ui.utils.collectOnUi
import com.podonin.xygraph.R
import com.podonin.xygraph.databinding.FragmentXYGraphBinding
import com.podonin.xygraph.navigation.entity.GraphScreenData
import com.podonin.xygraph.presentation.graphscreen.table.TableItem
import com.podonin.xygraph.presentation.graphscreen.table.adapter.TableAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GraphFragment : Fragment() {

    private var binding: FragmentXYGraphBinding? = null
    private val tableAdapter: TableAdapter = TableAdapter()

    private val viewModel: GraphViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.setScreenData(it.getParcelable(ARG_POINTS))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentXYGraphBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding?.run {
            recyclerView.adapter = tableAdapter

            smoothButton.setOnClickListener {
                graphView.switchIsSmooth()
            }
            saveGraphButton.setOnClickListener {
                val bitmap = graphView.saveGraphToBitmap()
                viewModel.saveGraphToGallery(bitmap)
            }
        }
    }

    private fun initObservers() {
        collectOnUi(viewModel.showToast) { stringRes ->
            Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
        }
        collectOnUi(viewModel.items) { items ->
            tableAdapter.setData(items)
        }
        collectOnUi(viewModel.points) { points ->
            binding?.run {
                graphView.setPoints(points)
                smoothButton.isVisible = points.isNotEmpty()
                saveGraphButton.isVisible = points.isNotEmpty()
            }
        }
    }


    companion object {
        private const val ARG_POINTS = "ARG_POINTS"

        @JvmStatic
        fun newInstance(xyData: GraphScreenData) =
            GraphFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_POINTS, xyData)
                }
            }
    }
}