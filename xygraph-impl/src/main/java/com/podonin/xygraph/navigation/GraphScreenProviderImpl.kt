package com.podonin.xygraph.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.podonin.points_count_api.domain.entity.XYPoint
import com.podonin.xygraph.navigation.entity.GraphScreenData
import com.podonin.xygraph.presentation.graphscreen.GraphFragment
import com.podonin.xygraph_api.navigation.GraphScreenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GraphScreenProviderImpl @Inject constructor() : GraphScreenProvider {
    override fun getXYGraphScreen(data: List<XYPoint>) = object : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment {
            return GraphFragment.newInstance(GraphScreenData(data))
        }
    }
}