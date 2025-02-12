package com.podonin.graphicstask.main

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.podonin.xygraph_api.entity.XYPoint
import com.podonin.xygraph_api.navigation.GraphScreenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val router: Router,
    private val graphScreenProvider: GraphScreenProvider
) : ViewModel() {

    fun navigateToFeatureScreen() {
        router.navigateTo(
            graphScreenProvider.getXYGraphScreen(generateTableData(Random.nextInt(1, 100)))
        )
    }

    private fun generateTableData(pointsCount: Int): List<XYPoint> {
        return List(pointsCount) { index ->
            XYPoint(
                index + 1,
                Random.nextInt(-10, 10) + Random.nextFloat(),
                Random.nextInt(-10, 10) + Random.nextFloat()
            )
        }
    }
}
