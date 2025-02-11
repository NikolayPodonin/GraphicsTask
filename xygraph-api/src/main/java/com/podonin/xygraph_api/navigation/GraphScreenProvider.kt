package com.podonin.xygraph_api.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.podonin.xygraph_api.entity.XYPoint

interface GraphScreenProvider {
    fun getXYGraphScreen(data: List<XYPoint>): FragmentScreen
}