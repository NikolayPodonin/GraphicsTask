package com.podonin.xygraph_api.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.podonin.points_count_api.domain.entity.XYPoint

interface GraphScreenProvider {
    fun getXYGraphScreen(data: List<XYPoint>): FragmentScreen
}