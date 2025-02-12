package com.podonin.xygraph.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.podonin.xygraph.R
import com.podonin.xygraph.databinding.FragmentXYGraphBinding
import com.podonin.xygraph.navigation.entity.GraphScreenData
import com.podonin.xygraph.presentation.table.TableItem
import com.podonin.xygraph.presentation.table.adapter.TableAdapter

class GraphFragment : Fragment() {

    private var binding: FragmentXYGraphBinding? = null
    private val tableAdapter: TableAdapter = TableAdapter()

    private var screenData: GraphScreenData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            screenData = it.getParcelable(ARG_POINTS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentXYGraphBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.adapter = tableAdapter
        val xyPoints = screenData?.points.orEmpty()
        val itemList = listOf(
            TableItem.TitleItem(
                title = getString(R.string.table_index),
                xLabel = getString(R.string.table_x_label),
                yLabel = getString(R.string.table_y_label)
            )
        ) + xyPoints.map {
            TableItem.PointItem(it)
        }

        tableAdapter.setData(itemList)
        binding?.graphView?.setPoints(
            xyPoints.sortedBy { it.x }
        )

        binding?.smoothButton?.setOnClickListener {
            binding?.graphView?.switchIsSmooth()
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